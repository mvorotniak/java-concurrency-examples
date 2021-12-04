package com.mvorodeveloper.conditions.producerConsumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread safe queue for producer-consumer usage
 */
public class ThreadSafeQueue {

    private static final int QUEUE_CAPACITY = 5;

    // signals the consumer that the producer has nothing more to offer and the consumer should terminate
    private boolean isTerminated = false;
    private boolean isEmpty = true;

    private final Queue<MatrixPair> queue = new LinkedList<>();

    /**
     * Adds a matrix pair to the queue if the it's not full
     * @param matrixPair the {@link MatrixPair} to add to the queue
     */
    public synchronized void add(MatrixPair matrixPair) {
        while (queue.size() == QUEUE_CAPACITY) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        queue.add(matrixPair);
        isEmpty = false;
        // wake up the consumer
        notify();
    }

    /**
     * Removes the head element of the queue if we still have matrix pairs in the queue
     * @return the {@link MatrixPair} that was removed
     */
    public synchronized MatrixPair remove() {
        MatrixPair removedMatrixPair;
        while (isEmpty && !isTerminated) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        if (queue.size() == 1) {
            isEmpty = true;
        }

        if (queue.size() == 0 && isTerminated) {
            return null;
        }

        System.out.println("Queue size " + queue.size());

        removedMatrixPair = queue.remove();
        if(queue.size() == QUEUE_CAPACITY - 1) {
            // wake up the producer
            notifyAll();
        }

        return removedMatrixPair;
    }

    /**
     * Called by the producer to let the consumer know that no more matrix pairs will be added to the queue
     * and the consumer should terminate it's work
     */
    public synchronized void terminate() {
        isTerminated = true;
        // wake up all the consumer threads. The producer was completely decoupled
        notifyAll();
    }
}
