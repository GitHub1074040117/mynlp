package mynlp.learner;

import mynlp.helper.ArrayListHelper;
import mynlp.helper.WordHelper;

import java.util.ArrayList;

class LearningTree {
    private LearningTreeNode root;
    private int degree;

    LearningTree(int degree) {
        if (degree <= 0) {
            new Exception("语法树的度数必须大于0").printStackTrace();
            return;
        }
        this.degree = degree;
        root = new LearningTreeNode();

    }

    // 统计已经分词的句子，构造语法树
    void build(ArrayList<String> tokenizedSentence) {
        for (int i = 0; i < tokenizedSentence.size(); i++ ) {
            insert(ArrayListHelper.subArrayList(tokenizedSentence, i, i + degree));
        }
    }

    // 插入一个N元词组
    private void insert(ArrayList<String> tuple) {
        LearningTreeNode node = root;
        node.occur(); // 频度加一
        for (String word : tuple) {
            if (!node.containKey(word)) {
                node.put(word, new LearningTreeNode());
            }
            node = node.getChild(word);
            node.occur();
        }
    }

    // 删除根节点中的某些孩子，入停用词，非汉字字符等；For learning
    void optimizeRootChildren() {
        ArrayList<String> deleted = new ArrayList<>();
        for (String key : root.getChildren().keySet()) {
            if (WordHelper.isIrregular(key) || WordHelper.isStopWord(key)) {
                deleted.add(key);
            }
        }
        for (String key : deleted) {
            root.removeChild(key);
        }
    }

    // 获取一个节点所在的代数
    /*int getGeneration(GramTreeNode node) {
        int generation = degree - 1;
        for (int i = 0; i < degree - 1; i++) {
            GramTreeNode next;
            next = node.getFirstChild();
            if (next == null) break;
            node = next;
            generation--;
        }
        return generation;
    }*/





    /*getter*/

    LearningTreeNode getRoot() {
        return root;
    }

}
