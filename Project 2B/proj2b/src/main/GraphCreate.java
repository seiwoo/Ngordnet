package main;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GraphCreate {

    private final HashMap<String, String[]> numWord;
    private final HashMap<String[], String> wordNum;
    private final HashMap<String, ArrayList<String>> numToNum;

    private final String wordFile;

    private final String countFile;
    public GraphCreate(String hyponyms, String synsets, String wordFile, String countFile) {

        this.wordFile = wordFile;
        this.countFile = countFile;

        this.numWord = new HashMap<>();
        this.wordNum = new HashMap<>();
        this.numToNum = new HashMap<>();

        String[] rowValue;
        In sReader = new In(synsets);
        In hReader = new In(hyponyms);

        while (!sReader.isEmpty()) {
            rowValue = sReader.readLine().split(",");
            wordNum.put(rowValue[1].split(" "), rowValue[0]);
            numWord.put(rowValue[0], rowValue[1].split(" "));
        }


        while (!hReader.isEmpty()) {
            rowValue = hReader.readLine().split(",");
            String key = rowValue[0];
            ArrayList<String> tempArray = new ArrayList<>(Arrays.asList(rowValue).subList(1, rowValue.length));
            numToNum.put(key, tempArray);
        }
    }

    public DirectedGraph Create() {

        return new DirectedGraph(numWord, numToNum, wordNum, wordFile, countFile);
    }
}
