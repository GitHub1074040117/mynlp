package mynlp.gram;

import java.util.HashMap;

/**
 * 语法树节点
 * */
class GramTreeNode {
    private HashMap<String, GramTreeNode> children;
    private int frequency;
    private String  key;

    GramTreeNode() {
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
    void put(String key, GramTreeNode node) {
        node.key = key;
        children.put(key, node);
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

    // 获取子节点的最大似然估计
    double getChildMLE(String key) {
        if (!containKey(key)) return 0;
        return children.get(key).frequency*1.0 / frequency;
    }

    void removeChild(String key) {
        frequency -= children.get(key).frequency;
        children.remove(key);
    }



    /*getter*/
    // 根据键值获取节点
    GramTreeNode getChild(String key) {
        return children.get(key);
    }

    HashMap<String, GramTreeNode> getChildren() {
        return children;
    }

    int getFreq() {
        return frequency;
    }

    String getKey() {
        return key;
    }


}
