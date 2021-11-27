package com.mvorodeveloper.dataSharing;

/**
 * Represents the issues that can be faced when two or more threads share the same resources
 */
public class DataSharing {

    public static void main(String[] args) throws InterruptedException {
        IncrementDecrementIndex incrementDecrementIndex = new IncrementDecrementIndex();
        IncrementingThread incrementingThread = new IncrementingThread(incrementDecrementIndex);
        DecrementingThread decrementingThread = new DecrementingThread(incrementDecrementIndex);

        // Running the threads in parallel leads to different index values every time, but we expect the value to be 0
        incrementingThread.start();
        decrementingThread.start();
        incrementingThread.join();
        decrementingThread.join();

        // Waiting for every thread to finish shows the expected index value
//        incrementingThread.start();
//        incrementingThread.join();
//        decrementingThread.start();
//        decrementingThread.join();

        System.out.println("The final index value is " + incrementDecrementIndex.getIndex());
    }

    private static class DecrementingThread extends Thread {
        private final IncrementDecrementIndex incrementDecrementIndex;

        public DecrementingThread(IncrementDecrementIndex incrementDecrementIndex) {
            this.incrementDecrementIndex = incrementDecrementIndex;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                incrementDecrementIndex.decrementIndex();
            }
        }
    }

    private static class IncrementingThread extends Thread {
        private final IncrementDecrementIndex incrementDecrementIndex;

        public IncrementingThread(IncrementDecrementIndex incrementDecrementIndex) {
            this.incrementDecrementIndex = incrementDecrementIndex;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                incrementDecrementIndex.incrementIndex();
            }
        }
    }

    private static class IncrementDecrementIndex {
        private int index = 0;

        public void incrementIndex() {
            index++;
        }

        public void decrementIndex() {
            index--;
        }

        public int getIndex() {
            return index;
        }
    }
}
