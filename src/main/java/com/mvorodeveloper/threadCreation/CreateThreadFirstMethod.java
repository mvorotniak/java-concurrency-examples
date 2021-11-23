package com.mvorodeveloper.threadCreation;

/**
 * Simple thread creation and running with exception handling
 */
public class CreateThreadFirstMethod {

    public static void main(String[] args) {
        // overriding the run() method via anonymous class implementing the Runnable interface
        Thread thread = new Thread(() -> {
            // code that will run in a new thread
            System.out.println("Inside of thread " + Thread.currentThread().getName());
            throw new RuntimeException("Intentional Exception");
        });

        // setting the name of the thread. By default the name would be "Thread-0"
        thread.setName("Custom Thread");
        // setting the priority of the thread. You can use numbers (from 1 to 10) or a predefined value
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setUncaughtExceptionHandler((t, e) -> System.out.println(
            "Unexpected exception happened during running thread " + t.getName() + ": " + e.getMessage()));

        System.out.println("We are in thread " + Thread.currentThread().getName() + " before starting the custom thread");
        // the start() method will run the code under the run() method. It takes some time for a thread to start
        thread.start();
        System.out.println("We are in thread " + Thread.currentThread().getName() + " after starting the custom thread");
    }
}
