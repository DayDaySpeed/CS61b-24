package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.*;

/**
 * CommonAncestorsHandler 用于处理共同祖先查询。
 * 给定多个词语，返回它们在 WordNet 图中的所有共同祖先 synset 中的词语集合。
 */
public class CommonAncestorsHandler extends NgordnetQueryHandler {
    private final WordNet wordnet;

    /**
     * 构造函数，注入 WordNet 实例。
     * @param wn WordNet 图结构对象
     */
    public CommonAncestorsHandler(WordNet wn) {
        this.wordnet = wn;
    }

    /**
     * 处理查询请求。
     * @param q 查询对象，包含词语列表
     * @return JSON 字符串形式的共同祖先词语集合（按字母排序）
     */
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();

        // 获取每个词对应的 synset ID 集合
        List<Set<Integer>> synsetIdSets = new ArrayList<>();
        for (String word : words) {
            synsetIdSets.add(wordnet.getSynsetIds(word));
        }

        // 获取每个 synset ID 的所有祖先 ID（向上遍历图）
        List<Set<Integer>> ancestorSets = new ArrayList<>();
        for (Set<Integer> ids : synsetIdSets) {
            ancestorSets.add(wordnet.getAncestors(ids));
        }

        // 求所有祖先集合的交集，得到共同祖先 ID
        Set<Integer> commonAncestors = new HashSet<>(ancestorSets.get(0));
        for (int i = 1; i < ancestorSets.size(); i++) {
            commonAncestors.retainAll(ancestorSets.get(i));
        }

        // 将共同祖先 synset ID 映射为词语集合
        Set<String> resultWords = wordnet.getWordsFromSynsetIds(commonAncestors);

        // 按字母顺序排序
        List<String> sortedResult = new ArrayList<>(resultWords);
        Collections.sort(sortedResult);

        // 返回 JSON 字符串（NgordnetQueryHandler 会自动序列化）
        return sortedResult.toString();
    }
}
