package mynlp.dict;

import java.util.ArrayList;

public class Dictionary {
    private static final int MAX_WORD_LENGTH = 6; // 词语最大长度
    private static final int MIN_WORD_LENGTH = 2; // 词语最小长度
    private DictStorage dictStorage;

    public Dictionary() {
        dictStorage = new DictStorage();
    }

    // 对一段话进行分词，生词分开，双向最大匹配
    public ArrayList<String> tokenize(String sentence) {
        ArrayList<String> result;
        ArrayList<String> forward = forwardMaxMatch(sentence);
        ArrayList<String> backward = backwardMaxMatch(sentence);
        result = forward.size() > backward.size() ? forward : backward;
        return result;
    }

    // 向前最大匹配
    private ArrayList<String> forwardMaxMatch(String sentence) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < sentence.length(); i++) {
            int len;
            for (len = MAX_WORD_LENGTH; len >= MIN_WORD_LENGTH; len--) {
                if (i + len > sentence.length()) continue;
                String word = sentence.substring(i, i + len);
                if (dictStorage.search(word)) {
                    result.add(word);
                    i = i + len - 1;
                    break;
                }
            }
            // 未查找到词，将单个字添加进去
            if (len < MIN_WORD_LENGTH) {
                result.add(sentence.substring(i, i + 1));
            }
        }
        return result;
    }

    // 向后最大匹配
    private ArrayList<String> backwardMaxMatch(String sentence) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = sentence.length(); i > 0; i--) {
            int len;
            for (len = MAX_WORD_LENGTH; len >= MIN_WORD_LENGTH; len--) {
                if (i - len < 0) continue;
                String word = sentence.substring(i - len, i);
                if (dictStorage.search(word)) {
                    result.add(0,word);
                    i = i - len + 1;
                    break;
                }
            }
            // 未查找到词，将单个字添加进去
            if (len < MIN_WORD_LENGTH) {
                result.add(0, sentence.substring(i-1, i));
            }
        }
        return result;
    }

}
