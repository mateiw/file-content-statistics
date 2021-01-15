package org.github.mateiw.statistics;

/**
 * Dots count.
 */
public class DotCount {

    int dotCount;

    public DotCount(int dotCount) {
        this.dotCount = dotCount;
    }

    public int getDotCount() {
        return dotCount;
    }

    @Override
    public String toString() {
        return "DotCount{" +
                "dotCount=" + dotCount +
                '}';
    }
}
