package mynlp.storage;

import java.util.ArrayList;

/**
 * 字典树
 * */

class Trie {
    private TrieNode root; // 根节点

    // 根据词汇表创建
    Trie(ArrayList<String> words) {
        root = new TrieNode();
        for (String word : words) {
            insert(word);
        }
    }

    private void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            if (!node.containKey(c)) {
                node.put(c, new TrieNode());
            }
            node = node.get(c);
        }
        node.wordEnded();
    }

    boolean search(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            if (!node.containKey(c)) {
                return false;
            }
            node = node.get(c);
        }
        return node.isWordEnd();
    }

}
