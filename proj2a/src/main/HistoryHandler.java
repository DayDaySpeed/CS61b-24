package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import com.google.gson.Gson;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;

import java.util.List;
public class HistoryHandler extends NgordnetQueryHandler {
    private final NGramMap map;

    public HistoryHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        // 收集每个词的 TimeSeries
        ArrayList<TimeSeries> seriesList = new ArrayList<>();
        for (String word : words) {
            TimeSeries ts = map.weightHistory(word, startYear, endYear);
            seriesList.add(ts);
        }

        // 使用 Plotter 生成图表
        XYChart chart = Plotter.generateTimeSeriesChart(words, seriesList);

        // 编码为 base64 字符串
        String base64Image = Plotter.encodeChartAsString(chart);

        // 返回 base64 字符串（可选：gson.toJson(base64Image)）
        return base64Image;
    }
}
