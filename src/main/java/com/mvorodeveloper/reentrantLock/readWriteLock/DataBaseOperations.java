package com.mvorodeveloper.reentrantLock.readWriteLock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Example for comparing simple ReentrantLock and ReadWriteLock when working with data bases
 */
public class DataBaseOperations {

    private static final int HIGHEST_PRICE = 1000;
    private static final int NUMBER_OF_READ_THREADS = 8;

    public static void main(String[] args) {
        DataBaseOperation dataBaseOperation = new DataBaseOperation();
        Random random = new Random();

        for (int i = 0; i < 100_000; i++) {
            dataBaseOperation.addItemToPrice(random.nextInt(HIGHEST_PRICE));
        }

        Thread writeThread = new Thread(() -> {
            while(true) {
                System.out.println("Adding and removing item...");
                dataBaseOperation.addItemToPrice(random.nextInt(HIGHEST_PRICE));
                dataBaseOperation.removeItemFromPrice(random.nextInt(HIGHEST_PRICE));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        });

        // As we set the writerThread as a daemon - the thread will stop when the reader threads stop reading
        writeThread.setDaemon(true);
        writeThread.start();

        List<Thread> readThreads = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_READ_THREADS; i++) {
            Thread readThread = new Thread(() -> {
                for (int y = 0; y < 100_000; y++) {
                    int highestPrice = random.nextInt(HIGHEST_PRICE);
                    int lowestPrice = highestPrice > 0 ? random.nextInt(highestPrice) : 0;

                    int items = dataBaseOperation.getAmountOfItemsByPriceRange(lowestPrice, highestPrice);
                }
            });
            readThread.setDaemon(true);
            readThreads.add(readThread);
        }

        long start = System.currentTimeMillis();

        readThreads.forEach(Thread::start);
        readThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        });

        long end = System.currentTimeMillis();
        System.out.println("Time for reading " + (end - start));
    }

    private static class DataBaseOperation {
        private final TreeMap<Integer, Integer> priceToQuantity = new TreeMap<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock writeLock = readWriteLock.writeLock();
        private final Lock readLock = readWriteLock.readLock();

        public int getAmountOfItemsByPriceRange(int lower, int upper) {
            readLock.lock();
            //lock.lock();
            try {
                Map.Entry<Integer, Integer> fromItem = priceToQuantity.ceilingEntry(lower);
                Map.Entry<Integer, Integer> toItem = priceToQuantity.floorEntry(upper);

                if (fromItem == null || toItem == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> subMap =
                    priceToQuantity.subMap(fromItem.getKey(), true, toItem.getKey(), true);

                int summary = 0;
                for (int quantity : subMap.values()) {
                    summary += quantity;
                }

                return summary;
            } finally {
                readLock.unlock();
                //lock.unlock();
            }
        }

        public void addItemToPrice(int price) {
            writeLock.lock();
            //lock.lock();
            try {
                Integer quantityForPrice = priceToQuantity.get(price);
                priceToQuantity.put(price, quantityForPrice == null ? 1 : quantityForPrice + 1);
            } finally {
                writeLock.unlock();
                //lock.unlock();
            }
        }

        public void removeItemFromPrice(int price) {
            writeLock.lock();
            //lock.lock();
            try {
                Integer quantityForPrice = priceToQuantity.get(price);
                if (quantityForPrice == null || quantityForPrice == 1) {
                    priceToQuantity.remove(price);
                } else {
                    priceToQuantity.put(price, quantityForPrice - 1);
                }
            } finally {
                writeLock.unlock();
                //lock.unlock();
            }
        }
    }
}
