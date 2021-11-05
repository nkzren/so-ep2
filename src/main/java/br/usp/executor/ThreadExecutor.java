package br.usp.executor;

import br.usp.database.WordDatabase;
import br.usp.lock.ReaderWriterLock;
import br.usp.utils.RandomGenerator;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ThreadExecutor {
    private static final Logger LOGGER = Logger.getLogger("");

    private static final int NUM_THREADS = 100;

    private final List<Thread> threads;
    private final WordDatabase database;
    private final ReaderWriterLock lock = new ReaderWriterLock();

    /**
     * Atributo que determina a quantidade de readers
     */
    private final int qtdReaders;

    public ThreadExecutor(WordDatabase database, int qtdReaders) {
        this.threads = new LinkedList<>();
        this.database = database;
        this.qtdReaders = qtdReaders;
    }

    public void init() throws InterruptedException {
        for (int i = 0; i < NUM_THREADS; i++) {
            if (i < qtdReaders) {
                addReader();
            } else {
                addWriter();
            }
        }
        Collections.shuffle(this.threads);
    }

    public void start() throws InterruptedException {
        for (Thread thread : this.threads) {
            thread.start();
            thread.join();
        }
    }

    private void addReader() {
        this.threads.add(new Thread(() -> {
            for (int j = 0; j < 100; j++) {
                try {
                    lock.readLock();
                    database.read();
                    lock.readUnlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void addWriter() {
        this.threads.add(new Thread(() -> {
            try {
                for (int j = 0; j < 100; j++) {
                    lock.writeLock();
                    database.write();
                    lock.writeUnlock();
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

}
