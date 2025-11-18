package main;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import help.GraphHelper;

import java.util.*;

public class WordNet {
    // 存储每个 synset 的 ID 和对应的词语集合
    private Map<Integer, List<String>> synsets;

    // 存储每个词语对应的所有 synset ID（反向索引）
    private Map<String, Set<Integer>> wordToIds;

    // 有向图，表示 hyponym 关系
    private Digraph graph;

    public WordNet(String synsetsFile, String hyponymsFile) {
        synsets = new HashMap<>();
        wordToIds = new HashMap<>();

        // 读取 synsets.txt 文件
        In synIn = new In(synsetsFile);
        while (!synIn.isEmpty()) {
            String[] parts = synIn.readLine().split(",");
            int id = Integer.parseInt(parts[0]);
            String[] words = parts[1].split(" ");
            synsets.put(id, Arrays.asList(words));

            // 建立词语到 synset ID 的反向索引
            for (String word : words) {
                wordToIds.computeIfAbsent(word, k -> new HashSet<>()).add(id);
            }
        }

        // 构建 hyponym 图
        graph = new Digraph(synsets.size());
        In hypIn = new In(hyponymsFile);
        while (!hypIn.isEmpty()) {
            String[] ids = hypIn.readLine().split(",");
            int source = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                graph.addEdge(source, Integer.parseInt(ids[i]));
            }
        }
    }

    // 查询某个词的所有下位词（包括自身）
    public Set<String> getHyponyms(String word) {
        Set<Integer> ids = wordToIds.getOrDefault(word, new HashSet<>());
        Set<Integer> descendants = GraphHelper.descendants(graph, ids);
        Set<String> result = new TreeSet<>(); // TreeSet 自动排序
        for (int id : descendants) {
            result.addAll(synsets.get(id));
        }
        return result;
    }
}
