package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

import ru.sberbank.bit.kolpakov.concurrency.threadpool.ThreadPool;
import ru.sberbank.bit.kolpakov.concurrency.threadpool.ThreadPoolImpl;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ArrayProcessorImpl extends AbstractArrayProcessor {
    public int[] process(int[] original, int threadCount, ArrayProcessorStrategy strategy) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        int[] result = original.clone();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new ArrayProcessorWorker(result,
                    strategy,
                    getStartPosition(result.length, i, threadCount),
                    getEndPosition(result.length, i, threadCount)));
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        return result;
    }
}
