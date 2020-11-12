package mynlp.tokenize;

import mynlp.helper.FileHelper;
import mynlp.helper.SpaceHelper;
import mynlp.storage.Storage;

import java.util.ArrayList;

public class Tokenizer {
    private static final String TEMP_PATH = "src/mynlp/storage/temp/";
    private static final int MAX_WORD_LENGTH = 4; // 词语最大长度
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

    // 对一段话进行分词，生词分开
    public ArrayList<String> tokenize(String sentence) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < sentence.length(); i++) {
            int wordLength;
            for (wordLength = MAX_WORD_LENGTH; wordLength >= MIN_WORD_LENGTH; wordLength--) {
                if (i + wordLength > sentence.length()) continue;
                String word = sentence.substring(i, i + wordLength);

                // 查找该词是否存在
                if (storage.search(word)) {
                    // 添加熟词
                    result.add(word);
                    // 更新指针
                    i = i + wordLength - 1;
                    break;
                }
            }
            if (wordLength < MIN_WORD_LENGTH) {
                result.add(sentence.substring(i, i + 1));
            }
        }
        // 没有匹配到任何词的时候，将整段句子添加进去
        if (result.size() == 0) {
            result.add(sentence);
            return result;
        }
        return result;
    }

}
