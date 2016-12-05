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
        private final List<Thread> workers;
        private final List<Runnable> tasks;
        private final Runnable callback;
        private boolean isFinished;
        private final AtomicInteger successCount;
        private final AtomicInteger failCount;
        private final AtomicInteger finishedWorkers;

        ContextImpl(Runnable callback, Runnable... runnables) {
            this.callback = callback;
            tasks = Arrays.stream(runnables).collect(Collectors.toList());
            successCount = new AtomicInteger();
            failCount = new AtomicInteger();
            finishedWorkers = new AtomicInteger();
            workers = IntStream
                    .range(0, 4)
                    .mapToObj(i -> new Thread(new Worker()))
                    .collect(Collectors.toList());
            workers.forEach(Thread::start);
        }

        @Override
        public int getTotalFinishedTaskCount() {
            return successCount.get() + failCount.get();
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
        public synchronized void interrupt() {
            isFinished = false;
        }

        @Override
        public synchronized boolean isFinished() {
            return isFinished;
        }

        private void incrementSuccessFinishedTaskCount() {
            successCount.increment();
        }

        private void incrementFailedTaskCount() {
            failCount.increment();
        }

        private synchronized Runnable getTask() {
            return tasks.isEmpty() ? null : tasks.remove(0);
        }

        private synchronized boolean isEmpty() {
            return tasks.isEmpty();
        }

        private class Worker implements Runnable {
            @Override
            public void run() {
                while (!isFinished() || !isEmpty()) {
                    Runnable task = getTask();
                    if (task != null) {
                        try {
                            task.run();
                            incrementSuccessFinishedTaskCount();
                        } catch (Exception e) {
                            incrementFailedTaskCount();
                        }
                    } else {
                        break;
                    }
                }
                if(allWorkersFinished()) {
                    callback.run();
                }
            }
        }

        private synchronized boolean allWorkersFinished() {
            finishedWorkers.increment();
            return workers.size() == finishedWorkers.get();
        }
    }
}
