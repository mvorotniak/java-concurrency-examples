package com.mvorodeveloper.threadCreation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class represents a bank account for which there is a password.
 * The hackers try to guess the password to steal the money.
 * The police tries to stop the hackers, but the try is not every time successful.
 */
public class BankAccountHacking {

    private static final int MAX_PASSWORD_VALUE = 9999;

    public static void main(String[] args) {
        Random random = new Random();
        int randomPassword = random.nextInt(MAX_PASSWORD_VALUE);
        System.out.println("The random password is " + randomPassword);

        Vault vault = new Vault(randomPassword);

        List<Thread> threads = Arrays.asList(
            new AscendingHacker(vault),
            new DescendingHacker(vault),
            new Police()
        );

        threads.forEach(Thread::start);
    }

    private static class Vault {
        private final int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrect(int guess) {
            try {
                // stopping hackers for 5 milliseconds
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return password == guess;
        }
    }

    private static abstract class Hacker extends Thread {
        Vault vault;

        public Hacker(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Starting thread " + this.getName() + "...");
            super.start();
        }
    }

    private static class AscendingHacker extends Hacker {
        public AscendingHacker(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = 0; guess < MAX_PASSWORD_VALUE; guess++) {
                if (vault.isCorrect(guess)) {
                    System.out.println("Bank Account hacked by " + this.getName() + ". The password was " + guess);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHacker extends Hacker {
        public DescendingHacker(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = MAX_PASSWORD_VALUE; guess > 0; guess--) {
                if (vault.isCorrect(guess)) {
                    System.out.println("Bank Account hacked by " + this.getName() + ". The password was " + guess);
                    System.exit(0);
                }
            }
        }
    }

    private static class Police extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 10; i > 0; i--) {
                    Thread.sleep(1000);
                    System.out.println("The police will arrive in " + i + " seconds...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Gave over hackers!");
            System.exit(0);
        }
    }
}
