package com.mvorodeveloper.dataRace;

/**
 * Example of Data Race that can be fixed by adding the `volatile` keyword
 */
public class DataRaceExample {

    public static void main(String[] args) {
        Data data = new Data();

        Thread thread1 = new Thread(() -> {
           for (int i = 0; i < Integer.MAX_VALUE; i++) {
               data.increment();
           }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                data.checkForRaceCondition();
            }
        });

        thread1.start();
        thread2.start();
    }

    private static class Data {
        private int x = 0;
        private int y = 0;

        // this fixes the data race
//        private volatile int x = 0;
//        private volatile int y = 0;

        public void increment() {
            x++;
            y++;
        }

        public void checkForRaceCondition() {
            if (y > x) {
                System.out.println("Race condition detected " + y + " > " + x);
            }
        }
    }
}
