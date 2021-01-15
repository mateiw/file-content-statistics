package org.github.mateiw;

import java.nio.file.Path;

/**
 * Holder for a particular statistic.
 * @param <T> the statistic class
 */
public class TextStatistic<T> {

    private Path file;
    private T stat;

    public TextStatistic(Path file, T stat) {
        this.file = file;
        this.stat = stat;
    }

    public static <T> TextStatistic<T> from(Path file, T stat) {
        return new TextStatistic(file, stat);
    }

    public Path getFile() {
        return file;
    }

    public T getStat() {
        return stat;
    }

    @Override
    public String toString() {
        return "TextStatistic{" +
                "file='" + file + '\'' +
                ", stat=" + stat +
                '}';
    }
}
