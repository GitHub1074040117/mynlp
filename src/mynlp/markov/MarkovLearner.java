package mynlp.markov;

/**
 * 模型学习分别有三种级别
 * 1.完全无指导的学习，输入仅为状态集和观测序列
 * 2.弱指导学习，在1条件基础上，再给出初始概率矩阵和转移矩阵
 * 3.强指导学习，在2条件基础上，再给出发射概率矩阵
 * 给定矩阵并不意味着学习过程中不能改变矩阵，而是在学习中给予有基础的调整
 * */

class MarkovLearner {
    private String[] state; // 状态集合
    private String[] obser;
    private int obserType; // 不同观察值的种类数量
    private String[] obserUnique; // 去重的观察序列

    private OneDimMatrix initMtx;  // 初始概率矩阵
    private TwoDimMatrix transMtx; // 转移概率矩阵
    private TwoDimMatrix omitMtx;  // 发射概率矩阵

    private int N; // 状态种类数
    private int T; // 观测长度
    private MarkovModel model;
    private MarkovComputer computer;

    MarkovLearner(OneDimMatrix initMtx, TwoDimMatrix transMtx, TwoDimMatrix omitMtx, String[] obser, String[] stateSet) {
        this.state = stateSet;
        this.obser = obser;
        this.obserType = Helper.countType(obser);
        this.obserUnique = Helper.countUnique(obser);
        this.initMtx = initMtx;
        this.transMtx = transMtx;
        this.omitMtx = omitMtx;
        this.N = stateSet.length;
        this.T = obser.length;
        this.model = new MarkovModel(initMtx, transMtx, omitMtx, obser, stateSet);
        this.computer = new MarkovComputer(model);
    }

    MarkovLearner(OneDimMatrix initMtx, TwoDimMatrix transMtx, String[] obser, String[] stateSet) {
        this.initMtx = initMtx;
        this.transMtx = transMtx;
        this.state = stateSet;
        this.obser = obser;
        this.obserType = Helper.countType(obser);
        this.obserUnique = Helper.countUnique(obser);
        this.omitMtx = initializeOmitMtx(state, obserType, obserUnique);
        model = new MarkovModel(initMtx, transMtx, omitMtx, obser, stateSet);
        computer = new MarkovComputer(model);
        N = state.length;
        T = obser.length;
    }

    MarkovLearner(String[] obser, String[] stateSet) {
        obserType = Helper.countType(obser);
        obserUnique = Helper.countUnique(obser);
        this.state = stateSet;
        this.obser = obser;
        N = stateSet.length;
        T = obser.length;
        initMtx = initializeInitMtx(state);
        transMtx = initializeTransMtx(state);
        omitMtx = initializeOmitMtx(state, obserType, obserUnique);
        model = new MarkovModel(initMtx, transMtx, omitMtx, obser, stateSet);
        computer = new MarkovComputer(model);
    }

    MarkovModel learning() {
        double[][][] xi = new double[T][N][N];          // 相邻时刻状态转移概率矩阵
        double[][] gama = new double[T][N];             // 某时刻状态概率矩阵

        double likelihood;
        double[][] backward = computer.getBackward();
        double[][] forward = computer.getForward();
        double iter = 1e-3; // 阈值
        do {
            likelihood = computer.forwardLikelihood(forward);
            // E-step

            // 计算gama
            for (int t = 0; t < T; t++) {
                for (int i = 0; i < N; i++) {
                    gama[t][i] = forward[t][i] * backward[t][i] / likelihood;
                }
            }
            // 计算xi
            for (int t = 0; t < T - 1; t++) {
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        xi[t][i][j] = forward[t][i] * transMtx.get(state[i], state[j]) *
                                omitMtx.get(state[j], obser[t + 1]) *
                                backward[t + 1][j] / likelihood;
                    }
                }
            }

            // M-step

            // 计算初始概率矩阵
            for (int i = 0; i < N; i++) {
                initMtx.put(state[i], gama[0][i]);
            }

            // 计算转移概率矩阵
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    double x1 = 0;
                    double x2 = 0;
                    for (int t = 0; t < T - 1; t++) {
                        x1 += xi[t][i][j];
                        double x3 = 0;
                        for (int p = 0; p < N; p++) {
                            x3 += xi[t][i][p];
                        }
                        x2 += x3;
                    }
                    transMtx.put(state[i], state[j], x1 / x2);
                }
            }
            // 计算发射概率矩阵
            for (int j = 0; j < N; j++) {
                for (int obs = 0; obs < obserType; obs++) {
                    double x1 = 0;
                    double x2 = 0;
                    for (int t = 0; t < T; t++) {
                        if (obserUnique[obs].equals(obser[t])) {
                            x1 += gama[t][j];
                        }
                        x2 += gama[t][j];
                    }
                    omitMtx.put(state[j], obserUnique[obs], x1 / x2);
                }
            }
            backward = computer.getBackward();
            forward = computer.getForward();
        } while (Math.abs(likelihood - computer.backwardLikelihood(backward)) > iter);
        return new MarkovModel(initMtx, transMtx, omitMtx, obser, state);
    }

    private OneDimMatrix initializeInitMtx(String[] state) {
        // 初始化初始概率矩阵
        OneDimMatrix initMtx = new OneDimMatrix();
        double[] initRandom = Helper.randoms(state.length);
        for (int i = 0; i < state.length; i++) {
            initMtx.put(state[i], initRandom[i]);
        }
        return initMtx;
    }

    private TwoDimMatrix initializeTransMtx(String[] state) {
        // 初始化转移概率矩阵
        TwoDimMatrix transMtx = new TwoDimMatrix();
        for (String s : state) {
            double[] transRandom = Helper.randoms(state.length);
            for (int j = 0; j < state.length; j++) {
                transMtx.put(s, state[j], transRandom[j]);
            }
        }
        return transMtx;
    }

    private TwoDimMatrix initializeOmitMtx(String[] state, int obserType, String[] obserUnique) {
        TwoDimMatrix omitMtx = new TwoDimMatrix();
        // 初始化发射概率矩阵
        for (String s : state) {
            double[] omitRandom = Helper.randoms(obserType);
            for (int j = 0; j < obserType; j++) {
                omitMtx.put(s, obserUnique[j], omitRandom[j]);
            }
        }
        return omitMtx;
    }

    // 获取学习前的model
    MarkovModel preLearnModel() {
        return model;
    }
}
