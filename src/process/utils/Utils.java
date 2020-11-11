package process.utils;

import study.language.chinese.word.WordCoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    // 计算秒数差
    public static int secondDiff(Date fDate, Date oDate) {
        int between = (int) (fDate.getTime() - oDate.getTime());
        return  Math.abs(between / 1000);
    }

    // 获取yyyy-mm-dd HH:mm:ss的日期格式
    public static String toSqlDate(Date dt) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH表示24小时制；
        return dFormat.format(dt);
    }

    // 判断该字是否为特殊字符
    public static boolean isRegular(char ch) {
        String str = WordCoder.codeGB2312(ch);
        String min = WordCoder.codeGB2312('啊');
        String max = WordCoder.codeGB2312('齄');
        return str.compareTo(min) >= 0 && str.compareTo(max) <= 0;
    }

    // 判断字符串是否不包含特殊字符
    public static boolean isRegular(String word) {
        char[] chars = word.toCharArray();
        for (char aChar : chars) {
            if (!isRegular(aChar)) return false;
        }
        return true;
    }

    // 将一个长字符串删去与较短字符串相同的部分，返回删除后的长字符串
    public static String stringCut(String str1, String str2) {
        if (str1.length() == 0) return str2;
        if (str2.length() == 0) return str1;
        String strLong, strShort;
        if (str1.length() > str2.length()) {
            strLong = str1;
            strShort = str2;
        } else {
            strLong = str2;
            strShort = str1;
        }
        // 判断v1是否为v2的子字符串
        for (int i = 0; i < strLong.length(); i++) {
            if (i + strShort.length() > strLong.length()) break;
            // 是则将相同部分从长字符串中删去
            if (strShort.equals(strLong.substring(i, i + strShort.length()))) {
                strLong = strLong.substring(0, i) + strLong.substring(i + strShort.length());
                i--;
            }
        }
        return strLong;
    }
}
