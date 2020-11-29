package mynlp.markov;

import mynlp.utils.WordUtility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class TwoDimMatrix extends HashMap<String, HashMap<String, Double>> {

    // key存在则将原值加上value，不存在则添加
    void add(String i, String j, double value) {
        if (containsKey(i) && get(i).containsKey(j)) {
            get(i).replace(j, get(i, j) + value);
        } else {
            put(i, j, value);
        }
    }

    // key1行，key2列
    void put(String i, String j, double v) {
        if (!containsKey(i)) {
            put(i, new HashMap<>());
        }
        get(i).put(j, v);
    }

    // 获取不到返回0
    double get(String i, String j) {
        if (!containsKey(i) || !get(i).containsKey(j)) return 0.0;
        return get(i).get(j);
    }

    void show(String[] cols) {
        String[] rows = keySet().toArray(new String[0]);
        show(cols, rows);
    }

    void show() {
        String[] rows = keySet().toArray(new String[0]);
        // 列取并集
        String[] cols;
        Set<String> set = new HashSet<>();
        for (String row : keySet()) {
            set.addAll(get(row).keySet());
        }
        cols = set.toArray(new String[0]);

        show(cols, rows);
    }

    private void show(String[] cols, String[] rows) {
        boolean isChinese = !WordUtility.isNotChinese(cols[1]);
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
                    System.out.printf("〇%-6.2f", get(row, col));
                }
            } else {
                for (String col : cols) {
                    System.out.printf("%-7.2f", get(row, col));
                }
            }

            System.out.println();
        }
    }


}
