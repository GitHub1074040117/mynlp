package mynlp.gram;

import mynlp.helper.ArrayListHelper;
import mynlp.helper.ContentHelper;
import mynlp.helper.FileHelper;
import mynlp.helper.SentenceHelper;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;

/**
 * 三元语法模型
 * */
public class GramModel {
    public static String TAIL = "#";        // 句子结尾标记
    public static String HEAD = "^";        // 句子开头标记
    private int degree;                     // 语法模型的元数
    private GramTree gramTree;              // 语法树，用来统计词频
    private Tokenizer tokenizer;            // 分词器
    private static final int MAX_TRY = 50;  // 造句的最大尝试次数

    public GramModel(int degree) {
        if (degree < 2) {
            new Exception("degree must >= 2! Found: " + degree).printStackTrace();
            return;
        }
        this.degree = degree;
        this.gramTree = new GramTree(degree);
        this.tokenizer = new Tokenizer();
    }

    // 读取文本进行训练，创建语法树
    public void training(String filePath) {
        ArrayList<String> content = FileHelper.readFile(filePath);
        // 将文本中的句子进行区分
        content = ContentHelper.specifySentenceFromContent(content);
        // 将每个句子规范化，添加前缀和后缀
        content = ContentHelper.regularSentenceFromContent(content, degree);
        // 对句子进行分词
        for (String sentence : content) {
            ArrayList<String> tokenizedSentence = tokenizer.tokenizeSentence(sentence);
            gramTree.build(tokenizedSentence);
        }
        // 打印训练结果
//        gramTree.show();
    }

    // 预测下一个词语
    private String predictNext(String prefix) throws Exception {
        int minLen = degree - 1;
        ArrayList<String> tokenizedSentence = tokenizer.tokenizeSentence(prefix);
        int size = tokenizedSentence.size();
        if (size < minLen) {
            throw new Exception("分词后的词组长度小于要求的最小长度, get: "
                    + tokenizedSentence.size() + "  require: " + minLen);
        }
        ArrayList<String> tuple = ArrayListHelper.subArrayList(tokenizedSentence, size - minLen, size);
        String word = gramTree.predict(tuple);
        return prefix + word;
    }

    // 随机生成一句话
    public String randomSentence() {
        String result = SentenceHelper.getSentenceHeadByDegree(degree);
        for (int i = 0; i < MAX_TRY; i++ ) {
            try {
                result = predictNext(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            if (result.substring(result.length() - 1).equals(TAIL)) break;
        }
        return result.substring(degree - 1, result.length() - 1);
    }

    // 根据前缀生成一句话
    public String randomSentence(String prefix) {
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

    /*getter*/

    Tokenizer getTokenizer() {
        return tokenizer;
    }

    int getDegree() {
        return degree;
    }

    GramTree getGramTree() {
        return gramTree;
    }
}
