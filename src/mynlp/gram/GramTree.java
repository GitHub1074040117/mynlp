package mynlp.gram;

import mynlp.helper.ArrayListHelper;

import java.util.ArrayList;

/**
 * 语法树
 * */
class GramTree {
    private GramTreeNode root;
    private int degree;
    private int lookAhead;

    GramTree(int degree) {
        if (degree <= 0) {
            new Exception("语法树的度数必须大于0").printStackTrace();
        }
        this.degree = degree;
        this.lookAhead = degree - 1;
        root = new GramTreeNode();
    }

    // 统计已经分词的句子，构造语法树
    void build(ArrayList<String> tokenizedSentence) {
        for (int i = 0; i < tokenizedSentence.size(); i++ ) {
            insert(ArrayListHelper.subArrayList(tokenizedSentence, i, i + degree));
        }
    }

    // 插入一个N元词组
    private void insert(ArrayList<String> tuple) {
        GramTreeNode node = root;
        node.occur(); // 频度加一
        for (String word : tuple) {
            if (!node.containKey(word)) {
                node.put(word, new GramTreeNode());
            }
            node = node.getChild(word);
            node.occur();
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
        ArrayList<String> subTuple = ArrayListHelper.subArrayList(tuple, tuple.size() - degree);
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





    /*getter*/

    GramTreeNode getRoot() {
        return root;
    }

    int getDegree() {
        return degree;
    }
}
