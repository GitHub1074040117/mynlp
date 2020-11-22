package mynlp.markov;

class MarkovComputer {
    private TwoDimMatrix transMtx; // 转移概率矩阵
    private TwoDimMatrix omitMtx;  // 发射概率矩阵
    private OneDimMatrix initMtx;  // 初始概率矩阵
    private String[] stateSet;            // 状态集合
    private String[] obser;

    MarkovComputer(MarkovModel model) {
        transMtx = model.getTransMtx();
        omitMtx = model.getOmitMtx();
        initMtx = model.getInitMtx();
        obser = model.getObservations();
        stateSet = model.getStateSet();
    }

    // 向前算法计算似然度
    double forward() {
        double[][] forward = getForward();
        return forwardLikelihood(forward);
    }

    // 向后算法计算似然度
    double backward() {
        double[][] backward = getBackward();
        return backwardLikelihood(backward);
    }

    double[][] getBackward() {
        int T = obser.length;
        int N = stateSet.length;
        double[][] backward = new double[T][N];
        // initialization step
        for (int j = 0; j < N; j++) {
            backward[T-1][j] = 1;
        }
        // recursion step
        for (int t = T - 2; t >= 0; t--) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    backward[t][i] += backward[t + 1][j] * transMtx.get(stateSet[i], stateSet[j]) * omitMtx.get(stateSet[j], obser[t + 1]);
                }
            }
        }
        return backward;
    }

    double[][] getForward() {
        int T = obser.length;
        int N = stateSet.length;
        double[][] forward = new double[T][N];
        // initialization step
        for (int j = 0; j < N; j++) {
            forward[0][j] = initMtx.get(stateSet[j]) * omitMtx.get(stateSet[j], obser[0]);
        }
        // recursion step
        for (int t = 1; t < T; t++) {
            for (int j = 0; j < N; j++) {
                for (int i = 0; i < N; i++) {
                    forward[t][j] += forward[t - 1][i] * transMtx.get(stateSet[i], stateSet[j]) * omitMtx.get(stateSet[j], obser[t]);
                }
            }
        }
        return forward;
    }

    double backwardLikelihood(double[][] backward) {
        double result = 0;
        int N = stateSet.length;
        // termination step
        for (int j = 0; j < N; j++) {
            result += initMtx.get(stateSet[j]) * omitMtx.get(stateSet[j], obser[0]) * backward[0][j];
        }
        return result;
    }

    double forwardLikelihood(double[][] forward) {
        double result = 0;
        int T = obser.length;
        int N = stateSet.length;
        // termination step
        for (int i = 0; i < N; i++) {
            result += forward[T - 1][i];
        }
        return result;
    }

    // Viterbi算法解码
    String[] bestHiddenSequence() {
        int T = obser.length;
        int N = stateSet.length;
        double[][] viterbi = new double[T][N];
        int[][] backPointer = new int[T][N];
        // initialization step
        for (int j = 0; j < N; j++) {
            viterbi[0][j] = initMtx.get(stateSet[j]) * omitMtx.get(stateSet[j], obser[0]);
        }
        // recursion step
        for (int t = 1; t < T; t++) {
            for (int i = 0; i < N; i++) {
                double max = 0;
                int argMax = 0;
                for (int j = 0; j < N; j++) {
                    double v = viterbi[t - 1][j] * transMtx.get(stateSet[j], stateSet[i]) * omitMtx.get(stateSet[i], obser[t]);
                    if (max < v) {
                        max = v;
                        argMax = j;
                    }
                }
                viterbi[t][i] = max;
                backPointer[t][i] = argMax;
            }
        }
        // termination step
        double max = 0;
        int[] bestPath = new int[T];
        for (int i = 0; i < N; i++) {
            if (max < viterbi[T - 1][i]) {
                max = viterbi[T - 1][i];
                bestPath[T - 1] = i;
            }
        }
        for (int t = T - 1; t > 0; t--) {
            bestPath[t - 1] = backPointer[t][bestPath[t]];
        }
        String[] result = new String[bestPath.length];
        for (int i = 0; i < bestPath.length; i++) {
            result[i] = stateSet[bestPath[i]];
        }
        return result;
    }
}
