package mynlp.markov;

import mynlp.helper.WordHelper;

import java.util.HashMap;

class TwoDimMatrix extends HashMap<String, HashMap<String, Double>> {

    // key1行，key2列
    void put(String i, String j, double v) {
        if (!containsKey(i)) {
            put(i, new HashMap<>());
        }
        get(i).put(j, v);
    }

    // 获取不到返回0
    double get(String i, String j) {
        if (!containsKey(i)) return 0.1;
        if (!get(i).containsKey(j)) return 0.2;
        return get(i).get(j);
    }

    void show() {
        String[] rows = keySet().toArray(new String[0]);
        String[] cols = get(rows[0]).keySet().toArray(new String[0]);
        boolean isChinese = !WordHelper.isNotChinese(cols[1]);
        System.out.println();
        System.out.printf("%10s", cols[0]);
        for (int i = 1; i < cols.length; i++) {
            System.out.printf("%7s", cols[i]);
        }
        System.out.println();
        for (String row : rows) {
            System.out.printf("%-6s",row);
            if (isChinese) {
                for (String col : cols) {
                    System.out.printf("〇%-6.2f", get(row).get(col));
                }
            } else {
                for (String col : cols) {
                    System.out.printf("%-7.2f", get(row).get(col));
                }
            }

            System.out.println();
        }
    }
}
