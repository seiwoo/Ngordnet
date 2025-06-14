package ngrams;
import java.util.Collection;
import edu.princeton.cs.algs4.In;
import java.util.HashMap;
import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;
/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    private final HashMap<String, TimeSeries> mapWorld = new HashMap<>();
    private final TimeSeries countWord = new TimeSeries();
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsInput = new In(wordsFilename);
        In countsInput = new In(countsFilename);
        while (!wordsInput.isEmpty()) {
            String line = wordsInput.readLine();
            String[] parts = line.split("\t");
            String word = parts[0];
            String year = parts[1];
            String value = parts[2];
            if (!mapWorld.containsKey(word)) {
                TimeSeries tempMap = new TimeSeries();
                tempMap.put(Integer.valueOf(year), Double.parseDouble(value));
                mapWorld.put(word, tempMap);
            } else {
                mapWorld.get(word).put(Integer.valueOf(year), Double.parseDouble(value));
            }
        }
        while (!countsInput.isEmpty()) {
            String line = countsInput.readLine();
            String[] parts = line.split(",");
            int year = Integer.parseInt(parts[0]);
            double totalCount = Double.parseDouble(parts[1]);
            countWord.put(year, totalCount);
        }
        wordsInput.close();
        countsInput.close();
    }
    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!mapWorld.containsKey(word)) {
            return new TimeSeries();
        } else {
            TimeSeries mapTemp = mapWorld.get(word);
            return new TimeSeries(mapTemp, startYear, endYear);
        }
    }
    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!mapWorld.containsKey(word)) {
            return new TimeSeries();
        } else {
            TimeSeries mapTemp = mapWorld.get(word);
            return new TimeSeries(mapTemp, MIN_YEAR, MAX_YEAR);
        }
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(countWord, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!mapWorld.containsKey(word)) {
            return new TimeSeries();
        } else {
            TimeSeries newS = mapWorld.get(word);
            newS = newS.dividedBy(countWord);
            return new TimeSeries(newS, startYear, endYear);
        }
    }
    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        if (words.isEmpty()) {
            return new TimeSeries();
        }
        TimeSeries result = null;
        int divisor = words.size();
        for (String word : words) {
            if (mapWorld.containsKey(word)) {
                TimeSeries wordSeries = new TimeSeries(mapWorld.get(word), startYear, endYear);

                if (result == null) {
                    result = wordSeries;
                } else {
                    result = result.plus(wordSeries);
                }
            }
        }
        return result.dividedBy(countWord);
    }
    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }
}
