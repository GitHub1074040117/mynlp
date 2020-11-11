package study.language.chinese.word;

import java.util.Date;

// 词语类，保存词的属性以及出现过的次数
public class Word {
    String value; // 词语的值
    private double memoryValue; // 遗忘值
    int frequency; // 记录每个词语出现的频率
    private double s; // Short-term Memory Factor
    private double m; // Memory Drop-Down Degree
    private Date updatedAt; // 词汇更新时间
    private static final double SF0 = 0.06; // 短期记忆因子初始值
    private static final double MD0 = 0.56; // 记忆下降因子初始值
    public static final int WORD_LENGTH_5 = 5;
    public static final int WORD_LENGTH_4 = 4;
    public static final int WORD_LENGTH_3 = 3;
    public static final int WORD_LENGTH_2 = 2;
    public static final int WORD_LENGTH_1 = 1;

    public Word(String value) {
        this.value = value;
        this.m = MD0;
        this.s = SF0;
        this.updatedAt = new Date();
        this.frequency = 1;
    }

    public Word(Word word) {
        this.memoryValue = word.memoryValue;
        this.value = word.value;
        this.frequency = word.frequency;
        this.m = MD0;
        this.s = SF0;
        this.updatedAt = word.updatedAt;
    }

    public Word(String value, int frequency, double s, double m, Date updatedAt) {
        this.value = value;
        this.frequency = frequency;
        this.s = s;
        this.m = m;
        this.updatedAt = updatedAt;
    }

    // 出现一次频率加一
    void occur() {
        ++frequency;
    }

    // 用一个新词替换当前词
    void replace(Word newWord) {
        this.frequency = newWord.frequency;
        this.value = newWord.value;
    }

    // 获取词内容
    public String getValue() {
        return value;
    }

    // 获取词频率
    public int getFreq() {
        return frequency;
    }

    // 获取词sf
    public double getM() {
        return m;
    }

    public double getS() {
        return s;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setM(double m) {
        this.m = m;
    }

    public void setFreq(int frequency) {
        this.frequency = frequency;
    }

    public void addFreq(int frequency) {
        this.frequency += frequency;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setS(double s) {
        this.s = s;
    }

    public void updatedAt(Date time) {
        updatedAt = time;
    }

    public void setMemoryValue(double memoryValue) {
        this.memoryValue = memoryValue;
    }

    public double getMemoryValue() {
        return memoryValue;
    }

    // 判断两个词汇是否相似
    public static boolean isSimilar(Word word1, Word word2) {
        if (word1.getValue().length() == 0 || word2.getValue().length() == 0) return false;
        String v1, v2; // v1取较短的词， v2取较长的词
        int v1Len, v2Len;
        if (word1.value.length() > word2.value.length()) {
            v1 = word2.value;
            v2 = word1.value;
        } else {
            v1 = word1.value;
            v2 = word2.value;
        }
        v1Len = v1.length();
        v2Len = v2.length();
        // 判断v1是否为v2的子字符串
        for (int i = 0; i < v2Len; i++) {
            if (i + v1Len > v2Len) break;
            if (v1.equals(v2.substring(i, i + v1Len))) {
                return true;
            }
        }
        return false;
    }

}
