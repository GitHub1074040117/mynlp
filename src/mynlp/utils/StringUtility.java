package mynlp.utils;

import java.util.HashMap;

public class StringUtility {

    public static String[] toStringArray(String str) {
        String[] result = new String[str.length()];
        for (int i = 0; i < str.length(); i++) {
            result[i] = str.substring(i, i + 1);
        }
        return result;
    }

    public static String toString(String[] strings) {
        StringBuilder result = new StringBuilder();
        for (String s : strings) {
            result.append(s);
        }
        return result.toString();
    }

    // 计算不同观察的种类数
    public static int countUniqueString(String[] strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        int count = 0;
        for (String s : strings) {
            if (!hashMap.containsKey(s)) {
                count++;
                hashMap.put(s, 1);
            } else {
                hashMap.replace(s, hashMap.get(s) + 1);
            }
        }
        return count;
    }

    // 将观察序列去重
    public static String[] getUniqueStrings(String[] strings) {
        int type = countUniqueString(strings);
        String[] unique = new String[type];
        int index = 0;
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String s : strings) {
            if (!hashMap.containsKey(s)) {
                unique[index++] = s;
                hashMap.put(s, 1);
            }
        }
        return unique;
    }
}
