package mynlp.gram;

import mynlp.helper.ArrayListHelper;
import mynlp.helper.WordHelper;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.Stack;

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
    private double probability(String sentence) {
        double result = 1;
        GramComputer gramComputer = new GramComputer(gramTree);
        ArrayList<String> tuple = tokenizer.tokenize(sentence);
        for (int i = 1; i <= tuple.size(); i++) {
            result *= gramComputer.getMLE(ArrayListHelper.subArrayList(tuple, i - degree, i));
        }
        return result;
    }

    // 打印语法树
    public void showTree() {
        show(gramTree);

    }
    public static void showTree(GramTree gramTree) {
        show(gramTree);

    }
    private static void show(GramTree gramTree) {
        Stack<GramTreeNode> stackA = new Stack<>();
        Stack<GramTreeNode> stackB = new Stack<>();
        GramTreeNode root = gramTree.getRoot();
        GramTreeNode node = gramTree.getRoot();
        stackA.push(node);

        while (!stackA.empty()) {
            node = stackA.pop();
            stackB.push(node);
            if (node.getChildren().size() != 0) {
                for (String key : node.getChildren().keySet()) {
                    stackA.push(node.getChild(key));
                }
            } else {
                // 打印
                System.out.printf("root%d", root.getFreq());
                for (int i = 0; i < stackB.size() - 1; i++) {
                    GramTreeNode parent = stackB.get(i);
                    GramTreeNode child = stackB.get(i+1);
                    System.out.printf("%8s%d",parent.getChildKey(child), child.getFreq());
                }
                System.out.println();

                String key;
                do {
                    stackB.pop();
                    node = stackB.peek();
                    if (stackA.empty()) return;
                    key = node.getChildKey(stackA.peek());
                } while (key.length() == 0 && !stackB.empty());

            }
        }
    }

}
