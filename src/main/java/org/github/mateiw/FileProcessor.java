package org.github.mateiw;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Process a file. Extract text using the text extractor that matches the file type and
 * execute the registered analyzer(s).
 * This is executed async in a separate thread.
 */
public class FileProcessor implements Runnable {
    private static final Logger logger = LogManager.getLogger(FileProcessor.class);
    private Path file;
    private Path processedDir;
    private TextExtractorRegistry textExtractorRegistry;
    private TextAnalyzerRegistry textAnalyzerRegistry;

    public FileProcessor(Path file, Path processedDir, TextExtractorRegistry textExtractorRegistry, TextAnalyzerRegistry textAnalyzerRegistry) {
        this.file = file;
        this.processedDir = processedDir;
        this.textExtractorRegistry = textExtractorRegistry;
        this.textAnalyzerRegistry = textAnalyzerRegistry;
    }

    @Override
    public void run() {
        if (!Files.exists(file)) {
            return; // do nothing, file was deleted in the meantime
        }
        try {
            Optional<TextExtractor> textExtractor = textExtractorRegistry.getExtractorForFile(file);
            if (textExtractor.isEmpty()) {
                logger.error("Could not find suitable extractor for file " + file);
                return;
            }
            // extract text
            var readerSupplier = textExtractor.get().newTextSupplier(file);

            try (var textReader = readerSupplier.get()) {
                // compute statistics
                analyzeFile(file, textReader);
                logger.debug("File " + file + " processed");
            } catch (IOException e) {
                logger.error("Could not extract text from file " + file, e);
            }
        } catch (Exception e) {
            logger.error("Error processing file", e);
            throw e;
        }
    }

    private void analyzeFile(Path file, Reader textReader) {
        for (var analyzer: textAnalyzerRegistry.getAnalyzers()) {
            var fileStats = analyzer.analyze(file, textReader);
            moveFileToProcessed(file);
            // TODO extract displaying of stats to another class
            StringBuffer buf = new StringBuffer();
            buf.append("\n****** Statistics for file " + file + "\n");
            for (var stat : fileStats) {
                buf.append("\t\t " + stat + "\n");
            }
            buf.append("*****\n");
            logger.info(buf);
        }
    }

    private void moveFileToProcessed(Path file) {
        try {
            Files.move(file, processedDir.resolve(file.getFileName() +
                    "." + System.currentTimeMillis())); // append unix time to avoid overwriting files
            logger.debug("Moved file " + file + " to " + processedDir.resolve(file.getFileName()));
        } catch (IOException e) {
            logger.error("Could not move file to processed", e);
        }
    }

}
