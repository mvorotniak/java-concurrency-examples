package com.mvorodeveloper.conditions.producerConsumer;

import java.io.FileReader;
import java.util.Scanner;

/**
 * Gets a matrix pair from a file, creates a {@link MatrixPair} object and adds it to the queue
 * for future consuming
 */
public class MatrixProducer extends Thread {

    private final Scanner scanner;
    private final ThreadSafeQueue threadSafeQueue;

    public MatrixProducer(FileReader fileReader, ThreadSafeQueue threadSafeQueue) {
        this.scanner = new Scanner(fileReader);
        this.threadSafeQueue = threadSafeQueue;
    }

    @Override
    public void run() {
        while (true) {
            float[][] matrix1 = readMatrix();
            float[][] matrix2 = readMatrix();

            if (matrix1 == null || matrix2 == null) {
                System.out.println("No more matrices to read from file. Terminating producer...");
                threadSafeQueue.terminate();
                return;
            }

            MatrixPair matrixPair = new MatrixPair(matrix1, matrix2);

            threadSafeQueue.add(matrixPair);
        }
    }

    private float[][] readMatrix() {
        int size = MatricesUtils.N;
        float[][] matrix = new float[size][size];

        for (int i = 0; i < size; i++) {
            if (!scanner.hasNext()) {
                // no more content to read from the file
                return null;
            }
            String[] lines = scanner.nextLine().split(",");
            for (int j = 0; j < size; j++) {
                matrix[i][j] = Float.parseFloat(lines[j]);
            }
        }

        scanner.nextLine();
        return matrix;
    }
}
