package mynlp.markov;

import java.util.HashMap;

class OneDimMatrix extends HashMap<String, Double> {

    // key存在则将原值加上value，不存在则添加
    void add(String key, double value) {
        if (containsKey(key)) {
            replace(key, get(key) + value);
        } else {
            put(key, value);
        }
    }

    double get(String key) {
        if (!containsKey(key)) return 0;
        return super.get(key);
    }

    void show() {
        String[] cols = keySet().toArray(new String[0]);
        System.out.println();
        for (String col : cols) {
            System.out.printf("%-7s", col);
        }
        System.out.println();
        for (double v : values()) {
            System.out.printf("%-6.2f", v);
        }
        System.out.println();
    }
}
