package com.mvorodeveloper.conditions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountDownLatch {

    public static void main(String[] args) throws InterruptedException {
        SimpleCountDownLatch simpleCountDownLatch = new SimpleCountDownLatch(10);
        List<Thread> threads = Stream
            .generate(() -> new Thread(new Worker(simpleCountDownLatch)))
            .limit(10)
            .collect(Collectors.toList());

        threads.forEach(Thread::start);
        simpleCountDownLatch.await();
        System.out.println("Latch released");
    }

    private static class Worker implements Runnable {
        private final SimpleCountDownLatch simpleCountDownLatch;

        public Worker(SimpleCountDownLatch simpleCountDownLatch) {
            this.simpleCountDownLatch = simpleCountDownLatch;
        }

        @Override
        public void run() {
            doSomeWork();
            System.out.println("Counted down");
            simpleCountDownLatch.countDown();
        }

        private void doSomeWork() {
            List<String> justList = Stream
                .generate(() -> "element")
                .limit(100)
                .collect(Collectors.toList());
        }
    }

    public static class SimpleCountDownLatch {
        private int count;

        public SimpleCountDownLatch(int count) {
            this.count = count;
            if (count < 0) {
                throw new IllegalArgumentException("count cannot be negative");
            }
        }

        /**
         * Causes the current thread to wait until the latch has counted down to zero.
         * If the current count is already zero then this method returns immediately.
         */
        public synchronized void await() throws InterruptedException {
            while (count > 0) {
                wait();
            }
        }

        /**
         *  Decrements the count of the latch, releasing all waiting threads when the count reaches zero.
         *  If the current count already equals zero then nothing happens.
         */
        public synchronized void countDown() {
            if (count > 0) {
                count--;
            }

            if (count == 0){
                notifyAll();
            }
        }

        /**
         * Returns the current count.
         */
        public int getCount() {
            return count;
        }
    }
}
