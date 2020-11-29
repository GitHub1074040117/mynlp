package mynlp;

import mynlp.tokenize.Tokenizer;

public class Main {
    public static void main(String[] args) {
        String sentence = "倚天屠龙记";
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.tokenize(sentence);
    }
}
