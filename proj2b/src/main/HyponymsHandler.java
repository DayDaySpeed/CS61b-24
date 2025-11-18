package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import java.util.Set;
import java.util.TreeSet;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wordnet;

    // 构造函数，注入 WordNet 实例
    public HyponymsHandler(WordNet wn) {
        this.wordnet = wn;
    }

    // 实现抽象方法，处理查询请求
    @Override
    public String handle(NgordnetQuery q) {
        // 获取用户输入的词语列表
        Set<String> result = new TreeSet<>(wordnet.getHyponyms(q.words().get(0)));

        // 如果用户输入多个词，求交集
        for (int i = 1; i < q.words().size(); i++) {
            result.retainAll(wordnet.getHyponyms(q.words().get(i)));
        }

        // 返回 JSON 字符串（NgordnetQueryHandler 会自动序列化）
        return result.toString();
    }
}
