package br.usp.lock;

import java.util.concurrent.Semaphore;

public class SimpleLock {
    private final Semaphore semaphore = new Semaphore(1);

    public void lock() throws InterruptedException {
        semaphore.acquire();
    }

    public void unlock() throws InterruptedException {
        semaphore.release();
    }
}
