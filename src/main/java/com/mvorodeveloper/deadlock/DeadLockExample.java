package com.mvorodeveloper.deadlock;

import java.util.Random;

/**
 * Deadlock example that can be fixed by lock ordering
 */
public class DeadLockExample {

    public static void main(String[] args) {
        Road road = new Road();

        Thread trainA = new TrainA(road);
        Thread trainB = new TrainB(road);

        trainA.start();
        trainB.start();
    }

    private static class TrainA extends Thread {
        private final Random random = new Random();
        private final Road road;

        public TrainA(Road road) {
            this.road = road;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(random.nextInt(5));
                } catch (InterruptedException ignored) {
                }

                road.takeRoadA();
            }
        }
    }

    private static class TrainB extends Thread {
        private final Random random = new Random();
        private final Road road;

        public TrainB(Road road) {
            this.road = road;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(random.nextInt(5));
                } catch (InterruptedException ignored) {
                }

                road.takeRoadB();
            }
        }
    }

    private static class Road {
        private final Object roadA = new Object();
        private final Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A locked by train " + Thread.currentThread().getName());
                synchronized (roadB) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored) {
                    }
                    System.out.println("Road B locked by train " + Thread.currentThread().getName());
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadB) {
                System.out.println("Road B locked by train " + Thread.currentThread().getName());
                synchronized (roadA) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored) {
                    }
                    System.out.println("Road A locked by train " + Thread.currentThread().getName());
                }
            }

            // Deadlock can be fixed by keeping the same lock ordering
//            synchronized (roadA) {
//                System.out.println("Road B locked by train " + Thread.currentThread().getName());
//                synchronized (roadB) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException ignored) {
//                    }
//                    System.out.println("Road A locked by train " + Thread.currentThread().getName());
//                }
//            }
        }
    }
}
