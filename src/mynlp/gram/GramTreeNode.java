package mynlp.gram;

import java.util.HashMap;

/**
 * 语法树节点
 * */
class GramTreeNode {
    private HashMap<String, GramTreeNode> children;
    private double frequency;

    GramTreeNode() {
        children = new HashMap<>();
        frequency = 0;
    }

    // 判断是否包含键值
    boolean containKey(String key) {
        return children.containsKey(key);
    }

    // 添加键值和节点
    void put(String key, GramTreeNode node) {
        children.put(key, node);
        frequency += node.getFreq();
    }

    // 获取随机的子节点的键值
    private GramTreeNode getRandomChild() {
        return GramComputer.wheelSelection(children);
    }

    // 获取孩子的key
    String getChildKey(GramTreeNode child) {
        for (String key : children.keySet()) {
            if (child.equals(children.get(key))) return key;
        }
        //new Exception("Child not found!").printStackTrace();
        return "";
    }

    // 获取随机孩子的key
    String getRandomChildKey() {
        return getChildKey(getRandomChild());
    }

    /*getter*/
    // 根据键值获取节点
    GramTreeNode getChild(String key) {
        if (children.containsKey(key)) {
            return children.get(key);
        }
        return new GramTreeNode();
    }

    HashMap<String, GramTreeNode> getChildren() {
        return children;
    }

    double getFreq() {
        return frequency;
    }

    // 设置频度
    void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    void addFrequency(double frequency) {
        this.frequency += frequency;
    }

}
