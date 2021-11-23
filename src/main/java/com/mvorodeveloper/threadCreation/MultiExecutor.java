package com.mvorodeveloper.threadCreation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Multi Task Parallel Executor
 */
public class MultiExecutor {

    private final List<Runnable> tasks;

    /*
     * @param tasks to be executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    /**
     * Starts and executes all the tasks concurrently
     */
    public void executeAll() {
        List<Thread> threads = tasks.stream().map(Thread::new).collect(Collectors.toList());

        threads.forEach(Thread::start);
    }
}
