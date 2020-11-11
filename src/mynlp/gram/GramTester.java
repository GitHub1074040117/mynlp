package mynlp.gram;

import mynlp.helper.ArrayListHelper;
import mynlp.helper.SentenceHelper;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 测试N元语法模型的困惑度，元组概率度
 * */

public class GramTester {
    private GramTree gramTree;
    private int degree;
    private Tokenizer tokenizer;

    public GramTester(GramModel gramModel) {
        this.gramTree = gramModel.getGramTree();
        this.degree = gramModel.getDegree();
        this.tokenizer = gramModel.getTokenizer();
    }

    // 输入句子，获取困惑度
    public double perplexity(String sentence) {
        return Math.pow(probability(sentence), -1.0/degree);
    }

    // 输入句子，返回概率
    public double probability(String sentence) {
        double result = 1;
        GramComputer gramComputer = new GramComputer(gramTree);
        ArrayList<String> tuple = tokenizer.tokenizeSentence(sentence);
        for (int i = 1; i <= tuple.size(); i++) {
            result *= gramComputer.computeMLE(ArrayListHelper.subArrayList(tuple, i - degree, i));
        }
        return result;
    }

    // 打印语法树
    public void printGramTree() {
        GramTreeNode root = gramTree.getRoot();
        if (degree != 3) {
            new Exception("当前打印类型只支持三元语法树").printStackTrace();
            return;
        }
        System.out.println("root-" + root.getFreq());
        HashMap<String, GramTreeNode> children = root.getChildren();
        for (String key : children.keySet()) {
            System.out.print(key + "-" + children.get(key).getFreq() + "==>");
            HashMap<String, GramTreeNode> children1 = children.get(key).getChildren();
            for (String key1 : children1.keySet()) {
                System.out.print(key1 + "-" + children1.get(key1).getFreq() + "==>");
                HashMap<String, GramTreeNode> children2 = children1.get(key1).getChildren();
                for (String key2 : children2.keySet()) {
                    System.out.print(key2 + "-" + children2.get(key2).getFreq());
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println(root.getChildren().keySet());
    }
}
