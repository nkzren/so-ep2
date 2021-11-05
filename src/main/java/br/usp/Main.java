package br.usp;

import br.usp.database.WordDatabase;
import br.usp.executor.ThreadExecutor;
import br.usp.utils.ResourcesReader;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private static final int NUM_ITERATIONS = 50;

    private final ResourcesReader reader;

    public Main(ResourcesReader reader) {
        this.reader = reader;
    }

    public void init() {
        LOGGER.info("Starting readers-writers");

        for (int i = 0; i < 101; i++) {
            WordDatabase database = new WordDatabase(reader.readLines("bd.txt"));
            LOGGER.info("Starting with readers: " + i);
            long total = 0;
            for (int j = 0; j < NUM_ITERATIONS; j++) {
                LOGGER.debug("Iteration number: " + j);
                ThreadExecutor executor = new ThreadExecutor(database, i);
                try {
                    executor.init();

                    long start = System.currentTimeMillis();
                    executor.start();
                    long end = System.currentTimeMillis();
                    long duration = end - start;
                    LOGGER.debug("Duration: " + duration + "ms");
                    total += duration;
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }
            }
            LOGGER.info("Average duration: " + (total/NUM_ITERATIONS) + "ms");
        }
    }

    public static void main(String[] args) {
        Main main = new Main(new ResourcesReader());

        main.init();
    }
}
