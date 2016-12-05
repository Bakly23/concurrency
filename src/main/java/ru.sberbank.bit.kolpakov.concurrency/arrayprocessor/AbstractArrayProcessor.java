package ru.sberbank.bit.kolpakov.concurrency.arrayprocessor;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public abstract class AbstractArrayProcessor implements ArrayProcessor {
    public int getStartPosition(int length, int i, int n) {
        return i * length / n;
    }

    public int getEndPosition(int length, int i, int n) {
        if(i == n - 1) {
            return length;
        } else {
            return (i + 1) * length / n;
        }
    }
}
