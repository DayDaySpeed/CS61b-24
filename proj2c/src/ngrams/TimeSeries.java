package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 * @chinese
 * 这个类继承自 Java 的 TreeMap<Integer, Double>，用于表示某个单词在不同年份的使用频率。
 *
 * 键（Integer）：年份
 *
 * 值（Double）：该年份的频率值 它提供了数据分析相关的工具方法，比如筛选年份区间、相加、相除等。
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
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
        super();
        // TODO: Fill in this constructor.
        //只添加在区域年龄中的
        for (Integer year : ts.keySet()) {
            if (year >= startYear && year <= endYear) {
                this.put(year, ts.get(year));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    // 返回所有年份（键），按升序排列（因为 TreeMap 是有序的）
    public List<Integer> years() {
        // TODO: Fill in this method.
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    // 返回所有频率值（值），顺序与 years() 对应
    public List<Double> data() {
        // TODO: Fill in this method.
        return new ArrayList<>(this.values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    // 返回两个 TimeSeries 的逐年相加结果
    public TimeSeries plus(TimeSeries ts) {
        // TODO: Fill in this method.
        //先创建一个空TimeSeries
        TimeSeries timeSeries = new TimeSeries();
        //先把原有数据放进timeSeries
        for (Integer year : this.keySet()) {
            timeSeries.put(year,this.get(year));
        }
        // 再遍历 ts，把数据加到 timeSeries 中
        for (Integer year : this.keySet()) {
            Double value = timeSeries.get(year);
            if(timeSeries.containsKey(year)){
                timeSeries.put(year,value+timeSeries.get(year));
            }else {
                timeSeries.put(year,value);
            }
        }
        return timeSeries;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    // 返回两个 TimeSeries 的逐年相除结果
    public TimeSeries dividedBy(TimeSeries ts) {
        // TODO: Fill in this method.
        TimeSeries result = new TimeSeries();

        for (Integer year : this.keySet()) {
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("Missing year " + year + " in divisor TimeSeries.");
            }
            result.put(year, this.get(year) / ts.get(year));
        }

        return result;
    }



    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
