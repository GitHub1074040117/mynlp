package mynlp.markov;

class MarkovModel {
    private OneDimMatrix initMtx;
    private TwoDimMatrix omitMtx;
    private TwoDimMatrix transMtx;
    private String[] obser;
    private String[] stateSet;

    MarkovModel(OneDimMatrix initMatrix, TwoDimMatrix transMatrix, String[] observations, String[] stateSet) {
        transMtx = transMatrix;
        initMtx = initMatrix;
        obser = observations;
        this.stateSet = stateSet;
    }

    MarkovModel(String[] obser, String[] stateSet) {
        this.obser = obser;
        this.stateSet = stateSet;
    }

    MarkovModel(OneDimMatrix initMatrix, TwoDimMatrix transMatrix, TwoDimMatrix omitMatrix, String[] observations, String[] stateSet) {
        transMtx = transMatrix;
        initMtx = initMatrix;
        omitMtx = omitMatrix;
        obser = observations;
        this.stateSet = stateSet;
    }

    // 输出最佳隐序列
    void bestHiddenPath() {
        MarkovComputer computer = new MarkovComputer(this);
        String[] path = computer.bestHiddenSequence();
        System.out.println();
        for (int i = 0; i < obser.length; i++) {
            System.out.print(obser[i] + "/" + path[i]);
            if (i != obser.length - 1) {
                System.out.print(", ");
            } else {
                System.out.print("\n");
            }
        }
    }

    // 强模型学习，基于给定的初始模型进行学习
    void learning(OneDimMatrix initMtx, TwoDimMatrix transMtx, TwoDimMatrix omitMtx, String[] obser, String[] stateSet) {
        MarkovLearner learner = new MarkovLearner(initMtx, transMtx, omitMtx, obser, stateSet);
        System.out.println("初始模型：");
        learner.preLearnModel().show();
        MarkovModel newModel = learner.learning();
        System.out.println("\n学习完毕：");
        newModel.show();
        update(newModel);
    }

    // 弱模型学习，基于给定的初始模型进行学习
    void learning(OneDimMatrix initMtx, TwoDimMatrix transMtx, String[] obser, String[] stateSet) {
        MarkovLearner learner = new MarkovLearner(initMtx, transMtx, obser, stateSet);
        System.out.println("初始模型：");
        learner.preLearnModel().show();
        MarkovModel newModel = learner.learning();
        System.out.println("学习完毕：");
        newModel.show();
        update(newModel);
    }

    // 无指导模型学习，完全无初始模型指导的学习
    MarkovModel learning(String[] obser, String[] stateSet) {
        MarkovLearner learner = new MarkovLearner(obser, stateSet);
        System.out.println("初始模型：");
        learner.preLearnModel().show();
        MarkovModel newModel = learner.learning();
        System.out.println("学习完毕：");
        newModel.show();
        update(newModel);
        return newModel;
    }

    void show() {
        initMtx.show();
        transMtx.show();
        omitMtx.show();
    }

    // 更新模型的属性值
    private void update(MarkovModel newModel) {
        this.initMtx = newModel.getInitMtx();
        this.transMtx = newModel.getTransMtx();
        this.omitMtx = newModel.getOmitMtx();
        this.obser = newModel.obser;
        this.stateSet = newModel.getStateSet();
    }

    OneDimMatrix getInitMtx() {
        return initMtx;
    }

    TwoDimMatrix getOmitMtx() {
        return omitMtx;
    }

    TwoDimMatrix getTransMtx() {
        return transMtx;
    }

    String[] getObservations() {
        return obser;
    }

    String[] getStateSet() {
        return stateSet;
    }
}
