package mynlp.helper;

import java.util.ArrayList;

public class ArrayListHelper {
    // 获取子串
    public static ArrayList<String> subArrayList(ArrayList<String> arrayList, int beginIndex, int endIndex) {
        ArrayList<String> result = new ArrayList<>();
        if (beginIndex < 0) beginIndex = 0;
        if (endIndex > arrayList.size()) endIndex = arrayList.size();
        for (int i = beginIndex; i < endIndex; i++) {
            result.add(arrayList.get(i));
        }
        return result;
    }

    // 获取子串
    public static ArrayList<String> subArrayList(ArrayList<String> arrayList, int beginIndex) {
        ArrayList<String> result = new ArrayList<>();
        if (beginIndex < 0) beginIndex = 0;
        for (int i = beginIndex; i < arrayList.size(); i++) {
            result.add(arrayList.get(i));
        }
        return result;
    }

    // 转化为字符串
    public static String arrayListToString(ArrayList<String> arrayList) {
        StringBuilder result = new StringBuilder();
        for (String str : arrayList) {
            result.append(str);
        }
        return result.toString();
    }

    // 在数组中去除子串
    public static ArrayList<String> removeSubStrings(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String strA = arrayList.get(i);
            for (int j = i + 1; j < arrayList.size(); j++) {
                String strB = arrayList.get(j);
                if (WordHelper.isSubMatch(strA, strB)) {
                    if (strA.length() < strB.length()) {
                        arrayList.remove(strA);
                        i--;
                        break;
                    } else {
                        arrayList.remove(strB);
                    }
                }
            }
        }
        return arrayList;
    }
}
