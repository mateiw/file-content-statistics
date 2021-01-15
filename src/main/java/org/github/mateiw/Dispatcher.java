package org.github.mateiw;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

/**
 * Take files paths from the FileQueue when available and submit to the work pool for processing.
 * Runs as a separate thread.
 */
public class Dispatcher implements Runnable {

    private static final Logger logger = LogManager.getLogger(App.class);

    private FileQueue queue;
    private ExecutorService workerPool;
    private TextAnalyzerRegistry textAnalyzerRegistry;
    private TextExtractorRegistry textExtractorRegistry;

    public Dispatcher(FileQueue queue, ExecutorService workerPool, TextAnalyzerRegistry textAnalyzerRegistry, TextExtractorRegistry textExtractorRegistry) {
        this.queue = queue;
        this.workerPool = workerPool;
        this.textAnalyzerRegistry = textAnalyzerRegistry;
        this.textExtractorRegistry = textExtractorRegistry;
    }

    @Override
    public void run() {
        for(;;) {
            try {
                Path filePath = queue.take();
                FileProcessor processor = new FileProcessor(filePath, textExtractorRegistry, textAnalyzerRegistry);
                logger.debug("Dispatching file " + filePath);
                workerPool.submit(processor);

            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

}
