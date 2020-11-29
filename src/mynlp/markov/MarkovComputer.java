package mynlp.markov;

class MarkovComputer {
    private OneDimMatrix initMtx;  // 初始概率矩阵
    private TwoDimMatrix transMtx; // 转移概率矩阵
    private TwoDimMatrix omitMtx;  // 发射概率矩阵
    private OneDimMatrix termMtx;  // 终结概率矩阵
    private String[] stateSet;     // 状态集合

    MarkovComputer(MarkovModel model) {
        initMtx = model.getInitMtx();
        transMtx = model.getTransMtx();
        omitMtx = model.getOmitMtx();
        termMtx = model.getTermMtx();
        stateSet = model.getStateSet();
    }

    // Viterbi算法解码，返回状态序列
    String[] bestHiddenPath(String[] obser) {
        int T = obser.length;
        int N = stateSet.length;
        double[][] viterbi = new double[T][N];
        int[][] backPointer = new int[T][N];
        // initialization step
        for (int j = 0; j < N; j++) {
            double a = initMtx.get(stateSet[j]);
            double b = omitMtx.get(stateSet[j], obser[0]);
            double c = a * b;
            viterbi[0][j] = c;
        }
        // recursion step
        for (int t = 1; t < T; t++) {
            for (int i = 0; i < N; i++) {
                double max = 0;
                int argMax = 0;
                for (int j = 0; j < N; j++) {
                    double a = viterbi[t - 1][j];
                    double b = transMtx.get(stateSet[j], stateSet[i]);
                    double c = omitMtx.get(stateSet[i], obser[t]);
                    double v = a * b * c;
                    if (max < v) {
                        max = v;
                        argMax = j;
                    }
                }
                viterbi[t][i] = max;
                backPointer[t][i] = argMax;
            }
        }
        // 考虑终结概率
        for (int i = 0; i < N; i++) {
            viterbi[T - 1][i] *= termMtx.get(stateSet[i]);
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
