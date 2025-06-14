package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {


    GraphCreate base;
    DirectedGraph testGraph;


    private static final int DEFSTART = 1900;
    private static final int DEFLAST = 2020;

    public HyponymsHandler(String hyponyms, String synsets, String wordFile, String countFile) {
        base = new GraphCreate(hyponyms, synsets, wordFile, countFile);
        testGraph =  base.Create();
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> wordList = q.words();
        if (q.k() == 0) {
            return testGraph.getCommons(wordList);
        }
        int k = q.k();
        int startYear = q.startYear();
        int endYear = q.endYear();
        return testGraph.kSpecified(wordList, k, startYear, endYear);
    }
}
