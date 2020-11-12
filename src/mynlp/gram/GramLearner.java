package mynlp.gram;

import mynlp.helper.ArrayListHelper;
import mynlp.helper.WordHelper;

import java.util.ArrayList;

public class GramLearner {
    private static final int DEGREE = 4; // 语法深度

    // 学习生词
    public ArrayList<String> learning(String filePath) {
        ArrayList<String> vocabulary = new ArrayList<>();
        GramModel model = new GramModel(DEGREE);
        model.training(filePath);
        GramTree gramTree = model.getGramTree();
        gramTree.optimizeRootChildren();
        for (GramTreeNode child : gramTree.getRoot().getChildren().values()) {
            ArrayList<String> unknowns = deepFirstSearch(child);
            if (unknowns.get(0).equals(child.getKey())) {
                continue;
            }
            vocabulary.addAll(unknowns);
        }
        System.out.println(vocabulary);
        return ArrayListHelper.removeSubStrings(vocabulary);
    }

    // 深度搜索
    private ArrayList<String> deepFirstSearch(GramTreeNode node) {
        ArrayList<String> keys = new ArrayList<>();
        if (node.getChildren().size() == 0) {
            keys.add(node.getKey());
            return keys;
        }
        for (GramTreeNode child : node.getChildren().values()) {
            if (WordHelper.isIrregular(child.getKey())) continue;
            if (GramComputer.stickLikelihood(node, child)) {
                for (String childKey : deepFirstSearch(child)) {
                    keys.add(node.getKey()+childKey);
                }
            }
        }
        if (keys.size() == 0) keys.add(node.getKey());
        return keys;
    }


}
