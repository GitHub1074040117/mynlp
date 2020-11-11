package process.memory;

import process.database.DB;
import process.utils.Utils;
import study.language.chinese.word.Word;
import study.language.chinese.word.WordTable;

import java.util.Date;

public class Memory {
    private static final int MEMORY_LIMITED_LENGTH = 100000; // 记忆的词汇表长度上限
    public static final int MAXLENGTH_PER_REMEMBER = 2000; // 每次记忆的长度
    private DB db;

    public Memory() {
        db = new DB();
    }

    // 记忆词汇表
    public synchronized int remember(WordTable wordTable) {
        if (wordTable == null || wordTable.getTableLength() == 0) {
            new Exception("获取到的词汇表为空！").printStackTrace();
            return -1;
        }
        System.out.println("开始记忆...");

        int sumF = wordTable.getFrequencySum(); // 获取词汇频度的总和
        int n = wordTable.getTableLength(); // 获取词汇种类数
        int OK = -1; // 判断数据库操作是否已经完成

        for (Word word : wordTable.getWords()) {
            // 查询要记住的词是否在数据库中存在
            Word word1 = db.query(word);
            if (word1 != null) {
                // 更新遗忘因子
                int second = Utils.secondDiff(new Date(), word1.getUpdatedAt());
                word.setM(justify(word1.getM(), second));
                word.setS(justify(word1.getS(), second));
                // 增加出现频率
                word.setFreq(word1.getFreq() + word.getFreq());
                // 更新记忆值
                word.setMemoryValue(wordMemoryValue(word, sumF, n));
                OK = db.update(word);
            } else {
                // 更新记忆值
                word.setMemoryValue(wordMemoryValue(word, sumF, n));
                // 记录一个词到数据库
                OK = db.insert(word);
            }
        }
        if (OK == DB.COMPLETE) {
            // 判断表的长度，如果过长则进行遗忘操作
            String tableName = DB.getTableName(wordTable.getWords().get(0));
            WordTable wordTable1 = db.fetchWordTable(tableName);
            if (wordTable1.getTableLength() > MEMORY_LIMITED_LENGTH) {
                forget(wordTable1);
            }
        } else {
            new Exception("数据库还没有操作完成...").printStackTrace();
        }
        return OK;
    }

    // 从词汇表中遗忘词汇
    private void forget(WordTable wordTable) {
        System.out.println("进行遗忘...");
        double mValue; // 记忆值，值越高说明记忆效果越好
        int sumF = wordTable.getFrequencySum(); // 获取词汇频度的总和
        int n = wordTable.getTableLength(); // 获取词汇种类数
        for (Word word : wordTable.getWords()) {
            mValue = wordMemoryValue(word, sumF, n);
            word.setMemoryValue(mValue);
            if (isForgotten(mValue)) {
                // 进行遗忘从数据库中删除
                db.delete(word);
            }
        }
    }

    /*// 对词的记忆进行增强
    public void intensify(Word word) {
        // 从数据库中查找该词汇
        Word w = DB.query(word);
        assert w != null;
        // 计算相隔天数
        int second = Utils.secondDiff(new Date(), w.getUpdatedAt());
        System.out.println(new Date() + " - " + w.getUpdatedAt() + " = " + second);
        // 因子调节
        w.setM(justify(w.getM(), second));
        w.setS(justify(w.getS(), second));
        // 更新数据库词汇记录
        DB.update(w);
    }*/

    // 根据复习间隔(s)，遗忘因子的调整函数,公式：=1-0.25*EXP(-(ATAN(deltaT/86400-1)^2))
    private static double justify(double factor, int deltaT) {
        if (deltaT == 0)return factor;
        return factor*(1 - 0.25*Math.exp(-Math.pow(Math.atan((double) deltaT / 86400 - 1), 2)));
    }

    // 根据间隔（s），和m, s计算记忆值，公式:=1-m*(deltaT/3600)^s
    private static double memoryValue(double m, double s, int deltaT) {
        return 1 - m * Math.pow(((double) deltaT / 3600), s);
    }

    // 词频调整因子，将词出现的频度引入到记忆值中，公式：mValue=mValue+β(1-mValue),其中β=(fi - avg(fi))/sum(fi)
    // β=(n*fi - sumF)/(n*sumF)
    private static double freqFactor(int fi, int sumF, int n) {
        return (double) (n * fi - sumF) / (n * sumF);
    }

    // 计算一个词的记忆值, sumF为总频度和, n为词的种类数
    private double wordMemoryValue(Word word, int sumF, int n) {
        double m = word.getM();
        double s = word.getS();
        int deltaT = Utils.secondDiff(new Date(), word.getUpdatedAt());
        double mValue = memoryValue(m, s, deltaT);
        return mValue + freqFactor(word.getFreq(), sumF, n) * (1 - mValue);
    }

    // 根据遗忘值，根据随机数决定是否遗忘
    private static boolean isForgotten(double memoryValue) {
        return Math.random() > memoryValue;
    }

}
