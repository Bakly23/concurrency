package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

import org.junit.Test;
import ru.sberbank.bit.kolpakov.concurrency.arrayprocessor.ArrayProcessorImpl;
import ru.sberbank.bit.kolpakov.concurrency.arrayprocessor.ArrayProcessorUtils;

import static org.junit.Assert.assertEquals;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ArrayProcessorImplTest {
    @Test
    public void testProcess() throws InterruptedException {
        int length = 1_000_000;
        int[] original = new int[length];
        for (int i = 0; i < length; i++) {
            original[i] = i;
        }
        int[] result = (new ArrayProcessorImpl()).process(original, 15, i -> i * 3);
        for (int i = 0; i < length; i++) {
            assertEquals(result[i], original[i] * 3);
        }

    }

    @Test
    public void testProcessRandom() throws InterruptedException {
        int length = 1_000_000;
        int[] original = ArrayProcessorUtils.getRandomArray(length / 2, length);
        int[] result = (new ArrayProcessorImpl()).process(original, 11, i -> i - 10000);
        for (int i = 0; i < length; i++) {
            assertEquals(result[i], original[i] - 10000);
        }
    }
}
