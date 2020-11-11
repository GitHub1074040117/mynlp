package mynlp;

import mynlp.gram.GramModel;
import mynlp.gram.GramTester;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        GramModel gramModel = new GramModel(3);
        GramTester gramTester = new GramTester(gramModel);
        gramModel.training("nlp.txt");
        System.out.println(gramTester.probability("宾利先生达西"));
        System.out.println("困惑度："+gramTester.perplexity("宾利先生达西"));
//        System.out.println(gramTester.probability("吉英说的"));
        /*
        System.out.println(gramModel.randomSentence("伊丽莎白"));
        System.out.println(gramModel.randomSentence());
        System.out.println(gramModel.randomSentence());
        System.out.println(gramModel.randomSentence());*/
    }
}
