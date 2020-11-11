package interact;

import process.database.DB;
import process.utils.Utils;
import study.language.chinese.word.Word;
import study.language.chinese.word.WordTable;

/**
 * 将一个句子中的词语提取出来
 * */
public class DialogParser {
    private DB db;
    private final int MAX_WORD_LENGTH = 4;
    private final int MIN_WORD_LENGTH = 2;

    private WordTable rawTable; // 生词表

    public DialogParser() {
        db = new DB();
        rawTable = new WordTable();
    }


    // 将一个句子的词语按长度4，3，2提取出来，同时将未匹配的句子部分打印出来
    public WordTable getWordTable(String sentence) {
        System.out.println(sentence);
        String subSentence = sentence;
        WordTable wordTable = new WordTable();
        for (int wordLength = MAX_WORD_LENGTH; wordLength >= MIN_WORD_LENGTH; wordLength--) {
            WordTable subWordTable = extractWords(subSentence, wordLength);
            wordTable.merge(subWordTable);
            subSentence = removeWords(subSentence, wordLength);
        }
        System.out.println(subSentence);
        updateRawTable(subSentence);
        rawTable.removeLengthOf(Word.WORD_LENGTH_1);
        refineRawTable();
        return wordTable;
    }

    public WordTable getRawTable() {
        return rawTable;
    }

    // 更新生词表, 将挖去词后的句子统计剩下残段纳入生词表中
    private void updateRawTable(String subSentence) {
        char[] chars = subSentence.toCharArray();
        int beginIndex = 0;
        for (int endIndex = 0; endIndex <= chars.length; endIndex++) {
            if (endIndex < chars.length && Utils.isRegular(chars[endIndex])) continue;
            if (beginIndex >= chars.length || !Utils.isRegular(chars[beginIndex])) {
                beginIndex = endIndex + 1;
                continue;
            }
            String value = subSentence.substring(beginIndex, endIndex);
            rawTable.add(new Word(value));
            beginIndex = endIndex + 1;
        }
        rawTable.sort();
    }

    // 对生词表再次求精
    private void refineRawTable() {
        int tableLength = rawTable.getTableLength();
        for (int i = 0; i < tableLength; i++) {
            /*// 由于是排好序的，就不考虑频度过低的字符串了
            if (rawTable.getWords().get(i).getFreq() < avgFreq) break;*/
            Word wordI = rawTable.getWords().get(i);
            for (int j = i + 1; j < tableLength; j++) {
                Word wordJ = rawTable.getWords().get(j);
                //if (wordI.getValue().length() >= wordJ.getValue().length()) continue;
                // 检测两个字符串的相似度，是则将两个字符串的相同部分提取出来，并将较长字符串的频度加到较短字符串上，
                // 并从较长字符串中删除那个公共部分
                if (Word.isSimilar(wordI, wordJ)) {
                    // 增加频度
                    if (wordI.getValue().length() > wordJ.getValue().length()) {
                        if (wordJ.getFreq() >= wordI.getFreq()) {
                            wordJ.addFreq(wordI.getFreq());
                            // 剪切词汇
                            wordI.setValue(Utils.stringCut(wordJ.getValue(), wordI.getValue()));
                            // 移除空词汇
                            //if (wordI.getValue().length() == 0) rawTable.remove(wordI);
                        }

                    } else {
                        if (wordI.getFreq() >= wordJ.getFreq()) {
                            wordI.addFreq(wordJ.getFreq());
                            // 剪切词汇
                            wordJ.setValue(Utils.stringCut(wordI.getValue(), wordJ.getValue()));
                            //if (wordJ.getValue().length() == 0) rawTable.remove(wordJ);
                        }

                    }
                }
            }
        }
        rawTable.removeLengthOf(Word.WORD_LENGTH_1);
        rawTable.removeLengthOf(0);
        rawTable.sort();
        rawTable.show();
    }

    // 将一个句子的词语解析出来
    private WordTable extractWords(String sentence, int wordLength) {
        WordTable wordTable = new WordTable();
        int len = sentence.length();
        for (int i = 0; i < len; i++) {
            if (i + wordLength > len) break;
            String term = sentence.substring(i, i + wordLength);
            // 判断该词是否是常规词，若包含了特殊字符，就没必要查询数据库了
            if (!Utils.isRegular(term)) continue;
            // 查询数据库判断是否是词，是则添加
            Word word = db.query(new Word(term));
            if (word != null) {
                wordTable.add(new Word(term));
            }
        }
        return wordTable;
    }

    // 将一句话的词移除，返回被移除后的句子
    private String removeWords(String sentence, int wordLen) {
        String space; // 将被挖去的地方填上
        switch (wordLen) {
            case 4:
                space = "〇〇〇〇";
                break;
            case 3:
                space = "〇〇〇";
                break;
            case 2:
                space = "〇〇";
                break;
            case 1:
                space = "〇";
                break;
            default:
                space = "  ";
        }
        for (int i = 0; i < sentence.length(); i++) {
            if (i + wordLen > sentence.length()) break;
            String next = sentence.substring(i, i + wordLen);
            Word word = db.query(new Word(next));
            // 查询到是一个词汇
            if (word != null) {
                sentence = sentence.substring(0, i) + space + sentence.substring(i+wordLen);
                i--;
            }
        }
        return sentence;
    }
}
