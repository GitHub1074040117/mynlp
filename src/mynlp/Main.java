package mynlp;

import mynlp.tokenize.Tokenizer;

public class Main {
    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();
        System.out.println(tokenizer.tokenize("南京市长江大桥"));
        ;
    }
}
