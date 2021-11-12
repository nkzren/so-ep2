package br.usp;

import br.usp.database.WordDatabase;
import br.usp.executor.ThreadExecutor;
import br.usp.lock.ReaderWriterLock;
import br.usp.lock.SimpleLock;
import br.usp.utils.ResourcesReader;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private static final int NUM_ITERATIONS = 50;

    private final ResourcesReader reader;

    public Main(ResourcesReader reader) {
        this.reader = reader;
    }

    public void initReadersWriters() {
        LOGGER.info("Starting readers-writers");

        for (int i = 0; i < 101; i++) {
            WordDatabase database = new WordDatabase(reader.readLines("bd.txt"));
            LOGGER.info("Starting with readers: " + i);
            long total = 0;
            for (int j = 0; j < NUM_ITERATIONS; j++) {
                LOGGER.debug("Iteration number: " + j);
                long duration = runExecutor(database, i);
                total += duration;
            }
            LOGGER.info("Average duration: " + (total/NUM_ITERATIONS) + "ms");
        }
    }

    /**
     * @param database o db de palavras
     * @param numReaders quantidade de readers a ser utilizada no executor
     * @return a duracao da execucao
     */
    private long runExecutor(WordDatabase database, int numReaders) {

        // decomentar a linha de baixo para rodar: reader writer lock
        // ThreadExecutor executor = new ThreadExecutor(database, numReaders, new ReaderWriterLock());

        // decomentar a linha de baixo para rodar: simple lock
        ThreadExecutor executor = new ThreadExecutor(database, numReaders, new SimpleLock());
        try {
            executor.init();

            long start = System.currentTimeMillis();
            executor.start();
            long end = System.currentTimeMillis();
            long duration = end - start;
            LOGGER.debug("Duration: " + duration + "ms");
            return duration;
        } catch (InterruptedException e) {
            LOGGER.error(e);
            return 0L;
        }
    }

    public static void main(String[] args) {
        Main main = new Main(new ResourcesReader());

        main.initReadersWriters();
    }
}
