package mynlp.tokenize;

import mynlp.dict.Dictionary;
import mynlp.markov.MarkovOuter;

import java.util.ArrayList;

/**
 * 结合词典和隐马尔科夫模型的分词法
 * 传统词典分词法具有正确性高的优点，但无法识别未记录的词
 * 基于隐马尔可夫模型的分词法能够识别未录入在词典中的词
 * */
public class Tokenizer {
    private Dictionary dictionary;
    private MarkovOuter markovOuter;

    public Tokenizer() {
        dictionary = new Dictionary();
        markovOuter = new MarkovOuter();
    }

    public void tokenize(String sentence) {
        ArrayList<String> dictTok = dictionary.tokenize(sentence);
        ArrayList<String> markTok = markovOuter.tokenize(sentence);
        System.out.println(dictTok);
        System.out.println(markTok);
    }
}
