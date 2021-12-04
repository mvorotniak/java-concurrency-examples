package com.mvorodeveloper.semaphores;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SemaphoreExample {

    public static void main(String [] args){
        int numberOfThreads = 3;

        Barrier barrier = new Barrier(numberOfThreads);
        List<Thread> threads = IntStream.range(0, numberOfThreads)
            .mapToObj(i -> new Thread(new CoordinatedWorkRunner(barrier)))
            .collect(Collectors.toList());

        threads.forEach(Thread::start);
    }

    public static class CoordinatedWorkRunner implements Runnable {
        private final Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                task();
            } catch (InterruptedException ignored) {
            }
        }

        private void task() throws InterruptedException {
            // Performing Part 1
            System.out.println(Thread.currentThread().getName()
                + " part 1 of the work is finished");

            barrier.barrier();

            // Performing Part2
            System.out.println(Thread.currentThread().getName()
                + " part 2 of the work is finished");
        }
    }

    public static class Barrier {
        private final int numberOfWorkers;
        // We initialize the semaphore to 0, to make sure every thread that tries to acquire the semaphore gets blocked
        private final Semaphore semaphore = new Semaphore(0);
        private int counter = 0;
        private final Lock lock = new ReentrantLock();

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void barrier() {
            lock.lock();
            boolean isLastWorker = false;
            try {
                counter++;

                if (counter == numberOfWorkers) {
                    isLastWorker = true;
                }
            } finally {
                lock.unlock();
            }

            if (isLastWorker) {
                // The last thread to get to the barrier, releases the semaphore numberOfWorkers - 1
                // since "numberOfWorkers - 1" threads are blocked on the semaphore
                semaphore.release(numberOfWorkers - 1);
            } else {
                try {
                    semaphore.acquire();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
