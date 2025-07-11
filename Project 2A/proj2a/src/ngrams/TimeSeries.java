package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /**
     * If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here.
     */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super(ts.subMap(startYear, true, endYear, true));
    }

    /**
     * Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {
        return new ArrayList<>(keySet());
    }

    /**
     * Returns all data for this time series. Must correspond to the
     * order of years().
     */
    public List<Double> data() {
        return new ArrayList<>(values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     * <p>
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries newSeries = new TimeSeries();
        for (int year : this.years()) {
            newSeries.put(year, this.get(year) + ts.getOrDefault(year, 0.0));
        }
        for (int year : ts.years()) {
            if (!newSeries.years().contains(year)) {
                newSeries.put(year, ts.get(year));
            }
        }
        return newSeries;
    }


    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries newSeries = new TimeSeries();
        for (int year : this.years()) {
            if (!ts.years().contains(year)) {
                throw new IllegalArgumentException();
            } else {
                newSeries.put(year, this.get(year) / ts.get(year));
            }
        }
        return newSeries;
    }
}
