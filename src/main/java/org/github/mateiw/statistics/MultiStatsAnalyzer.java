package org.github.mateiw.statistics;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.github.mateiw.TextAnalyzer;
import org.github.mateiw.TextStatistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Analyzes text and produces several statistics in one go.
 */
public class MultiStatsAnalyzer implements TextAnalyzer {

    private static final Logger logger = LogManager.getLogger(TextAnalyzer.class);

    private static Pattern WORD_REGEX = Pattern.compile("[a-zA-Z0-9]*");

    @Override
    public List<TextStatistic<?>> analyze(Path file, Reader text) {
        Map<String, Integer> tokenFrequency = new HashMap<>();
        try (var bin = new BufferedReader(text)) {
            String line;
            while ((line = bin.readLine()) != null) {
                String tokens[] = line.toLowerCase().split("\\b");
                for (var token : tokens) {
                    var t = token.trim();
                    if (t.isEmpty()) {
                        continue;
                    }
                    if (t.contains(".")) {
                        int count = tokenFrequency.getOrDefault(".", 0);
                        tokenFrequency.put(".", ++count);
                    } else if (isWord(t)) {
                        int count = tokenFrequency.getOrDefault(t, 0);
                        tokenFrequency.put(t, ++count);
                    }

                }
            }
        } catch (IOException e) {
            logger.error("Could not compute statistics", e);
        }
        var stats = new ArrayList<TextStatistic<?>>();

        int wc = tokenFrequency
                .entrySet().stream()
                .filter(e -> !e.getKey().equals("."))
                .map(e -> e.getValue())
                .reduce(0, Integer::sum);
        stats.add(TextStatistic.from(file, new WordCount(wc)));
        stats.add(TextStatistic.from(file, new DotCount(tokenFrequency.getOrDefault(".", 0))));
        stats.add(TextStatistic.from(file, findMostUsedWord(tokenFrequency)));

        return stats;
    }

    private MostUsed findMostUsedWord(Map<String, Integer> tokenFrequency) {
        int max = tokenFrequency
                .values()
                .stream()
                .max(Comparator.naturalOrder())
                .get();
        String word = tokenFrequency.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
        return new MostUsed(word, max);
    }

    private boolean isWord(String token) {
        return WORD_REGEX.matcher(token).matches();
    }
}
