package br.usp.lock;

public interface Lock {

    void readLock() throws InterruptedException;

    void readUnlock() throws InterruptedException;

    void writeLock() throws InterruptedException;

    void writeUnlock() throws InterruptedException;
}
