package org.github.mateiw.extractor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.github.mateiw.TextExtractor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class TextFileExtractor implements TextExtractor {

    private static final Logger logger = LogManager.getLogger(TextFileExtractor.class);

    @Override
    public Supplier<Reader> newTextSupplier(Path file){
        return () -> {
            try {
                return new FileReader(file.toFile());
            } catch (FileNotFoundException e) {
                logger.error("Could not open file " + file, e);
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public boolean canExtract(Path file) {
        try {
            if (Files.isRegularFile(file)) {
                return Files.probeContentType(file).equals("text/plain");
            }
        } catch (IOException e) {
            logger.debug("Could not probe content type of file " + file, e);
        }
        return false;
    }

}
