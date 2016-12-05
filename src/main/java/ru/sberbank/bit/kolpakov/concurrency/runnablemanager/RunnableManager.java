package ru.sberbank.bit.kolpakov.concurrency.runnablemanager;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public interface RunnableManager {
    void manage(Runnable callback, Runnable... tasks);
}
