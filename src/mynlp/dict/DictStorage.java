package mynlp.dict;

import mynlp.utils.SpaceUtility;
import mynlp.utils.FileUtility;

import java.util.ArrayList;

class DictStorage {
    private DictTrie dictTrie;
    private static final String STORE_PATH = "src/mynlp/dict/dictionary/";
    private static final String WORD_2 = STORE_PATH + "word_2.txt";
    private static final String WORD_3 = STORE_PATH + "word_3.txt";
    private static final String WORD_4 = STORE_PATH + "word_4.txt";

    DictStorage() {
        ArrayList<String> words = readWords(WORD_2);
        words.addAll(readWords(WORD_3));
        words.addAll(readWords(WORD_4));
        dictTrie = new DictTrie(words);
    }

    // 查询词汇
    boolean search(String word) {
        return dictTrie.search(word);
    }

    // 读取词汇
    private ArrayList<String> readWords(String filePath) {
        ArrayList<String> content = FileUtility.readFile(filePath);
        return SpaceUtility.extractWordsSimplyBySpace(content);
    }

}
