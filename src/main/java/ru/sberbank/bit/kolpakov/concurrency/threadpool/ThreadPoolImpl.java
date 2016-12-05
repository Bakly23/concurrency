package ru.sberbank.bit.kolpakov.concurrency.threadpool;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Georgii Kolpakov on 14.11.16.
 */
public class ThreadPoolImpl implements ThreadPool {
    private final List<Runnable> tasks;
    private final List<Thread> workers;
    private final Object lock = new Object();
    private volatile boolean isFinished;

    public ThreadPoolImpl(int numOfWorkers) {
        tasks = new ArrayList<>();
        isFinished = false;
        workers = IntStream
                .range(0, numOfWorkers)
                .mapToObj(i -> new Thread(new Worker()))
                .collect(Collectors.toList());
        workers.forEach(Thread::start);
    }

    @Override
    public synchronized void execute(Runnable runnable) {
        if (isFinished) {
            throw new RuntimeException("Thread pool has already finished its work.");
        } else {
            tasks.add(runnable);
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    private synchronized Runnable getTask() {
        return tasks.isEmpty() ? null : tasks.remove(0);
    }

    private synchronized boolean isEmpty() {
        return tasks.isEmpty();
    }



    public void finish() {
        isFinished = true;
        workers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!isFinished || !isEmpty()) {
                Runnable task = getTask();
                if (task != null) {
                    task.run();
                    synchronized (lock) {
                        lock.notify();
                    }
                } else {
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
