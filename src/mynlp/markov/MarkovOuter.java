package mynlp.markov;

import mynlp.utils.StringUtility;

import java.util.ArrayList;

public class MarkovOuter {
    private static final String S = "S";
    private static final String E = "E";
    private static final String TRAINING_FILE = "src/mynlp/markov/training/anna.txt";
    private MarkovBuilder builder;

    public MarkovOuter() {
        // 根据文件创建马尔可夫模型
        builder = new MarkovBuilder(MarkovOuter.TRAINING_FILE);
    }

    public ArrayList<String> tokenize(String sentence) {
        ArrayList<String> result = new ArrayList<>();
        String[] observations = StringUtility.toStringArray(sentence);
        MarkovModel model = builder.getMarkovModel(observations);
        String[] path = model.bestHiddenPath();

        StringBuilder word = new StringBuilder();
        for (int i = 0; i < observations.length; i++) {
            word.append(observations[i]);
            if (path[i].equals(S) || path[i].equals(E)) {
                result.add(word.toString());
                word = new StringBuilder();
            }
        }

        return result;
    }

    // 输出句子的标记序列
    public void showHiddenPath(String sentence) {
        String[] observations = StringUtility.toStringArray(sentence);
        MarkovModel model = builder.getMarkovModel(observations);
        String[] path = model.bestHiddenPath();
        // 打印输出
        print(observations, path);
    }

    public void showMarkovModel(String sentence) {
        String[] observations = StringUtility.toStringArray(sentence);
        MarkovModel model = builder.getMarkovModel(observations);
        model.show();
    }

    // 打印输出
    private void print(String[] observations, String[] path) {
        System.out.println();
        StringBuilder word = new StringBuilder();
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < observations.length; i++) {
            word.append(observations[i]);
            state.append(path[i]);
            if (path[i].equals(S) || path[i].equals(E)) {
                System.out.print(word + "/" + state);
                word = new StringBuilder();
                state = new StringBuilder();
                if (i != observations.length - 1) {
                    System.out.print(", ");
                } else {
                    System.out.print("\n");
                }
            }
        }
    }
}
