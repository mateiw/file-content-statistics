package org.github.mateiw;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Watches a directory and adds any new files to the FileQueue to be processed asynchronously.
 */
public class DirectoryWatcher {

    private static final Logger logger = LogManager.getLogger(DirectoryWatcher.class);

    private FileQueue fileQueue;
    private Path directory;
    private TextExtractorRegistry textExtractorRegistry;

    public DirectoryWatcher(Path directory, FileQueue fileQueue, TextExtractorRegistry textExtractorRegistry) {
        this.directory = directory;
        this.fileQueue = fileQueue;
        this.textExtractorRegistry = textExtractorRegistry;
    }

    public void watchDirectory() {
        try {
            addExistingFileToQueue();

            var watchService = FileSystems.getDefault().newWatchService();
            directory.register(watchService, ENTRY_CREATE);

            for (;;) {
                var watchKey = watchService.take();
                for (var event: watchKey.pollEvents()) {
                    var eventType = event.kind();
                    var filename = event.context().toString();
                    if (ENTRY_CREATE.equals(eventType)) {
                        fileQueue.add(directory.resolve(filename));
                    }
                    logger.trace("Event kind : " + event.kind() + " - File : " + event.context());

                }
                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException | InterruptedException | RuntimeException e) {
           logger.error("Error watching directory " + directory, e);
        }
    }

    private void addExistingFileToQueue() throws IOException {
        try (var stream = Files.list(directory)) {
            stream.forEach(file -> {
                if (textExtractorRegistry.supports(file)) {
                    try {
                        fileQueue.add(file);
                    } catch (InterruptedException e) {
                        logger.error("Could not add file " + file + " to queue");
                        throw new RuntimeException(e);
                    }
                }
            });
        }

    }

}
