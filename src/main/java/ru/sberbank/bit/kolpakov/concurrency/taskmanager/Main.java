package ru.sberbank.bit.kolpakov.concurrency.taskmanager;

import ru.sberbank.bit.kolpakov.concurrency.taskmanager.TaskManager;
import ru.sberbank.bit.kolpakov.concurrency.taskmanager.TaskManagerImpl;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class Main {
    private static final Object monitor = new Object();
    private static final int NUMBER_OF_TASKS = 500;
    public static void main(String[] args) throws InterruptedException {
        TaskManager manager = new TaskManagerImpl();
        //java.util.concurrent.atomic instances are used here.
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        TaskManager.Context context = manager.execute(() -> System.out.println("Everything finished"),
                IntStream.range(1, NUMBER_OF_TASKS + 1).mapToObj(i -> (Runnable) () -> {
                    if(Math.random() < 0.9) {
                        double result = IntStream.range(0, 100 * i)
                                .mapToDouble(i1 -> 2.5 * i1)
                                .reduce((i1, j) -> i1 + 2 * j)
                                .orElse(0.0);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Thread: " + i + " showed result: " + result);
                        success.incrementAndGet();
                    } else {
                        System.out.println("Thread: " + i + " failed.");
                        fail.incrementAndGet();
                        throw new RuntimeException("Just throwing exception");
                    }
                })
                        .collect(Collectors.toList())
                        .toArray(new Runnable[25]));
        for(int i = 0; i < NUMBER_OF_TASKS / 6; i++) {
            printContext(context);
            Thread.sleep(1);
        }
        context.interrupt();
        while(!context.isFinished()) {
            synchronized (monitor) {
                monitor.wait(500);
            }
        }
        printContext(context);
        System.out.println("REAL VALUES: ");
        System.out.println("Success: " + success.get() +
                "; Failed: " + fail.get() +
                "; Interrupted: " + (NUMBER_OF_TASKS - success.get() - fail.get()));
    }

    private static void printContext(TaskManager.Context context) {
        System.out.println("Success: " + context.getSuccessFinishedTaskCount() +
                "; Failed: " + context.getFailedTaskCount() +
                "; Interrupted: " + context.getInterruptedTaskCount());
    }
}
