package br.usp.executor;

import br.usp.lock.ReaderWriterLock;
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

    private final ReaderWriterLock lock = new ReaderWriterLock();
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
            thread.join();
        }
    }

    private void addReader() {
        this.threads.add(new Thread(() -> {
            try {
                for (int j = 0; j < 100; j++) {
                    lock.readLock();
                    int random = RandomGenerator.random(words.size() - 1);
                    String s = this.words.get(random);

                    // FIXME: Remover depois que estiver funcionando tudo
//                    LOGGER.info("Reader: " + random + " " + s);
                    lock.readUnlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private void addWriter() {
        this.threads.add(new Thread(() -> {
            try {
                for (int j = 0; j < 100; j++) {
                    lock.writeLock();
                    int random = RandomGenerator.random(words.size() - 1);
                    this.words.set(random, "MODIFICADO");
                    String s = this.words.get(random);

                    // FIXME: Remover logger depois que estiver funcionando tudo
//                    LOGGER.info("Writer: " + random + " " + s);
                    lock.writeUnlock();
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

}
