package mynlp.helper;

import mynlp.gram.GramModel;

public class SentenceHelper {

    // 为句子添加head和tail
    public static String regularSentence(String sentence, int degree) {
        String head;
        String tail = GramModel.TAIL;
        head = getSentenceHeadByDegree(degree);
        return head + sentence + tail;
    }

    // 获取句子的头部
    public static String getSentenceHeadByDegree(int degree) {
        String h = GramModel.HEAD;
        switch (degree) {
            case 1:
            case 2:
                return  h;
            case 3:
                return  h+h;
            case 4:
                return  h+h+h;
            case 5:
                return  h+h+h+h;
            case 6:
                return  h+h+h+h+h;
            case 7:
                return  h+h+h+h+h+h;
            case 8:
                return  h+h+h+h+h+h+h;
            default:
                new Exception("Degree超出限制！").printStackTrace();
                return h+h+h+h;
        }
    }
}
