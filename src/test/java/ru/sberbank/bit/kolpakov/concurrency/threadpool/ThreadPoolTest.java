package ru.sberbank.bit.kolpakov.concurrency.threadpool;

import org.junit.Test;
import ru.sberbank.bit.kolpakov.concurrency.arrayprocessor.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ThreadPoolTest {
    @Test
    public void testProcessRandom() throws InterruptedException {
        int length = 1_000_000;
        int[] original = ArrayProcessorUtils.getRandomArray(length / 2, length);
        ArrayProcessorStrategy strategy = i -> i - 10000;
        int[] result = (new ArrayProcessorThreadPoolImpl()).process(original, 1, strategy);
        for (int i = 0; i < length; i++) {
            assertEquals(result[i], strategy.process(original[i]));
        }
    }


}
