package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

import java.util.Random;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ArrayProcessorUtils {
    public static int[] getRandomArray(int mean, long size) {
        return (new Random()).ints(size, mean / 2, mean).toArray();
    }
}
