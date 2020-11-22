package mynlp.tokenize;

import mynlp.helper.FileHelper;
import mynlp.helper.SpaceHelper;
import mynlp.storage.Storage;

import java.util.ArrayList;

public class Tokenizer {
    private static final String TEMP_PATH = "src/mynlp/storage/temp/";
    private static final int MAX_WORD_LENGTH = 6; // 词语最大长度
    private static final int MIN_WORD_LENGTH = 2; // 词语最小长度
    private Storage storage;

    public Tokenizer() {
        storage = new Storage();
    }

    // 读取一个文件，将内容分词后返回
    public ArrayList<String> tokenize(String filePath, boolean stopwordRemoved) {
        ArrayList<String> content = FileHelper.readFile(filePath);
        if (content == null) {
            new Exception("分词失败，文件读取错误！").printStackTrace();
            return null;
        }
        // 删除多余空格
        content = SpaceHelper.removeSpace(content);

        ArrayList<String> result = new ArrayList<>();
        for (String sentence : content) {
            result.addAll(tokenize(sentence));
        }
        return stopwordRemoved ? Stopword.removeStopwords(result) : result;
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
                if (storage.search(word)) {
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
                if (storage.search(word)) {
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
