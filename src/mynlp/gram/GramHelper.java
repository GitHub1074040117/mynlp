package mynlp.gram;

import java.util.ArrayList;

class GramHelper {

    // 为内容中的句子添加head和tail
    static ArrayList<String> regularSentenceFromContent(ArrayList<String> content, int degree) {
        ArrayList<String> result = new ArrayList<>();
        String head;
        String tail = GramModel.TAIL;
        head = GramHelper.getSentenceHeadByDegree(degree);
        for (String sentence : content) {
            result.add(head + sentence + tail);
        }
        return result;
    }

    // 获取句子的头部
    static String getSentenceHeadByDegree(int degree) {
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
