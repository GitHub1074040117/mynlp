package mynlp.gram;

import mynlp.utils.ArrayListUtility;

import java.util.ArrayList;

/**
 * 语法树
 * */
class GramTree {
    private GramTreeNode root;
    private int degree;

    GramTree(int degree) {
        if (degree <= 0) {
            new Exception("语法树的度数必须大于0").printStackTrace();
        }
        this.degree = degree;
        root = new GramTreeNode();
    }

    // 统计已经分词的句子，构造语法树
    void build(ArrayList<String> tokenizedSentence) {
        double frequency = 1;
        for (int i = 0; i < tokenizedSentence.size(); i++ ) {
            insert(ArrayListUtility.subArrayList(tokenizedSentence, i, i + degree), frequency);
        }
    }

    // 插入一个N元词组，并设置其出现的频率
    private void insert(ArrayList<String> tuple, double frequency) {
        GramTreeNode node = root;
        root.addFrequency(frequency);
        for (String word : tuple) {
            if (!node.containKey(word)) {
                GramTreeNode child = new GramTreeNode();
                node.put(word, child);
                child.setFrequency(frequency);
            } else {
                node.getChild(word).addFrequency(frequency);
            }
            node = node.getChild(word);
        }
    }

    // 根据词组预测下一个词
    String predict(ArrayList<String> tuple) {
        int bound = degree - 1;
        GramTreeNode node = root;
        String result = "";

        if (tuple.size() > bound) {
            new Exception("Size of tuple out of bound : " + tuple.size() + " : " + bound).printStackTrace();
            return result;
        }

        for (String word : tuple) {
            if (node.containKey(word)) {
                node = node.getChild(word);
            }
        }
        result = node.getRandomChildKey();
        return result;
    }

    // 根据输入的元组获取节点
    GramTreeNode getTreeNodeByTuple(ArrayList<String> tuple) {
        GramTreeNode node = root;
        ArrayList<String> subTuple = ArrayListUtility.subArrayList(tuple, tuple.size() - degree);
        for (String key : subTuple) {
            if (node.containKey(key)) {
                node = node.getChild(key);
            } else {
                new Exception("node not found : " + key).printStackTrace();
                return node;
            }
        }
        return node;
    }

    // 获取父子后跟概率, 如果为频度0则将频度设置为N0并添加入语法树中
    double getAfterProb(String parentKey, String childKey) {
        // 若节点不存在, 则添加
        if (!root.containKey(parentKey) || !root.getChild(parentKey).containKey(childKey)) {
            GramComputer computer = new GramComputer(this);
            double zeroCount = computer.estimateZero(); // 0计数的估计值
            ArrayList<String> tuple = new ArrayList<>();
            tuple.add(parentKey);
            tuple.add(childKey);
            insert(tuple, zeroCount);
        }
        GramTreeNode parent = root.getChild(parentKey);
        GramTreeNode child = parent.getChild(childKey);
        // 当计算不存在的父子节点概率时，会出现概率为1的情况，这是我们不想要的
        double parentFreq = parent.getFreq();
        double childFreq = child.getFreq();
        return childFreq / parentFreq;
    }

    /*getter*/

    GramTreeNode getRoot() {
        return root;
    }

}
