package mynlp.gram;

import java.util.HashMap;
import java.util.Random;

/**
 * 协助语法树完成一些计算和其他功能
 * */

class GramComputer {
    private GramTree tree;
    private int K = 6; // 大于k的计数认为是可靠的

    GramComputer(GramTree gramTree) {
        this.tree = gramTree;
    }

    // 轮盘赌选择法
    static GramTreeNode wheelSelection(HashMap<String, GramTreeNode> children) {
        HashMap<String, Double> hashMap = new HashMap<>();
        for (String key : children.keySet()) {
            hashMap.put(key, children.get(key).getFreq());
        }

        double[] a = new double[hashMap.size()];
        String[] b = new String[hashMap.size()];
        String result = "";
        int len = 0;
        int sum = 0;
        for (String key : hashMap.keySet()) {
            a[len] = hashMap.get(key);
            sum += a[len];
            b[len] = key;
            len++;
        }
        for (int i = 0; i < len; i++ ) {
            if (i == 0) {
                a[i] = a[i] / sum;
            } else {
                a[i] = a[i] / sum + a[i - 1];
            }
        }
        Random random = new Random();
        double p = random.nextDouble();
        for (int i = 0; i < len; i++ ) {
            if (p < a[i]) {
                result = b[i];
                break;
            }
        }

        return children.get(result);
    }

    // 使用Good-Turing平滑算法统计Nc，为估计0计数的N元语法出现的频率
    double estimateZero() {
        double[] counts = new double[K];
        double zeroCount;
        GramTreeNode root = tree.getRoot();
        double N = root.getFreq();
        counts = deepFirstSearch(root, counts);
        zeroCount = counts[0] / N;
        return zeroCount;
    }

    private double[] deepFirstSearch(GramTreeNode node, double[] counts) {
        if (node.getChildren().size() == 0) {
            if (node.getFreq() <= K)
                counts[(int) (node.getFreq() - 1)]++;
            return counts;
        }
        for (GramTreeNode child : node.getChildren().values()) {
            counts = deepFirstSearch(child, counts);
        }
        return counts;
    }
}
