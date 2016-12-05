package ru.sberbank.bit.kolpakov.concurrency.taskmanager;

import ru.sberbank.bit.kolpakov.concurrency.taskmanager.TaskManager;
import ru.sberbank.bit.kolpakov.concurrency.taskmanager.TaskManagerImpl;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskManager manager = new TaskManagerImpl();
        TaskManager.Context context = manager.execute(() -> System.out.println("Everything finished"),
                IntStream.range(0, 50).mapToObj(i -> (Runnable) () -> {
                    if(Math.random() < 0.9) {
                        double result = IntStream.range(0, 100 * i)
                                .mapToDouble(i1 -> 2.5 * i1)
                                .reduce((i1, j) -> i1 + 2 * j)
                                .orElse(0.0);
                        System.out.println("Thread: " + i + " showed result: " + result);
                    } else {
                        System.out.println("Thread: " + i + " failed.");
                        throw new RuntimeException("Just throwing exception");
                    }
                })
                        .collect(Collectors.toList())
                        .toArray(new Runnable[25]));
        for(int i = 0; i < 10; i++) {
            System.out.println("Success: " + context.getSuccessFinishedTaskCount() + "; Failed: " + context.getFailedTaskCount() + "; All: " +
                    context.getTotalFinishedTaskCount());
            Thread.sleep(1);
        }
    }
}
