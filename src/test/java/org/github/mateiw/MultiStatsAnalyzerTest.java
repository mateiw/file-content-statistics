package org.github.mateiw;

import org.github.mateiw.statistics.DotCount;
import org.github.mateiw.statistics.MostUsed;
import org.github.mateiw.statistics.MultiStatsAnalyzer;
import org.github.mateiw.statistics.WordCount;

import org.junit.Test;

import java.io.StringReader;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class MultiStatsAnalyzerTest {

    @Test
    public void test() {
        MultiStatsAnalyzer analyzer = new MultiStatsAnalyzer();
        String text = "aaa bbb ccc.\n ccc xxx\nx xx; ?ccc,.xxx";
        var st = new StringReader(text);

        var stats = analyzer.analyze(Paths.get("test.txt"), st);
        assertEquals(3, stats.size());
        assertTrue(stats.stream()
                .filter(s -> s.getStat() instanceof WordCount).findFirst().isPresent());
        assertTrue(stats.stream()
                .filter(s -> s.getStat() instanceof DotCount).findFirst().isPresent());
        assertTrue(stats.stream()
                .filter(s -> s.getStat() instanceof MostUsed).findFirst().isPresent());
        for (var stat : stats) {
            var val = stat.getStat();
            if (val instanceof WordCount) {
                assertEquals(9, ((WordCount)val).getWordCount());
            } else if (val instanceof DotCount) {
                assertEquals(2, ((DotCount)val).getDotCount());
            } else if (val instanceof MostUsed) {
                assertEquals("ccc", ((MostUsed)val).getWord());
                assertEquals(3, ((MostUsed)val).getCount());
            }
        }


    }

}
