package mynlp.learner;

import mynlp.helper.ArrayListHelper;
import mynlp.helper.ContentHelper;
import mynlp.helper.FileHelper;
import mynlp.storage.Storage;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;

public class Learner {
    private static final int DEGREE = 4; // 语法深度
    private LearningComputer computer; // 语法计算器
    private Tokenizer tokenizer;

    public Learner() {
        tokenizer = new Tokenizer();
    }

    // 创建学习树
    private LearningTree createTree(String filePath) {
        LearningTree tree = new LearningTree(DEGREE);
        ArrayList<String> content = FileHelper.readFile(filePath);
        // 将文本中的句子进行区分
        content = ContentHelper.specifySentenceFromContent(content);
        // 将每个句子规范化，添加前缀和后缀
        content = ContentHelper.regularSentenceFromContent(content, DEGREE);
        // 对句子进行分词
        for (String sentence : content) {
            ArrayList<String> tokenizedSentence = tokenizer.tokenize(sentence);
            tree.build(tokenizedSentence);
        }
        return tree;
    }

    // 二次学习法学习单词
    public ArrayList<String> learns(String filePath) {
        ArrayList<String> words = new ArrayList<>(), words1, words2;

        // 第一次学习
        words1 = learning(filePath);
        //System.out.println("1"+words1);
        ArrayListHelper.removeSubStrings(words1);
        Storage.storeNewWords(words1); // 写入生词

        // 第二次学习
        words2 = learning(filePath);
        //System.out.println("2"+words2);
        ArrayListHelper.removeSubStrings(words2);
        Storage.storeNewWords(words); // 写入生词

        words.addAll(words1);
        words.addAll(words2);
        return words;
    }

    // 学习生词
    private synchronized ArrayList<String> learning(String filePath) {
        ArrayList<String> vocabulary = new ArrayList<>();
        LearningTree tree = createTree(filePath);
        // 优化树
        tree.optimizeRootChildren();
        //GramTester.showTree(tree);
        computer = new LearningComputer();
        for (LearningTreeNode child : tree.getRoot().getChildren().values()) {
            ArrayList<String> unknowns = deepFirstSearch(child);
            if (unknowns.get(0).equals(child.getKey())) {
                continue;
            }
            vocabulary.addAll(unknowns);
        }
        return vocabulary;
    }

    // 递归搜索
    private ArrayList<String> deepFirstSearch(LearningTreeNode node) {
        ArrayList<String> keys = new ArrayList<>();
        if (node.getChildren().size() == 0) {
            keys.add(node.getKey());
            return keys;
        }
        for (LearningTreeNode child : node.getChildren().values()) {
            if (computer.stickLikelihood(node, child)) {
                //System.out.printf("%s%d--%s%d\n", node.getKey(), node.getFreq(), child.getKey(), child.getFreq());
                for (String childKey : deepFirstSearch(child)) {
                    keys.add(node.getKey()+childKey);
                }
            }
        }
        if (keys.size() == 0) keys.add(node.getKey());
        return keys;
    }
}
