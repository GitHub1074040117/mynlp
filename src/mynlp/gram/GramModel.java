package mynlp.gram;

import mynlp.utils.ArrayListUtility;
import mynlp.utils.ContentUtility;
import mynlp.utils.FileUtility;
import mynlp.dict.Dictionary;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 三元语法模型
 * */
class GramModel {
    static String TAIL = "#";        // 句子结尾标记
    static String HEAD = "^";        // 句子开头标记
    private int degree;                     // 语法模型的元数
    private GramTree gramTree;              // 语法树，用来统计词频
    private Dictionary dictionary;          // 词典分词器
    private static final int MAX_TRY = 50;  // 造句的最大尝试次数

    GramModel(int degree) {
        if (degree < 2) {
            new Exception("degree must >= 2! Found: " + degree).printStackTrace();
            return;
        }
        this.degree = degree;
        this.gramTree = new GramTree(degree);
        this.dictionary = new Dictionary();
    }

    // 读取文本进行训练，创建语法树
    void training(String filePath) {
        ArrayList<String> content = FileUtility.readFile(filePath);
        // 将文本中的句子进行区分
        content = ContentUtility.specifySentenceFromContent(content);
        // 将每个句子规范化，添加前缀和后缀
        content = GramHelper.regularSentenceFromContent(content, degree);
        // 对句子进行分词
        for (String sentence : content) {
            gramTree.build(ArrayListUtility.toArrayList(sentence));
        }
    }

    // 给定前后两个词，获取后跟概率
    double getAfterProb(String preWord, String word) {
        return gramTree.getAfterProb(preWord, word);
    }

    // 预测下一个词语
    private String predictNext(String prefix) {
        int lookAhead = degree - 1; // 向前看的词数
        ArrayList<String> tokenizedSentence = dictionary.tokenize(prefix);
        int size = tokenizedSentence.size();
        ArrayList<String> tuple = ArrayListUtility.subArrayList(tokenizedSentence, size - lookAhead);
        String word = gramTree.predict(tuple);
        return prefix + word;
    }

    // 随机生成一句话
    String randomSentence() {
        String result = GramHelper.getSentenceHeadByDegree(degree);
        for (int i = 0; i < MAX_TRY; i++ ) {
            result = predictNext(result);
            if (result.substring(result.length() - 1).equals(TAIL)) break;
        }
        return result.substring(degree - 1, result.length() - 1);
    }

    // 根据前缀生成一句话
    String randomSentence(String prefix) {
        String result = prefix;
        for (int i = 0; i < MAX_TRY; i++ ) {
            try {
                result = predictNext(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            if (result.toCharArray()[result.length() - 1] == '#') break;
        }
        return result.substring(0, result.length() - 1);
    }

    void show() {
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
                System.out.printf("root%.2f", root.getFreq());
                for (int i = 0; i < stackB.size() - 1; i++) {
                    GramTreeNode parent = stackB.get(i);
                    GramTreeNode child = stackB.get(i+1);
                    System.out.printf("%8s%.2f",parent.getChildKey(child), child.getFreq());
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
