package mynlp.markov;

import java.util.HashMap;

class OneDimMatrix extends HashMap<String, Double> {
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
