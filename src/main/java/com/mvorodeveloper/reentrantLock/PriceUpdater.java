package com.mvorodeveloper.reentrantLock;

import java.util.Random;

public class PriceUpdater extends Thread {

    private final Random random = new Random();
    private final CryptoCurrencyContainer cryptoCurrencyContainer;

    public PriceUpdater(CryptoCurrencyContainer cryptoCurrencyContainer) {
        this.cryptoCurrencyContainer = cryptoCurrencyContainer;
    }

    @Override
    public void run() {
        while(true) {
            cryptoCurrencyContainer.getLock().lock();

            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                cryptoCurrencyContainer.setBitcoinPrice(random.nextDouble());
                cryptoCurrencyContainer.setEtherPrice(random.nextInt(100));
                cryptoCurrencyContainer.setBitcoinCashPrice(random.nextInt(100));
                cryptoCurrencyContainer.setLiteCoinPrice(random.nextInt(500));
                cryptoCurrencyContainer.setRipplePrice(random.nextInt(600));
            } finally {
                cryptoCurrencyContainer.getLock().unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
