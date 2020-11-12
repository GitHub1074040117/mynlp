package mynlp;

import mynlp.gram.GramModel;
import mynlp.gram.GramTester;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        GramModel gramModel = new GramModel(3);
        GramTester gramTester = new GramTester(gramModel);
        gramModel.training("nlp.txt");
        gramTester.printGramTree();
        /*System.out.println(gramModel.randomSentence());
        System.out.println(gramModel.randomSentence("伊丽"));*/
    }
}
