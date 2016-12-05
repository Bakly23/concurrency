package ru.sberbank.bit.kolpakov.concurrency.threadpool;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public interface ThreadPool {
    void execute(Runnable runnable);
    void finish();
}
