package br.usp.executor;

import br.usp.utils.RandomGenerator;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ThreadExecutor {
    private static final Logger LOGGER = Logger.getLogger("");

    private static final int NUM_THREADS = 100;

    private final List<Thread> threads;
    private final List<String> words;

    private final Semaphore readerSemaphore = new Semaphore(1);
    private final Semaphore writerSemaphore = new Semaphore(1);
    private int count = 0;

    /**
     * Atributo que determina a quantidade de readers
     */
    private final int qtdReaders;

    public ThreadExecutor(List<String> words, int qtdReaders) {
        this.threads = new LinkedList<>();
        this.words = words;
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
        }
    }

    private void addReader() {
        this.threads.add(new Thread(() -> {
            try {
                Thread.currentThread().join();
                for (int j = 0; j < 100; j++) {
                    readLock();
                    int random = RandomGenerator.random(words.size() - 1);
                    String s = this.words.get(random);

                    // FIXME: Remover depois que estiver funcionando tudo
//                    LOGGER.info("Reader: " + random + " " + s);
                    readUnlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private void addWriter() {
        this.threads.add(new Thread(() -> {
            try {
                Thread.currentThread().join();
                for (int j = 0; j < 100; j++) {
                    writeLock();
                    int random = RandomGenerator.random(words.size() - 1);
                    this.words.set(random, "MODIFICADO");
                    String s = this.words.get(random);

                    // FIXME: Remover logger depois que estiver funcionando tudo
//                    LOGGER.info("Writer: " + random + " " + s);
                    writeUnlock();
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private void readLock() throws InterruptedException {
        readerSemaphore.acquire();
        if (count == 0) {
            writerSemaphore.acquire();
        }
        count++;
        readerSemaphore.release();
    }

    private void readUnlock() throws InterruptedException {
        readerSemaphore.acquire();
        count--;
        if (count == 0) {
            writerSemaphore.release();
        }
        readerSemaphore.release();
    }

    private void writeLock() throws InterruptedException {
        writerSemaphore.acquire();
    }

    private void writeUnlock() throws InterruptedException {
        writerSemaphore.release();
    }
}
