package mynlp.storage;

import mynlp.helper.SpaceHelper;
import mynlp.helper.FileHelper;

import java.util.ArrayList;

public class Storage {
    private Trie trie;
    private static final String STORE_PATH = "src/mynlp/storage/corpora/";
    private static final String WORD_2 = STORE_PATH + "word_2.txt";
    private static final String WORD_3 = STORE_PATH + "word_3.txt";
    private static final String WORD_4 = STORE_PATH + "word_4.txt";

    public Storage() {
        ArrayList<String> words = readWords(WORD_2);
        words.addAll(readWords(WORD_3));
        words.addAll(readWords(WORD_4));
        trie = new Trie(words);
    }

    // 查询词汇
    public boolean search(String word) {
        return trie.search(word);
    }

    // 读取词汇
    private ArrayList<String> readWords(String filePath) {
        ArrayList<String> content = FileHelper.readFile(filePath);
        return SpaceHelper.extractWordsSimplyBySpace(content);
    }

}
