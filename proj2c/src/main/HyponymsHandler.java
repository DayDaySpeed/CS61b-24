package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final WordNet wordnet;
    private final NGramMap ngramMap;

    // 构造函数，注入 WordNet 和 NGramMap 实例
    public HyponymsHandler(WordNet wn, NGramMap ngm) {
        this.wordnet = wn;
        this.ngramMap = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int k = q.k();
        int startYear = q.startYear();
        int endYear = q.endYear();

        // 获取所有词的下位词集合
        Set<String> result = new TreeSet<>(wordnet.getHyponyms(words.get(0)));
        for (int i = 1; i < words.size(); i++) {
            result.retainAll(wordnet.getHyponyms(words.get(i)));
        }

        // 如果 k == 0，直接返回所有下位词（按字母排序）
        if (k == 0) {
            return result.toString();
        }

        // 构建词频映射：词 → 在时间范围内的总频率
        Map<String, Double> freqMap = new HashMap<>();
        for (String word : result) {
            TimeSeries ts = ngramMap.countHistory(word, startYear, endYear);
            double total = ts.values().stream().mapToDouble(Double::doubleValue).sum();
            if (total > 0) {
                freqMap.put(word, total);
            }
        }

        // 排序：按频率降序 → 取前 k 个 → 按字母升序
        List<String> topK = freqMap.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        return topK.toString();
    }
}
