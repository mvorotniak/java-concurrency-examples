package com.mvorodeveloper.conditions.producerConsumer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Example output:
 * ...
 * Queue size 5
 * Queue size 5
 * No more matrices to read from file.
 * Queue size 4
 * Queue size 3
 * Queue size 2
 * Queue size 1
 * There are no more matrices to consume. Terminating consumer...
 */
public class ProducerConsumerRunner {

    private static final String MATRICES_FILE_PATH = "src/main/resources/files/matrices.txt";
    private static final String MATRICES_RESULT_FILE_PATH = "src/main/resources/files/matrices-result.txt";

    public static void main(String[] args) throws IOException {
        MatricesFileCreator matricesFileCreator = new MatricesFileCreator();
        matricesFileCreator.createFileWithRandomMatrices(MATRICES_FILE_PATH);

        File fileInput = new File(MATRICES_FILE_PATH);
        File fileOutput = new File(MATRICES_RESULT_FILE_PATH);

        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();

        MatrixProducer matrixProducer = new MatrixProducer(new FileReader(fileInput), threadSafeQueue);
        MatrixConsumer matrixConsumer = new MatrixConsumer(new FileWriter(fileOutput), threadSafeQueue);

        matrixProducer.start();
        matrixConsumer.start();
    }
}
