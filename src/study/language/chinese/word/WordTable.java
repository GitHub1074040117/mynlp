package study.language.chinese.word;

import java.util.ArrayList;

public class WordTable {
    private ArrayList<Word> words; // 存放词语
    private int tableLength; // 当前获取到不同词语的个数

    public WordTable() {
        this.words = new ArrayList<>();
        this.tableLength = 0;
    }

    public WordTable(ArrayList<Word> words) {
        this.words = words;
        this.tableLength = words.size();
    }

    // 获取词汇数组
    public ArrayList<Word> getWords() {
        return words;
    }

    // 插入一个词
    public void insert(String word) {
        int index = getIndex(word);
        // 词不存在
        if (index == 0) {
            words.add(new Word(word));
            tableLength++;
        } else { // 词存在
            words.get(index).occur(); // 出现次数加一
        }
    }

    public int getTableLength() {
        return tableLength;
    }

    // 打印输出
    public void show() {
        System.out.println();
        for (int i = 0; i < tableLength; i++) {
            System.out.printf("%s%-8d", words.get(i).value, words.get(i).frequency);
            if ((i + 1)%20 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // 获取词汇表中频度的总和
    public int getFrequencySum() {
        int sumF = 0;
        for (Word word : words) {
            sumF += word.frequency;
        }
        return sumF;
    }

    public double avgFreq() {
        return (double) getFrequencySum() / tableLength;
    }

    // 精简词汇表, 将出现频率过低的词汇删除
    public void refine() {
        removeLowFreqWord();
    }

    // 删除低频词汇, 低于平均频度的词汇将被删除
    private void removeLowFreqWord() {
        double avgF = avgFreq();
        Word word;
        for (int i = 0; i < tableLength; i++) {
            word = words.get(i);
            if (word.getFreq() < avgF) {
                words.remove(word);
                tableLength--;
                i--;
            }
        }
    }

    // 获取部分长度词汇表, 包括起始索引，不包括终止索引
    public WordTable subTable(int beginIndex, int endIndex) {
        if (endIndex > tableLength) {
            endIndex = tableLength;
        }
        ArrayList<Word> newWords = new ArrayList<>(words.subList(beginIndex, endIndex));
        return new WordTable(newWords);
    }

    /**----------------------------------------------------------**/

    // 获取词在wordTable中的下标
    private int getIndex(String word) {
        for (int i = 0; i <= this.tableLength; i++) {
            if (word.equals(words.get(i).value)) {
                return i;
            }
        }
        return 0;
    }

    // 在wordTable中添加一个词
    public void add(Word word) {
        // 词不存在
        if (!isExisted(word)) {
            words.add(word);
            tableLength++;
        } else {
            // 若存在则出现频度加一
            words.get(getIndex(word.getValue())).occur();
        }
    }

    // 判断一个词在词汇表中是否存在
    private boolean isExisted(Word word) {
        for (int i = 0; i < this.tableLength; i++) {
            if (word.getValue().equals(words.get(i).value)) {
                return true;
            }
        }
        return false;
    }

    // 融合一个子词汇表
    public void merge(WordTable subWordTable) {
        for (int i = 0; i < subWordTable.getTableLength(); i++) {
            Word word = subWordTable.words.get(i);
            if (isExisted(word)){
                // 若存在则频度相加
                words.get(words.indexOf(word)).addFreq(word.getFreq());
                continue;
            }
            this.add(subWordTable.words.get(i));
        }
    }

    // 根据词的频度排序
    public void sort() {
        words.sort((arg0, arg1) -> {
            if (arg0.getFreq() == arg1.getFreq()) return 0;
            return arg0.getFreq() > arg1.getFreq()? -1 : 1; //这是顺序
        });
    }

    // 从词汇表中删除某个词汇
    public void remove(Word word) {
        int index = getIndex(word.getValue());
        words.remove(index);
        tableLength--;
    }

    // 从词汇表中删除某个长度的词
    public void removeLengthOf(int wordLength) {
        for (int i = 0; i < tableLength; i++) {
            if (words.get(i).getValue().length() == wordLength) {
                remove(words.get(i));
                i--;
            }
        }
    }
}
