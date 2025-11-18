package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    // TODO: Add any necessary static/instance variables.
    private Map<String, TimeSeries> wordTimeSeriesMap; // 每个单词对应一个 TimeSeries（年份 → 频率）
    private TimeSeries totalCounts; // 每年总词频（年份 → 总词数）

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        wordTimeSeriesMap = new HashMap<>();
        totalCounts = new TimeSeries();

        // 读取 words 文件
        In inWords = new In(wordsFilename);
        while (inWords.hasNextLine()) {
            String[] parts = inWords.readLine().split("\t");
            String word = parts[0];
            int year = Integer.parseInt(parts[1]);
            double count = Double.parseDouble(parts[2]);

            wordTimeSeriesMap.putIfAbsent(word, new TimeSeries());
            wordTimeSeriesMap.get(word).put(year, count);
        }

        // 读取 counts 文件
        In inCounts = new In(countsFilename);
        while (inCounts.hasNextLine()) {
            String[] parts = inCounts.readLine().split(",");
            int year = Integer.parseInt(parts[0]);
            double count = Double.parseDouble(parts[1]);
            totalCounts.put(year, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     * @chinese
     * 返回指定单词在指定年份区间内的原始频率（即出现次数）。
     * 返回的是防御性复制，避免外部修改内部数据。
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        if(!wordTimeSeriesMap.containsKey(word)) {
            return new TimeSeries();// 单词不存在，返回空对象
        }
        return new TimeSeries(wordTimeSeriesMap.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     * @chinese
     * 返回指定单词在所有年份中的原始频率。
     * 返回的是防御性复制，避免外部修改内部数据。
     */
    public TimeSeries countHistory(String word) {
        // TODO: Fill in this method.
        if (!wordTimeSeriesMap.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordTimeSeriesMap.get(word), MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     * @chinese
     * 返回所有年份的总词数（即每年所有单词的总出现次数）。
     * 返回的是防御性复制。
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        return new TimeSeries(totalCounts, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     * @chinese
     * 返回指定单词在指定年份区间内的相对频率（即该词频率 / 当年总词数）。
     * 如果单词不存在，返回空 TimeSeries。
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries wordTS = countHistory(word, startYear, endYear);
        if (wordTS.isEmpty()) {
            return new TimeSeries();
        }
        TimeSeries totalTS = new TimeSeries(totalCounts, startYear, endYear);
        return wordTS.dividedBy(totalTS);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     * @chinese
     * 返回指定单词在所有年份中的相对频率。
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     * @chinese
     * 返回多个单词在指定年份区间内的相对频率总和。
     * 忽略不存在的单词，不抛异常。
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries w = weightHistory(word, startYear, endYear);
            result = result.plus(w); // 累加每个单词的相对频率
        }
        return result;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     * @chinese
     * 返回多个单词在所有年份中的相对频率总和。
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
