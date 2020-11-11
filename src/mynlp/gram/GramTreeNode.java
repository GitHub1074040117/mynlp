package mynlp.gram;

import java.util.HashMap;

/**
 * 语法树节点
 * */
class GramTreeNode {
    private HashMap<String, GramTreeNode> children;
    private int frequency;

    GramTreeNode() {
        children = new HashMap<>();
        frequency = 0;
    }

    // 根据键值获取节点
    GramTreeNode get(String key) {
        return children.get(key);
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
        children.put(key, node);
    }

    // 获取随机的子节点的键值
    String getRandomChildKey() {
        HashMap<String, Integer> freqMap = new HashMap<>(); // 每个词记录频度的Map
        String result;
        for (String key : children.keySet()) {
            freqMap.put(key, children.get(key).frequency);
        }
        result = GramComputer.rouletteWheelSelection(freqMap);
        return result;
    }

    /*getter*/

    HashMap<String, GramTreeNode> getChildren() {
        return children;
    }

    int getFreq() {
        return frequency;
    }


}
