package org.github.mateiw;

import org.github.mateiw.extractor.TextFileExtractor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Keep track of text extractors.
 */
public class TextExtractorRegistry {

    private List<TextExtractor> extractors = new ArrayList<>();

    public void registerExtractor(TextExtractor extractor) {
        extractors.add(extractor);
    }

    /**
     * Check if any extractor supports this file.
     * @param file file to check
     * @return true if an extractor supports this file
     */
    public boolean supports(Path file) {
        return extractors.stream()
                .filter(extractor -> extractor.canExtract(file))
                .findAny()
                .isPresent();
    }

    /**
     * Get an extractor based on the file type of the supplied file
     * @param file file to check
     * @return an optional extractor that can handle this file
     */
    public Optional<TextExtractor> getExtractorForFile(Path file) {
        return extractors.stream()
                .filter(extractor -> extractor.canExtract(file))
                .findFirst();

    }
}
