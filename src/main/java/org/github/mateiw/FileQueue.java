package org.github.mateiw;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Simple wrapper around a BlockingQueue. Takes in file Paths.
 */
public class FileQueue {

    private static final Logger logger = LogManager.getLogger(FileQueue.class);

    private BlockingQueue<Path> queue = new LinkedBlockingDeque<>();

    public void add(Path file) throws InterruptedException {
        logger.debug("Adding file " + file + " to queue");
        queue.put(file);
    }

    public Path take() throws InterruptedException {
        return queue.take();
    }

}
