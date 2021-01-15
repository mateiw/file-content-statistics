package org.github.mateiw.statistics;

public class MostUsed {

    private String word;
    private int count;

    public MostUsed(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "MostUsed{" +
                "word='" + word + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
