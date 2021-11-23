package com.mvorodeveloper.threadCoordination;

/**
 * Simply interrupts a thread that is executing a method that throws InterruptedException
 */
public class SimpleInterrupt {

    public static void main(String[] args) {
        BlockingTask blockingTask = new BlockingTask();

        blockingTask.start();
        blockingTask.interrupt();
    }

    private static class BlockingTask extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000000);
            } catch (InterruptedException e) {
                System.out.println("The thread was interrupted");
            }
        }
    }
}
