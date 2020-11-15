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
    public static void removeSubStrings(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String strA = arrayList.get(i);
            for (int j = i + 1; j < arrayList.size(); j++) {
                String strB = arrayList.get(j);
                if (WordHelper.isSubMatch(strA, strB)) {
                    if (strA.length() < strB.length()) {
                        arrayList.remove(strA);
                        i--;
                        i = Math.max(0, i);
                        break;
                    } else {
                        arrayList.remove(strB);
                        i--;
                        i = Math.max(0, i);
                    }
                }
            }
        }
    }

    // 融合两个ArrayList，并自动去重
    public static ArrayList<String> mergeDuplicateRemoval(ArrayList<String> listA, ArrayList<String> listB) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> tempA = new ArrayList<>(listA);
        ArrayList<String> tempB = new ArrayList<>(listB);
        for (String s : tempA) {
            for (int j = 0; j < tempB.size(); j++) {
                if (s.equals(tempB.get(j))) {
                    tempB.remove(j);
                    j--;
                }
            }
        }
        result.addAll(tempA);
        result.addAll(tempB);
        return result;
    }
}
