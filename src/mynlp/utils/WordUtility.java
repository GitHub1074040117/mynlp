package mynlp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordUtility {
    private static final String[] STOPWORD = {"儿","这","们","吧","呀","哟","哈","啊","啦","是","有","地","着","来","它","她","了","就","我","要","的","他","你"};

    // 判断一个词是否无其他非中文字符
    public static boolean isNotChinese(String word) {
        String regx = "[\u4e00-\u9fa5]+";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(word);
        return !matcher.matches();
    }

    // 判断一个词是否是停用词
    public static boolean isStopWord(String word) {
        for (String stopword : STOPWORD) {
            if (stopword.equals(word)) return true;
        }
        return false;
    }

    // 判断一个词是否是另一个词的子字符串
    static boolean isSubMatch(String wordA, String wordB) {
        String longWord = wordA.length() > wordB.length() ? wordA : wordB;
        String shortWord = wordA.length() > wordB.length() ? wordB : wordA;
        Pattern pattern = Pattern.compile(shortWord);
        Matcher matcher = pattern.matcher(longWord);
        return matcher.find();
    }
}
