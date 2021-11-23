package com.mvorodeveloper.threadCoordination;

import java.math.BigInteger;

/**
 * Handles the interrupt signal explicitly
 */
public class InterruptedLongCalculation {

    public static void main(String[] args) throws InterruptedException {
        LongCalculation longCalculation = new LongCalculation(new BigInteger("2000000"), new BigInteger("300000000"));

        //longCalculation.setDaemon(true); - this is another way of stopping the thread instead of explicitly interrupting
        longCalculation.start();
        Thread.sleep(100);
        longCalculation.interrupt();
    }

    private static class LongCalculation extends Thread {
        private final BigInteger base;
        private final BigInteger power;

        public LongCalculation(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if (this.isInterrupted()) {
                    System.out.println("The calculation was interrupted at result " + result);
                    return result;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
