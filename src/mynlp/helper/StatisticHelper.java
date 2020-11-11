package mynlp.helper;


import java.util.ArrayList;
import java.util.HashMap;

public class StatisticHelper {
    private HashMap<String, Integer> unigramCount; // 一元词语散列表

    // 输入分好词的词组
    public StatisticHelper(ArrayList<String> words) {
        unigramCount = new HashMap<>();
        countUnigram(words);
    }

    // 统计一元语法计数
    private void countUnigram(ArrayList<String> words) {
        for (String word : words) {
            if (unigramCount.containsKey(word)) {
                unigramCount.replace(word, unigramCount.get(word) + 1);
                continue;
            }
            unigramCount.put(word, 1);
        }
    }

    public HashMap<String, Integer> getUnigramCount() {
        return unigramCount;
    }
}
