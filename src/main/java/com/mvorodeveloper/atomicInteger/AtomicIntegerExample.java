package com.mvorodeveloper.atomicInteger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AtomicIntegerExample {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();

        IncrementThread incrementThread = new IncrementThread(inventoryCounter);
        DecrementThread decrementThread = new DecrementThread(inventoryCounter);

        incrementThread.start();
        decrementThread.start();

        incrementThread.join();
        decrementThread.join();

        System.out.println("Value is " + inventoryCounter.getAtomicIntegerValue());
    }

    private static class DecrementThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public DecrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            IntStream.range(0, 10_000_000).forEach(i -> inventoryCounter.decrement());
        }
    }

    private static class IncrementThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public IncrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            IntStream.range(0, 10_000_000).forEach(i -> inventoryCounter.increment());
        }
    }

    private static class InventoryCounter {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        private void increment() {
            atomicInteger.incrementAndGet();
        }

        private void decrement() {
            atomicInteger.decrementAndGet();
        }

        public int getAtomicIntegerValue() {
            return atomicInteger.get();
        }
    }
}
