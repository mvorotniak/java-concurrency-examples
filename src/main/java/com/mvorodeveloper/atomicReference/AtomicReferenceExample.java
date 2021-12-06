package com.mvorodeveloper.atomicReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AtomicReferenceExample {

    private static final int NUM_OF_PUSHING_THREADS = 2;
    private static final int NUM_OF_POPPING_THREADS = 2;

    public static void main(String[] args) throws InterruptedException {
        //StandardStack<Integer> stack = new StandardStack<>();
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        Random random = new Random();

        IntStream.range(0, 100_000).mapToObj(i -> random.nextInt()).forEach(stack::push);

        List<Thread> pushingThreads = Stream.generate(() -> new Thread(() -> {
            while (true) {
                stack.push(random.nextInt());
            }
        })).limit(NUM_OF_PUSHING_THREADS)
            .collect(Collectors.toList());

        List<Thread> poppingThreads = Stream.generate(() -> new Thread(() -> {
            while (true) {
                stack.pop();
            }
        })).limit(NUM_OF_POPPING_THREADS)
            .collect(Collectors.toList());

        List<Thread> threads = new ArrayList<>();
        threads.addAll(poppingThreads);
        threads.addAll(pushingThreads);

        threads.forEach(thread -> thread.setDaemon(true));
        threads.forEach(Thread::start);

        Thread.sleep(10_000);

        System.out.printf("%,d operations were performed in 10 seconds %n", stack.getCounter());
    }

    private static class LockFreeStack<T> {
        private final AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        public void push(T value) {
            StackNode<T> newHead = new StackNode<>(value);

            while(true) {
                StackNode<T> currentHead = head.get();
                newHead.next = currentHead;
                if (head.compareAndSet(currentHead, newHead)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
            atomicInteger.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentHead = head.get();
            StackNode<T> newHead;

            while (currentHead != null) {
                newHead = currentHead.next;
                if (head.compareAndSet(currentHead, newHead)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    currentHead = head.get();
                }
            }

            atomicInteger.incrementAndGet();
            return currentHead != null ? currentHead.value : null;
        }

        public int getCounter() {
            return atomicInteger.get();
        }
    }

    private static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public synchronized void push(T value) {
            StackNode<T> newHead = new StackNode<>(value);
            newHead.next = head;
            head = newHead;
            counter++;
        }

        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }

            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }

        public int getCounter() {
            return counter;
        }
    }

    private static class StackNode<T> {
        private final T value;
        public StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
        }
    }
}
