package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

import java.util.Arrays;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArrayProcessor processor = new ArrayProcessorImpl();
        int[] original = new int[50];
        for (int i = 0; i < 50; i++) {
            original[i] = i;
        }
        int[] result = processor.process(original, 3, i -> i * 2);
        System.out.println("Input: "+ Arrays.toString(original));
        System.out.println("Output: "+Arrays.toString(result));
    }
}
