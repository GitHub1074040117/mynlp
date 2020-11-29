package mynlp.dict;

import java.util.HashMap;

class DictTrieNode {
    private HashMap<Character, DictTrieNode> children; // 存放子节点，键值为字符
    private boolean wordEnd; // 判断到该节点时，一个词是否完整

    DictTrieNode() {
        children = new HashMap<>();
        wordEnd = false;
    }

    // 判断是否包含键值
    boolean notContainsKey(Character key) {
        return !children.containsKey(key);
    }

    // 判断是否完整
    boolean isWordEnd() {
        return wordEnd;
    }

    // 添加键值和节点
    void put(Character key, DictTrieNode node) {
        children.put(key, node);
    }

    // 根据键值获取节点
    DictTrieNode get(Character key) {
        return children.get(key);
    }

    void wordEnded() {
        this.wordEnd = true;
    }
}
