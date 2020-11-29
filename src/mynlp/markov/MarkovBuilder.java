package mynlp.markov;

import mynlp.gram.GramOuter;
import mynlp.dict.Dictionary;
import mynlp.utils.ContentUtility;
import mynlp.utils.FileUtility;
import mynlp.utils.StringUtility;

import java.util.ArrayList;

/**
 * Count只是Mtx的早期形式，Count经过归一化处理后即为Markov模型中的Mtx
 * */

class MarkovBuilder {
    private static final String S = "S";
    private static final String B = "B";
    private static final String M = "M";
    private static final String E = "E";
    private static final int GRAM_DEGREE = 3; // 3元语法模型
    private static final String[] STATE_SET = {S, B, M, E};
    private Dictionary dictionary;
    private OneDimMatrix initCount;     // 初始计数矩阵
    private TwoDimMatrix transCount;    // 转移计数矩阵
    private TwoDimMatrix omitCount;     // 发射计数矩阵
    private OneDimMatrix termCount;     // 终结计数矩阵
    private GramOuter gramOuter;        // N元语法模型

    MarkovBuilder(String filePath) {
        initCount = new OneDimMatrix();
        transCount = new TwoDimMatrix();
        omitCount = new TwoDimMatrix();
        termCount = new OneDimMatrix();
        gramOuter = new GramOuter(filePath, GRAM_DEGREE);
        dictionary = new Dictionary();
        ArrayList<String> content = FileUtility.readFile(filePath);
        content = ContentUtility.specifySentenceFromContent(content);
        for (String sentence : content) {
            ArrayList<String> tagged = tagging(sentence);
            buildModel(tagged);
        }
    }

    // 根据观测序列获取马尔可夫模型
    MarkovModel getMarkovModel(String[] observations) {
        predictUnknownOmit(observations);
        OneDimMatrix initMtx = normalizeInitCount();
        TwoDimMatrix transMtx = normalizeTransCount();
        TwoDimMatrix omitMtx = normalizeOmitCount(observations);
        OneDimMatrix termMtx = normalizeTermCount();
        return new MarkovModel(initMtx, transMtx, omitMtx, termMtx, observations, STATE_SET);
    }

    // 对分词的句子进行标记, 返回已标记序列
    private ArrayList<String> tagging(String sentence) {
        ArrayList<String> tokSentence = dictionary.tokenize(sentence);
        ArrayList<String> tagged = new ArrayList<>();
        for (String word : tokSentence) {
            if (word.length() == 1) {
                tagged.add(word);
                tagged.add(S);
            } else {
                tagged.add(word.substring(0,1));
                tagged.add(B);
                for (int i = 1; i < word.length() - 1; i++) {
                    tagged.add(word.substring(i, i + 1));
                    tagged.add(M);
                }
                tagged.add(word.substring(word.length() - 1));
                tagged.add(E);
            }
        }
        return tagged;
    }

    // 统计构造
    private void buildModel(ArrayList<String> tagged) {
        String preState; // 向前状态
        String preObser; // 向前观测
        double preProb;  // 向前概率
        String state; // 当前状态
        String obser; // 当前观测
        String postState; // 向后状态
        String postObser; // 向后观测
        double postProb;  // 向后概率
        int size = tagged.size();
        if (tagged.size() <= 3) {
            return;
        }
        state = tagged.get(1);
        obser = tagged.get(0);
        postState = tagged.get(3);
        postObser = tagged.get(2);
        postProb = gramOuter.getAfterProb(obser, postObser);
        if (state.equals(S) && postState.equals(S)) {
            // #SS
            initCountAdd(S, 1 - postProb);
            initCountAdd(B, postProb);
            omitCountAdd(S, obser, 1 - postProb);
            omitCountAdd(B, obser, postProb);
        } else {
            initCountAdd(state, 1);
            omitCountAdd(state, obser, 1);
        }

        for (int i = 3; i < size - 2; i = i + 2) {
            state = tagged.get(i);
            obser = tagged.get(i - 1);
            preState = tagged.get(i - 2);
            preObser = tagged.get(i - 3);
            preProb = gramOuter.getAfterProb(preObser, obser);
            postState = tagged.get(i + 2);
            postObser = tagged.get(i + 1);
            postProb = gramOuter.getAfterProb(obser, postObser);
            if (!state.equals(S)) {
                transCountAdd(preState, state, 1);
                omitCountAdd(state, obser, 1);
                continue;
            }
            // ESB
            if (preState.equals(E) && postState.equals(B)) {
                transCountAdd(E, S, 1);
                omitCountAdd(S, obser, 1);
            }
            // ESS
            if (preState.equals(E) && postState.equals(S)) {
                transCountAdd(E, S, 1 - postProb);
                transCountAdd(E, B, postProb);
                omitCountAdd(S, obser, 1 - postProb);
                omitCountAdd(B, obser, postProb);
            }
            // SSB
            if (preState.equals(S) && postState.equals(B)) {
                transCountAdd(S, S, 1 - preProb / 2);
                transCountAdd(E, S, 1 - preProb / 2);
                transCountAdd(B, E, preProb / 2);
                transCountAdd(M, E, preProb / 2);
                omitCountAdd(S, obser, 1 - preProb);
                omitCountAdd(E, obser, preProb);
            }
            // SSS
            if (preState.equals(S) && postState.equals(S)) {
                transCountAdd(S, S, (1 - preProb) * (1 - postProb) / 2);
                transCountAdd(E, S, (1 - preProb) * (1 - postProb) / 2);
                transCountAdd(S, B, (1 - preProb) * postProb / 2);
                transCountAdd(E, B, (1 - preProb) * postProb / 2);
                transCountAdd(B, M, preProb * postProb / 2);
                transCountAdd(M, M, preProb * postProb / 2);
                transCountAdd(B, E, preProb * (1 - postProb) / 2);
                transCountAdd(M, E, preProb * (1 - postProb) / 2);

                omitCountAdd(S, obser, (1 - preProb) * (1 - postProb));
                omitCountAdd(B, obser, (1 - preProb) * postProb);
                omitCountAdd(M, obser, preProb * postProb);
                omitCountAdd(E, obser, preProb * (1 - postProb));
            }
        }

        state = tagged.get(size - 1);
        preState = tagged.get(size - 3);
        obser = tagged.get(size - 2);
        preObser = tagged.get(size - 4);
        preProb = gramOuter.getAfterProb(preObser, obser);
        if (state.equals(S) && preState.equals(S)) {
            // SS#
            transCountAdd(S, S, (1 - preProb) / 2);
            transCountAdd(E, S, (1 - preProb) / 2);
            transCountAdd(B, E, preProb / 2);
            transCountAdd(M, E, preProb / 2);
            omitCountAdd(S, obser, 1 - preProb);
            omitCountAdd(E, obser, preProb);
            termCountAdd(S, 1 - preProb);
            termCountAdd(E, preProb);
        } else {
            transCountAdd(preState, state, 1);
            omitCountAdd(state, obser, 1);
            termCountAdd(state, 1);
        }
    }

    // 初始计数矩阵增值
    private void initCountAdd(String state, double value) {
        initCount.add(state, value);
    }

    // 转移计数矩阵增值
    private void transCountAdd(String preState, String state, double value) {
        transCount.add(preState, state, value);
    }

    // 发射计数矩阵增值
    private void omitCountAdd(String state, String obser, double value) {
        omitCount.add(state, obser, value);
    }

    // 终结计数矩阵增值
    private void termCountAdd(String state, double value) {
        termCount.add(state, value);
    }

    // 归一化初始计数矩阵
    private OneDimMatrix normalizeInitCount() {
        double sum = 0;
        OneDimMatrix initMtx = new OneDimMatrix();
        for (String key : initCount.keySet()) {
            sum += initCount.get(key);
        }
        for (String key : initCount.keySet()) {
            if (sum == 0) initMtx.put(key, 0.0);
            else initMtx.put(key, initCount.get(key) / sum);
        }
        return initMtx;
    }

    // 归一化转移计数矩阵
    private TwoDimMatrix normalizeTransCount() {
        TwoDimMatrix transMtx = new TwoDimMatrix();
        for (String i : transCount.keySet()) {
            double sum = 0;
            for (String j : transCount.keySet()) {
                sum += transCount.get(i, j);
            }

            for (String j : transCount.keySet()) {
                if (sum == 0) transMtx.put(i, j, 0);
                else transMtx.put(i, j, transCount.get(i, j) / sum);
            }
        }
        return smoothingTransMtx(transMtx);
    }

    // 平滑转移计数矩阵
    private TwoDimMatrix smoothingTransMtx(TwoDimMatrix transMtx) {
        double a, b;
        a = transMtx.get(B, E);
        b = transMtx.get(B, M);
        transMtx.put(B, E, a / 2 + 0.25);
        transMtx.put(B, M, b / 2 + 0.25);

        a = transMtx.get(S, B);
        b = transMtx.get(S, S);
        transMtx.put(S, B, a / 2 + 0.25);
        transMtx.put(S, S, b / 2 + 0.25);

        a = transMtx.get(E, B);
        b = transMtx.get(E, S);
        transMtx.put(E, B, a / 2 + 0.25);
        transMtx.put(E, S, b / 2 + 0.25);

        a = transMtx.get(M, E);
        b = transMtx.get(M, M);
        transMtx.put(M, E, a / 2 + 0.25);
        transMtx.put(M, M, b / 2 + 0.25);
        return transMtx;
    }

    // 归一化发射计数矩阵，仅对观测的序列计数进行归一
    private TwoDimMatrix normalizeOmitCount(String[] observations) {
        String[] obserUnique = StringUtility.getUniqueStrings(observations);
        TwoDimMatrix omitMtx = new TwoDimMatrix();
        for (String i : omitCount.keySet()) {
            double sum = 0;
            for (String obs : obserUnique) {
                sum += omitCount.get(i, obs);
            }
            for (String obs : obserUnique) {
                if (sum == 0) omitMtx.put(i, obs, 0);
                else omitMtx.put(i, obs, omitCount.get(i, obs) / sum);
            }
        }
        return omitMtx;
    }

    // 归一化终结计数矩阵
    private OneDimMatrix normalizeTermCount() {
        double sum = 0;
        OneDimMatrix initMtx = new OneDimMatrix();
        for (String key : termCount.keySet()) {
            sum += termCount.get(key);
        }
        for (String key : termCount.keySet()) {
            if (sum == 0) initMtx.put(key, 0.0);
            else initMtx.put(key, termCount.get(key) / sum);
        }
        return initMtx;
    }

    // 估计未被统计过的观测
    private void predictUnknownOmit(String[] observations) {
        ArrayList<String> tagged = tagging(StringUtility.toString(observations));
        buildModel(tagged);
    }

}
