package br.usp.lock;

import java.util.concurrent.Semaphore;

public class ReaderWriterLock implements Lock {
    private final Semaphore readerSemaphore = new Semaphore(1);
    private final Semaphore writerSemaphore = new Semaphore(1);
    private int count = 0;

    public void readLock() throws InterruptedException {
        readerSemaphore.acquire();
        if (count == 0) {
            writerSemaphore.acquire();
        }
        count++;
        readerSemaphore.release();
    }

    public void readUnlock() throws InterruptedException {
        readerSemaphore.acquire();
        count--;
        if (count == 0) {
            writerSemaphore.release();
        }
        readerSemaphore.release();
    }

    public void writeLock() throws InterruptedException {
        writerSemaphore.acquire();
    }

    public void writeUnlock() throws InterruptedException {
        writerSemaphore.release();
    }
}
