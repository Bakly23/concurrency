package ru.sberbank.bit.kolpakov.concurrency.taskmanager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskManagerImpl implements TaskManager {
    @Override
    public Context execute(Runnable callback, Runnable... runnables) {
        return new ContextImpl(callback, runnables);
    }

    private class ContextImpl implements TaskManager.Context {
        private static final int NUMBER_OF_WORKER_THREADS = 4;
        private final List<Runnable> tasks;
        private final Runnable callback;
        private volatile boolean isInterrupted;
        private volatile boolean hasCountedInterruptedTasks;
        private volatile boolean isFinished;
        private final AtomicInteger successCount;
        private final AtomicInteger failCount;
        private int interruptedTaskCount;
        private int finishedJobWorkers;

        ContextImpl(Runnable callback, Runnable... runnables) {
            this.callback = callback;
            tasks = Arrays.stream(runnables).collect(Collectors.toList());
            successCount = new AtomicInteger();
            failCount = new AtomicInteger();
            interruptedTaskCount = 0;
            IntStream
                    .range(0, NUMBER_OF_WORKER_THREADS)
                    .mapToObj(i -> new Thread(new Worker()))
                    .forEach(Thread::start);
        }

        @Override
        public synchronized int getInterruptedTaskCount() {
            return interruptedTaskCount;
        }

        @Override
        public int getFailedTaskCount() {
            return failCount.get();
        }

        @Override
        public int getSuccessFinishedTaskCount() {
            return successCount.get();
        }

        @Override
        public void interrupt() {
            isInterrupted = true;
        }

        @Override
        public boolean isFinished() {
            return isFinished;
        }

        private synchronized void interruptTaskManager() {
            if(!hasCountedInterruptedTasks) {
                interruptedTaskCount = tasks.size();
                hasCountedInterruptedTasks = true;
            }
        }

        private synchronized void finishTaskManager() {
            ++finishedJobWorkers;
            if(finishedJobWorkers == NUMBER_OF_WORKER_THREADS) {
                callback.run();
                isFinished = true;
            }
        }

        private synchronized Runnable getTask() {
            return tasks.isEmpty() ? null : tasks.remove(0);
        }

        private class Worker implements Runnable {
            @Override
            public void run() {
                while (!isInterrupted) {
                    Runnable task = getTask();
                    if (task != null) {
                        try {
                            task.run();
                            successCount.increment();
                        } catch (Exception e) {
                            failCount.increment();
                        }
                    } else {
                        break;
                    }
                }
                interruptTaskManager();
                finishTaskManager();
            }
        }
    }
}
