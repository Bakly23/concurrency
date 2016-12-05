package ru.sberbank.bit.kolpakov.concurrency.runnablemanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class RunnableManagerImpl implements RunnableManager {
    @Override
    public void manage(Runnable callback, Runnable... tasks) {
        Thread managerThread = new Thread(() -> {
            List<Thread> managedThreads = Arrays.stream(tasks)
                    .map(Thread::new)
                    .collect(Collectors.toList());
            Thread callbackThread = new Thread(callback);
            managedThreads.forEach(thread -> {
                try {
                    thread.start();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
            managedThreads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            callbackThread.run();
            try {
                callbackThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        managerThread.run();
    }
}
