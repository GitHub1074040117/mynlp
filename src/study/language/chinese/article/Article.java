package study.language.chinese.article;

import study.language.chinese.word.WordCoder;
import study.language.chinese.word.WordTable;

import java.io.BufferedReader;
import java.io.File;

public class Article {
    private WordTable wordTable;
    private String content;
    private static final int LIMITED_LENGTH = 5;

    public Article(String filePath) {
        this.wordTable = new WordTable();
        readContentFrom(filePath);
    }

    // 读取文件
    private void readContentFrom(String filePath){
        File file = new File(filePath);
        StringBuilder result = new StringBuilder();
        System.out.println("正在读取文本...");
        try{
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));//构造一个BufferedReader类来读取文件
            String s;
            while((s = br.readLine())!= null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("文本读取完成...");
        this.content = result.toString();
    }

    public String getContent() {
        return content;
    }

    // 接收一篇文章,统计里面的词汇
    public WordTable getWordTable(int wordLength){
        System.out.println("正在生成词汇表...");
        if (wordLength > LIMITED_LENGTH) try {
            throw new Exception("词汇超出长度！");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        content = washContent(content);
        System.out.println("正在读取文本内容...");
        for (int i = 0; i < content.length(); i = i + wordLength) {
            if (i + wordLength > content.length()) continue;
            String word = content.substring(i, i + wordLength);
            if (i % 20 == 0) System.out.println();
            System.out.printf("%-8s", word);
            countWord(word);
        }
        // 删除频度过低词汇
        //wordTable.refine();
        return wordTable;
    }

    // 记录每个词语
    private void countWord(String word) {
        wordTable.insert(word);
    }

    // 去除文章中的标点符号
    public String washContent(String content) {
        System.out.println("正在清洗词汇表...");
        for (int i = 0; i < content.length(); i++) {
            if (!isRegular(content.charAt(i))) {
                content = removeCharAt(content, i);
                i--;
            }
        }
        System.out.println("词汇表清洗完成...");
        return content;
    }

    // 删除字符串中某个位置的字符
    private static String removeCharAt(String s, int index) {
        return s.substring(0, index) + s.substring(index + 1);
    }

    // 判断该字是否为特殊字符
    private static boolean isRegular(char word) {
        String str = WordCoder.codeGB2312(word);
        String min = WordCoder.codeGB2312('啊');
        String max = WordCoder.codeGB2312('齄');
        return str.compareTo(min) >= 0 && str.compareTo(max) <= 0;
    }
}
