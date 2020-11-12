package mynlp;

import mynlp.gram.GramLearner;
import mynlp.gram.GramModel;
import mynlp.gram.GramTester;
import mynlp.helper.WordHelper;
import mynlp.tokenize.Tokenizer;

import java.util.ArrayList;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        GramLearner learner = new GramLearner();
        System.out.println(learner.learning("anna.txt"));
        ;
    }
}
