package com.mvorodeveloper.atomicOperations;

/**
 * Thread safe metrics class
 */
public class MinMaxMetricsApp {

    public static void main(String[] args) {
        MinMaxMetrics metrics = new MinMaxMetrics();

        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);

        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        metricsPrinter.start();
    }

    private static class MetricsPrinter extends Thread {
        private final MinMaxMetrics metrics;

        public MetricsPrinter(MinMaxMetrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                System.out.println("MIN_METRIC=" + metrics.getMin() + ", MAX_METRIC=" + metrics.getMax());
            }
        }
    }

    private static class BusinessLogic extends Thread {
        private final MinMaxMetrics metrics;

        public BusinessLogic(MinMaxMetrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
                long end = System.currentTimeMillis();
                metrics.addSample(end - start);
            }
        }
    }

    private static class MinMaxMetrics {
        private volatile long min = 0;
        private volatile long max = 0;

        /**
         * Adds a new sample to our metrics.
         */
        public synchronized void addSample(long newSample) {
            min = (min == 0) ? newSample : Math.min(min, newSample);
            max = Math.max(max, newSample);
        }

        /**
         * Returns the smallest sample we've seen so far.
         */
        public long getMin() {
            return min;
        }

        /**
         * Returns the biggest sample we've seen so far.
         */
        public long getMax() {
            return max;
        }
    }
}
