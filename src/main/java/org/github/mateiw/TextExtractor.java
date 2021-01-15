package org.github.mateiw;

import java.io.Reader;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Interface for a generic text extractor. Creates a Supplier that reads a file,
 * extracts the text, stores the text in some way (memory or a temp file) and returns a Reader.
 */
public interface TextExtractor {

    Supplier<Reader> newTextSupplier(Path file);

    boolean canExtract(Path file);

}
