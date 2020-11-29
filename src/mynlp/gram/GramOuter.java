package mynlp.gram;

public class GramOuter {
    private GramModel gramModel;

    public GramOuter(String trainingFile, int degree) {
        gramModel = new GramModel(degree);
        gramModel.training(trainingFile);
    }

    // 给定前后两个词，获取后跟概率
    public double getAfterProb(String preWord, String word) {
        return gramModel.getAfterProb(preWord, word);
    }

    public void showGramModel() {
        gramModel.show();
    }
}
