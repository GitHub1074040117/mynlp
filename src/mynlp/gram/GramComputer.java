package mynlp.gram;

import mynlp.helper.ArrayListHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 协助语法树完成一些随机计算和其他功能
 * */

class GramComputer {
    private int degree;
    private GramTreeNode root;

    GramComputer(GramTree gramTree) {
        this.degree = gramTree.getDegree();
        this.root = gramTree.getRoot();
    }

    // 轮盘赌选择法
    static String rouletteWheelSelection(HashMap<String, Integer> hashMap) {
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

        return result;
    }


    // 计算最大似然估计值（Maximum Likelihood Estimate），以degree=3为例，输入三元组<w1,w2,w3>，计算P(w3|w1w2)，即MLE，
    // 计算方法 P(w3|w1w2) = C(w1w2w3)/C(w1w2)
    double computeMLE(ArrayList<String> tuple) {
        if (exceeded(tuple.size())) return 0;

        // 计算C(w1w2)
        int denominator = count(ArrayListHelper.subArrayList(tuple, 0, tuple.size() - 1));
        // 计算C(w1w2w3)
        int numerator = count(tuple);

        return numerator*1.0 / denominator;
    }

    // 计算C(w1w2w3...wn)其中n不能超过N元组中的N
    private int count(ArrayList<String> tuple) {
        if (tuple.size() == 0) return root.getFreq();
        if (exceeded(tuple.size())) return root.getFreq();
        GramTreeNode node = root;
        for (String key : tuple) {
            if (node.containKey(key)) {
                node = node.get(key);
            } else {
                new Exception("Word not found : " + key).printStackTrace();
                return root.getFreq();
            }
        }
        return node.getFreq();
    }

    // 检查元组的长度是否超过degree
    private boolean exceeded(int tupleSize) {
        if (tupleSize > degree) {
            new Exception("元组数超出限制！found: "+ tupleSize + " require: <=" + degree).printStackTrace();
            return true;
        }
        if (tupleSize == 0) {
            new Exception("元组数为空！found: "+ tupleSize + " require: > 0").printStackTrace();
            return true;
        }
        return false;
    }
}
