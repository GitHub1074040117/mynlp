package mynlp.helper;

import mynlp.gram.GramModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentHelper {

    // 将内容中的句子区分开来
    public static ArrayList<String> specifySentenceFromContent(ArrayList<String> content) {
        ArrayList<String> result = new ArrayList<>();
        String regx = "[\u4e00-\u9fa5].+?[。\n？！]";
        Pattern pattern = Pattern.compile(regx);
        for (String sentence : content) {
            Matcher matcher = pattern.matcher(sentence);
            while (matcher.find()) {
                result.add(matcher.group());
            }
        }
        if (result.size() == 0) new Exception("Result is empty!").printStackTrace();
        return result;
    }

    // 为内容中的句子添加head和tail
    public static ArrayList<String> regularSentenceFromContent(ArrayList<String> content, int degree) {
        ArrayList<String> result = new ArrayList<>();
        String head;
        String tail = GramModel.TAIL;
        head = SentenceHelper.getSentenceHeadByDegree(degree);
        for (String sentence : content) {
            result.add(head + sentence + tail);
        }
        return result;
    }



    public static void showContent(ArrayList<String> content) {
        for (String sentence : content) {
            System.out.println(sentence);
        }
    }
}
