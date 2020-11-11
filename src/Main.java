import interact.DialogParser;
import study.language.chinese.article.Article;

public class Main {

    public static void main(String[] args) {

        String filePath = "anna.txt";
        System.out.println("正在读取文本...");
        Article article = new Article(filePath);

        DialogParser parser = new DialogParser();
        parser.getWordTable(article.getContent());
        //parser.getRawTable().show();


    }


}
