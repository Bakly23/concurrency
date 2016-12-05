package ru.sberbank.bit.kolpakov.concurrency.taskmanager;

/**
 * Created by Georgii Kolpakov on 05.12.16.
 */
class AtomicInteger {
    private int value;

    synchronized void increment() {
        value++;
    }

    synchronized int get() {
        return value;
    }
}
