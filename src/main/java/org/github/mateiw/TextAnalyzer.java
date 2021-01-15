package org.github.mateiw;

import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

/**
 * Text analyzer. Takes in a text Reader and returns one or more statistics for the given text.
 */
public interface TextAnalyzer {

    List<TextStatistic<?>> analyze(Path file, Reader text) ;

}
