package mynlp.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpaceUtility {

    // 仅仅通过空格区分，来将文本内容转化为词数组
    public static ArrayList<String> extractWordsSimplyBySpace(ArrayList<String> content) {
        ArrayList<String> words = new ArrayList<>();
        for (String sentence : content) {
            words.addAll(extractWordsSimplyBySpace(sentence));
        }
        return words;
    }

    // 对一句话进行分词，仅仅按照非中文作为分隔符
    private static ArrayList<String> extractWordsSimplyBySpace(String sentence) {
        ArrayList<String> words = new ArrayList<>();
        String regx = "[\u4e00-\u9fa5]+";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(sentence);
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }

    // 从文本将空格等奇怪符号去除
    public static ArrayList<String> removeSpace(ArrayList<String> content) {
        ArrayList<String> result = new ArrayList<>();
        for (String sentence : content) {
            String str = removeSpace(sentence);
            if (str.length() > 0) result.add(str);
        }
        return result;
    }

    private static String removeSpace(String sentence) {
        String regx = "[\u4e00-\u9fa5‘’“”。·，！？\n]+";
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(sentence);
        while (matcher.find()) {
            result.append(matcher.group());
        }
        return result.toString();
    }
}
