package ru.sberbank.bit.kolpakov.concurrency.taskmanager;

/**
 * Created by Georgii Kolpakov on 21.11.16.
 */
public interface TaskManager {
    Context execute(Runnable callback, Runnable... runnables);

    interface Context {
        int getFailedTaskCount();
        int getSuccessFinishedTaskCount();
        int getInterruptedTaskCount();
        void interrupt();
        boolean isFinished();
    }
}
