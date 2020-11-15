package mynlp.learner;

import mynlp.helper.WordHelper;

class LearningComputer {

    // 计算父子节点间可以组成粘连可能性，子节点的频度大于同代节点的平均频度
    boolean stickLikelihood(LearningTreeNode parent, LearningTreeNode child) {
        if (WordHelper.isIrregular(child.getKey())) return false;
        if (WordHelper.isStopWord(child.getKey()) || WordHelper.isStopWord(parent.getKey())) return false;
        // 排除频度过低的节点
        if (child.getFreq() < 5) return false;
        return child.getFreq() * 1.0 / parent.getFreq() > 0.73;
    }

    // 获取某一代的平均频度值，初代父母为第0代
    /*double getAvgFreq(int generation) {
        if (generation < 0 || generation >= degree) {
            new Exception("Generation exceed!(0 <= generation < " +
                    degree + "; Got " + generation).printStackTrace();
            return 0;
        }
        return avgFreq[generation];
    }*/

    // 计算每一代的平均频度
    /*private void computeAvgFreq() {
        Queue<GramTreeNode> queue = new LinkedList<>();
        GramTreeNode root = tree.getRoot();
        int childSize = root.getChildren().size();
        for (GramTreeNode child : root.getChildren().values()) {
            queue.offer(child);
        }

        for (int generation = 0; generation < degree; generation++) {
            int nextSize = 0; // 下一代孩子的总数量
            for (int i = 0; i < childSize; i++) {
                if (queue.isEmpty()) break;
                GramTreeNode node = queue.poll();
                avgFreq[generation] += node.getFreq();
                for (GramTreeNode child : node.getChildren().values()) {
                    queue.offer(child);
                }
                nextSize += node.getChildren().size();
            }
            avgFreq[generation] = avgFreq[generation] / childSize;
            System.out.println(avgFreq[generation] + "  " + generation);
            childSize = nextSize;
        }
    }*/
}
