package com.mvorodeveloper.conditions.producerConsumer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creates a file with (MATRIX_PAIRS * 2) random matrices
 */
public class MatricesFileCreator {

    // 10,000x2 matrices
    private static final int MATRIX_PAIRS = 10_000;

    public void createFileWithRandomMatrices(String path) throws IOException {
        // Creating a new file
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file);

        // Creating and saving matrices to the previously created file
        createAndSaveMatrices(fileWriter);

        // Write the content of the buffer to the destination and empty it. Also close the stream permanently
        fileWriter.flush();
        fileWriter.close();
    }

    private void createAndSaveMatrices(FileWriter fileWriter) throws IOException {
        for (int i = 0; i < MATRIX_PAIRS * 2; i++) {
            float[][] matrix = MatricesUtils.createMatrix();
            MatricesUtils.saveMatrixToFile(fileWriter, matrix);
        }
    }
}
