package com.mvorodeveloper.conditions.producerConsumer;

import java.io.FileWriter;
import java.io.IOException;

/**
 * MatrixPair consumer that gets and removes an element from the queue, makes a multiplication
 * and saves the results into a file
 */
public class MatrixConsumer extends Thread {

    private final FileWriter fileWriter;
    private final ThreadSafeQueue threadSafeQueue;

    public MatrixConsumer(FileWriter fileWriter, ThreadSafeQueue threadSafeQueue) {
        this.fileWriter = fileWriter;
        this.threadSafeQueue = threadSafeQueue;
    }

    @Override
    public void run() {
        while (true) {
            MatrixPair matrixPair = threadSafeQueue.remove();
            if (matrixPair == null) {
                System.out.println("There are no more matrices to consume. Terminating consumer...");
                break;
            }

            float[][] resultMatrix = MatricesUtils.multiplyMatrices(matrixPair.getMatrix1(), matrixPair.getMatrix2());

            try {
                MatricesUtils.saveMatrixToFile(fileWriter, resultMatrix);
            } catch (IOException ignored) {
            }
        }

        try {
            // flush and close the writes to be sure that the data was correctly written to the file system
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred while trying to close the file writer: " + e.getMessage());
        }
    }
}
