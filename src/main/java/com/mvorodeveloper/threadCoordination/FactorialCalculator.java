package com.mvorodeveloper.threadCoordination;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calculating factorials avoiding race conditions
 */
public class FactorialCalculator {

    public static void main(String[] args) {
        List<Long> inputNumbers = Arrays.asList(1000000000000L, 10L, 1L);

        List<InternalFactorialCalculator> threads = inputNumbers.stream()
            .mapToLong(inputNumber -> inputNumber)
            .mapToObj(InternalFactorialCalculator::new)
            .collect(Collectors.toList());

        // Setting the threads as Daemon so they are terminated once all the work is done
        threads.forEach(thread -> thread.setDaemon(true));
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                // After 2 seconds we give up calculating the factorial
                thread.join(2000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted join for thread " + Thread.currentThread().getName());
            }
        });

        threads.forEach(thread -> {
            System.out.println(thread.isFinished()
                ? "Factorial of " + thread.getInputNumber() + " is " + thread.getResult()
                : "The calculation of " + thread.getInputNumber() + " took too long. Giving up...");
        });
    }

    private static class InternalFactorialCalculator extends Thread{
        private final long inputNumber;
        private boolean isFinished = false;
        private BigInteger result = BigInteger.ONE;

        public InternalFactorialCalculator(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            result = factorial(inputNumber);
            isFinished = true;
        }

        private BigInteger factorial(long inputNumber) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = inputNumber; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public long getInputNumber() {
            return inputNumber;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
