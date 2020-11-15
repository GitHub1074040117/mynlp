package mynlp.helper;

public class FuzzyLogicHelper {
    private static final double INTEGER_SIMILARITY = 0.5;

    // 判断两个整数是否相近
    public boolean isSimilar(int integerA, int integerB) {
        double max = Math.max(integerA, integerB);
        double min = Math.min(integerA, integerB);
        return min / max > INTEGER_SIMILARITY;
    }
}
