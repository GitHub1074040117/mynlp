package mynlp.dict;

import java.util.ArrayList;

/**
 * 字典树
 * */

class DictTrie {
    private DictTrieNode root; // 根节点

    // 根据词汇表创建
    DictTrie(ArrayList<String> words) {
        root = new DictTrieNode();
        for (String word : words) {
            insert(word);
        }
    }

    private void insert(String word) {
        DictTrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            if (node.notContainsKey(c)) {
                node.put(c, new DictTrieNode());
            }
            node = node.get(c);
        }
        node.wordEnded();
    }

    boolean search(String word) {
        DictTrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            if (node.notContainsKey(c)) {
                return false;
            }
            node = node.get(c);
        }
        return node.isWordEnd();
    }

}
