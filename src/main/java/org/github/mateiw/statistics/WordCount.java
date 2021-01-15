package org.github.mateiw.statistics;

/**
 * Words count.
 */
public class WordCount {

    private int wordCount;

    public WordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    @Override
    public String toString() {
        return "WordCount{" +
                "wordCount=" + wordCount +
                '}';
    }
}
