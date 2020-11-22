package mynlp.markov;

import java.util.HashMap;
import java.util.Random;

class Helper {

    // 生成num个随机数
    static double[] randoms(int len) {
        double[] result = new double[len];
        double sum = 0;
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int r = random.nextInt(100);
            result[i] = r;
            sum += r;
        }
        for (int i = 0; i < len; i++) {
            result[i] = result[i] / sum;
        }
        return result;
    }

    // 计算不同观察的种类数
    static int countType(String[] strings) {
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
    static String[] countUnique(String[] strings) {
        int type = countType(strings);
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
