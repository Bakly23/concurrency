package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public interface ArrayProcessor {
    int[] process(int[] original, int threadCount, ArrayProcessorStrategy strategy) throws InterruptedException;
}
