package mynlp.markov;

class Main {
    public static void main(String[]args) {
        String[] obs = {"1", "2", "3", "1", "3"};
        String[] sta = {"Hot", "Cold"};
        TwoDimMatrix trans = new TwoDimMatrix();
        OneDimMatrix init = new OneDimMatrix();
        TwoDimMatrix omit = new TwoDimMatrix();

        trans.put("Hot", "Cold", 0.3);
        trans.put("Hot", "Hot", 0.7);
        trans.put("Cold", "Cold", 0.6);
        trans.put("Cold", "Hot", 0.4);

        init.put("Hot", 0.5);
        init.put("Cold", 0.5);

        omit.put("Hot", "3", 0.4);
        omit.put("Hot", "2", 0.4);
        omit.put("Hot", "1", 0.2);
        omit.put("Cold", "3", 0.1);
        omit.put("Cold", "2", 0.3);
        omit.put("Cold", "1", 0.6);

        /*trans.put("B", "B", 0.01);
        trans.put("B", "S", 0.01);
        trans.put("B", "M", 0.49);
        trans.put("B", "E", 0.49);

        trans.put("S", "B", 0.5);
        trans.put("S", "S", 0.5);
        trans.put("S", "M", 0.0);
        trans.put("S", "E", 0.0);

        trans.put("M", "B", 0.0);
        trans.put("M", "S", 0.0);
        trans.put("M", "M", 0.5);
        trans.put("M", "E", 0.5);

        trans.put("E", "B", 0.5);
        trans.put("E", "S", 0.5);
        trans.put("E", "M", 0.0);
        trans.put("E", "E", 0.0);

        init.put("E", 0.0);
        init.put("B", 0.5);
        init.put("S", 0.5);
        init.put("M", 0.0);*/
        MarkovModel model = new MarkovModel(init, trans, obs, sta);
        model.learning(init, trans, omit, obs, sta);
        //System.out.println(Arrays.toString(model.bestHiddenPath()));
        //model.learning();
        model.bestHiddenPath();
    }
}
