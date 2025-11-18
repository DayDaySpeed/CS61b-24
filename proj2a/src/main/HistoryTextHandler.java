package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap map;

    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        StringBuilder sb = new StringBuilder();

        for (String word : q.words()) {
            TimeSeries ts = map.countHistory(word, q.startYear(), q.endYear());
            sb.append(word).append(": ").append(ts).append("\n");
        }

        return sb.toString();
    }
}