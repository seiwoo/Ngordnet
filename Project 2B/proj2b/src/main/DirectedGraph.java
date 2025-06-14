package main;

import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class DirectedGraph {

    private final Map<String, String[]> numWord;
    private final HashMap<String[], String> wordNum;
    private final HashMap<String, ArrayList<String>> numToNum;

    String wordsFile;
    String countFile;
    NGramMap mapping;

    Map<String, Double> countDict = new HashMap<>();
    public DirectedGraph(HashMap<String, String[]> numToWord, HashMap<String, ArrayList<String>>  numNum, HashMap<String[], String> wordToNum, String wordFile, String countFile) {
        this.wordsFile = wordFile;
        this.countFile = countFile;
        numWord = numToWord;
        numToNum = numNum;
        wordNum = wordToNum;
        mapping = new NGramMap(wordsFile, countFile);
    }

    public void getOneWord_helper(String index, SortedSet<String> set){
        String[] synonyms = numWord.get(index);

        set.addAll(Arrays.asList(synonyms));
        if(numToNum.containsKey(index)) {
            ArrayList<String> directed = numToNum.get(index);
            for (String directedIndex: directed) {
                getOneWord_helper(directedIndex, set);
            }
        }
    }


    public SortedSet<String> getOneWord(String word){
        SortedSet<String> set = new TreeSet<>();
        ArrayList<String> setPoint = new ArrayList<>();
        for (Map.Entry<String[], String> pair : wordNum.entrySet()) {
            for (String target: pair.getKey()){
                if (target.equals(word)){
                    setPoint.add(pair.getValue());
                    break;
                }
            }
        }
        for (String elem: setPoint){
            getOneWord_helper(elem, set);
        }
        return set;
    }

    public String getCommons(List<String> words){
        SortedSet<String> base  = getOneWord(words.get(0));
        if(words.size() != 1) {
            for (int i = 1; i < words.size(); i++) {
                SortedSet<String> variant = getOneWord(words.get(i));
                base.retainAll(variant);
            }
        }
        ArrayList<String> result = new ArrayList<>(base);
        return "[" + String.join(", ", result) + "]";
    }

    private void clearCD() {
        this.countDict = new HashMap<>();
    }

    private ArrayList Sorting(Map<String, Double> hashmap){

        List<Map.Entry<String, Double>> entries = new ArrayList<>(hashmap.entrySet());

        entries.sort(Comparator.comparing(Map.Entry<String, Double>::getValue, Comparator.reverseOrder())
                .thenComparing(Map.Entry::getKey));

        ArrayList output = new ArrayList<>();
        for (Map.Entry<String, Double> entry : entries) {
            output.add(entry.getKey());
        }
        return output;
    }

    public String kSpecified(List<String> words, int k, int startY, int endY) {

        SortedSet<String> base  = getOneWord(words.get(0));
        if(words.size() != 1) {
            for (int i = 1; i < words.size(); i++) {
                SortedSet<String> variant = getOneWord(words.get(i));
                base.retainAll(variant);
            }
        }

        ArrayList<String> result = new ArrayList<>(base);

        for (String word : result) {
            TimeSeries yearCount = mapping.countHistory(word, startY, endY);
            if (!yearCount.isEmpty()) {
                Double num = 0.0;
                for (Double elem : yearCount.values()) {
                    num += elem;
                }
                countDict.put(word, num);
            }
        }

        if (countDict.isEmpty()){
            clearCD();
            return "[]";
        }

        else {

            ArrayList<String> countResult;
            countResult = Sorting(countDict);
            ArrayList<String> output = new ArrayList<>();

            if (countDict.size() <= k) {
                countResult.sort(Comparator.naturalOrder());
                clearCD();
                return "[" + String.join(", ", countResult)+ "]";
            }

            for (int i = 0; i < k; i++) {
                output.add(countResult.get(i));
            }
            output.sort(Comparator.naturalOrder());
            clearCD();
            return "[" + String.join(", ", output) + "]";

        }
    }

}

