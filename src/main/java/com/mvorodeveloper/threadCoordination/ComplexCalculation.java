package com.mvorodeveloper.threadCoordination;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ComplexCalculation {

    public static void main(String[] args) {
        BigInteger result =
            calculateResult(new BigInteger("100000000000"), new BigInteger("10000000000000"), new BigInteger("2"), new BigInteger("4"));

        System.out.println("Summary amount of results is " + result);
    }

    public static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result = BigInteger.ZERO;

        List<PowerCalculatingThread> threads = Arrays.asList(
            new PowerCalculatingThread(base1, power1),
            new PowerCalculatingThread(base2, power2)
        );

        threads.forEach(thread -> thread.setDaemon(true));
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join(10);
            } catch (InterruptedException e) {
                System.out.println("Join interrupted for thread " + Thread.currentThread().getName());
            }
        });

        for (PowerCalculatingThread thread : threads) {
            result = result.add(thread.getResult());
        }

        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private final BigInteger base;
        private final BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
            System.out.println(base + "^" + power + " = " + result);
        }

        public BigInteger getResult() { return result; }
    }
}
