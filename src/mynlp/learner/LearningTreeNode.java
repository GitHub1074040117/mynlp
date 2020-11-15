package mynlp.learner;

import java.util.HashMap;

class LearningTreeNode {
    private HashMap<String, LearningTreeNode> children;
    private int frequency;
    private String key;

    LearningTreeNode() {
        children = new HashMap<>();
        frequency = 0;
        key = "";
    }


    // 频度加一
    void occur() {
        this.frequency++;
    }

    // 判断是否包含键值
    boolean containKey(String key) {
        return children.containsKey(key);
    }

    // 添加键值和节点
    void put(String key, LearningTreeNode node) {
        node.key = key;
        children.put(key, node);
    }

    void removeChild(String key) {
        frequency -= children.get(key).frequency;
        children.remove(key);
    }




    /*getter*/
    // 根据键值获取节点
    LearningTreeNode getChild(String key) {
        return children.get(key);
    }

    HashMap<String, LearningTreeNode> getChildren() {
        return children;
    }

    int getFreq() {
        return frequency;
    }

    String getKey() {
        return key;
    }
}
