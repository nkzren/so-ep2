package br.usp.lock;

import java.util.concurrent.Semaphore;

public class SimpleLock implements Lock {
    private final Semaphore semaphore = new Semaphore(1);

    @Override
    public void readLock() throws InterruptedException {
        semaphore.acquire();
    }

    @Override
    public void readUnlock() throws InterruptedException {
        semaphore.release();
    }

    @Override
    public void writeLock() throws InterruptedException {
        semaphore.acquire();
    }

    @Override
    public void writeUnlock() throws InterruptedException {
        semaphore.release();
    }
}
