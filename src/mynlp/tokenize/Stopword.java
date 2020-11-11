package mynlp.tokenize;

import mynlp.helper.SpaceHelper;

import java.util.ArrayList;

class Stopword {
    private static String[] STOPWORD = new String[]{"们", "的", "我", "你", "他", "了", "她", "是", "和", "吧", "把"};

    // 删除停用词，返回字符串数组
    static ArrayList<String> removeStopwords(ArrayList<String> content) {
        ArrayList<String> result = new ArrayList<>();
        for (String sentence : content) {
            result.addAll(removeStopwords(sentence));
        }
        return result;
    }

    private static ArrayList<String> removeStopwords(String sentence) {
        for (String stopword : STOPWORD) {
            sentence = sentence.replace(stopword, " ");
        }
        return SpaceHelper.extractWordsSimplyBySpace(sentence);
    }


}
