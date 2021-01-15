package org.github.mateiw;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.github.mateiw.extractor.TextFileExtractor;
import org.github.mateiw.statistics.MultiStatsAnalyzer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

/**
 * Main class.
 */
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    private static final int poolSize = 5; // TODO extract to config

    public static void main(String[] args) {
        if (args.length != 1) {
            logger.error("Directory path missing");
            System.exit(1);
        }
        String dir = args[0];
        var dirPath = Paths.get(dir);
        if (!(Files.exists(dirPath) && Files.isDirectory(dirPath))) {
            logger.error(dir + " doesn't exist or is not a directory");
            System.exit(2);
        }

        TextExtractorRegistry extractors = new TextExtractorRegistry();
        extractors.registerExtractor(new TextFileExtractor());

        TextAnalyzerRegistry analyzers = new TextAnalyzerRegistry();
        analyzers.addAnalyzer(new MultiStatsAnalyzer());

        FileQueue fileQueue = new FileQueue();
        DirectoryWatcher watcher = new DirectoryWatcher(dirPath, fileQueue, extractors);

        Dispatcher dispatcher = new Dispatcher(fileQueue,
                Executors.newFixedThreadPool(poolSize),
                analyzers,
                extractors
                );
        new Thread(dispatcher, "Dispatcher-Thread").start();
        watcher.watchDirectory();

    }
}
