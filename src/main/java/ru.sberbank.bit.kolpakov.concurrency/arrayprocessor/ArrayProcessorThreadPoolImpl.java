package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

import ru.sberbank.bit.kolpakov.concurrency.threadpool.ThreadPool;
import ru.sberbank.bit.kolpakov.concurrency.threadpool.ThreadPoolImpl;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ArrayProcessorThreadPoolImpl extends AbstractArrayProcessor {
    public int[] process(int[] original, int threadCount, ArrayProcessorStrategy strategy) throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(4);
        int[] result = original.clone();
        for (int i = 0; i < threadCount; i++) {
            pool.execute(new ArrayProcessorWorker(result,
                    strategy,
                    getStartPosition(result.length, i, threadCount),
                    getEndPosition(result.length, i, threadCount)));
        }
        pool.finish();
        return result;
    }
}
