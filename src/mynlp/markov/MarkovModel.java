package mynlp.markov;

class MarkovModel {
    private OneDimMatrix initMtx;
    private TwoDimMatrix transMtx;
    private TwoDimMatrix omitMtx;
    private OneDimMatrix termMtx;
    private String[] obser;
    private String[] stateSet;

    MarkovModel(OneDimMatrix initMatrix, TwoDimMatrix transMatrix,
                TwoDimMatrix omitMatrix, OneDimMatrix termMatrix,
                String[] observations, String[] stateSet) {
        transMtx = transMatrix;
        initMtx = initMatrix;
        omitMtx = omitMatrix;
        obser = observations;
        termMtx = termMatrix;
        this.stateSet = stateSet;
    }

    // 输出模型的三个矩阵
    void show() {
        initMtx.show();
        transMtx.show();
        omitMtx.show(obser);
        termMtx.show();
    }

    // 输出最佳隐序列
    String[] bestHiddenPath() {
        MarkovComputer computer = new MarkovComputer(this);
        return computer.bestHiddenPath(obser);
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

    OneDimMatrix getTermMtx() {
        return termMtx;
    }

    String[] getStateSet() {
        return stateSet;
    }
}
