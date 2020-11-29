package mynlp.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentUtility {

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
}
