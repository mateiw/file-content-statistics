package org.github.mateiw;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of TextAnalyzer(s)
 */
public class TextAnalyzerRegistry {

    private List<TextAnalyzer> analyzerList = new ArrayList<>();

    public void addAnalyzer(TextAnalyzer analyzer) {
        analyzerList.add(analyzer);
    }

    public List<TextAnalyzer> getAnalyzers() {
        return analyzerList;
    }
}
