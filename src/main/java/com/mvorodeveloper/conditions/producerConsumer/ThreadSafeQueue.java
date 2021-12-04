package com.mvorodeveloper.conditions.producerConsumer;

import java.util.LinkedList;
import java.util.Queue;

import com.mvorodeveloper.conditions.producerConsumer.MatrixPair;

/**
 * Thread safe queue for producer-consumer usage
 */
public class ThreadSafeQueue {

    private static final int QUEUE_CAPACITY = 5;

    private boolean isTerminated = false;
    private boolean isEmpty = true;

    private final Queue<MatrixPair> queue = new LinkedList<>();

    /**
     * Adds a matrix pair to the queue if the it's not full
     * @param matrixPair the {@link MatrixPair} to add to the queue
     */
    public synchronized void add(MatrixPair matrixPair) {
        if (queue.size() == QUEUE_CAPACITY) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        queue.add(matrixPair);
        isEmpty = false;
        notify();
    }

    /**
     * Removes the head element of the queue if we still have matrix pairs in the queue
     * @return the {@link MatrixPair} that was removed
     */
    public synchronized MatrixPair remove() {
        MatrixPair removedMatrixPair;
        if(isEmpty && !isTerminated) {
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
            notifyAll();
        }

        return removedMatrixPair;
    }

    /**
     * Stops queue operations
     */
    public synchronized void terminate() {
        isTerminated = true;
        notifyAll();
    }
}
