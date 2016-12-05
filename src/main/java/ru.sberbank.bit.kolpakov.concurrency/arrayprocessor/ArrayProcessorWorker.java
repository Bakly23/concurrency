package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ArrayProcessorWorker implements Runnable {
    private final int[] array;
    private final ArrayProcessorStrategy strategy;
    private int start;
    private int end;

    public ArrayProcessorWorker(int[] array, ArrayProcessorStrategy strategy, int start, int end) {
        this.array = array;
        this.strategy = strategy;
        this.start = start;
        this.end = end;
    }

    public void run() {
        for (int i = start; i < end; i++) {
            array[i] = strategy.process(array[i]);
        }
    }
}
