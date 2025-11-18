package main;

import browser.NgordnetServer;
import ngrams.NGramMap;
import org.slf4j.LoggerFactory;

import static utils.Utils.TOP_49887_WORDS_FILE;
import static utils.Utils.TOTAL_COUNTS_FILE;

public class Main {
    static {
        LoggerFactory.getLogger(Main.class).info("\033[1;38mChanging text color to white");
    }
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();
        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";

        // 加载 WordNet 数据
        WordNet wn = new WordNet(synsetFile, hyponymFile);

        NGramMap ngm = new NGramMap(TOP_49887_WORDS_FILE, TOTAL_COUNTS_FILE);
        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymsHandler(wn,ngm));
        hns.register("ancestors",new CommonAncestorsHandler(wn));
        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
