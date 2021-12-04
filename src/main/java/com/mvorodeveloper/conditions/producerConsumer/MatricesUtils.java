package com.mvorodeveloper.conditions.producerConsumer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.StringJoiner;

/**
 * This utils class contains a bunch of methods for different operations with matrices
 */
public class MatricesUtils {

    // 10x10 matrix
    public static final int N = 10;

    /**
     * Saves a given matrix into a file with formatted, comma separated values
     * @param fileWriter the file to save the matrix
     * @param matrix random matrix
     */
    public static void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
        int matrixLength = matrix.length;

        for (float[] floats : matrix) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (int j = 0; j < matrixLength; j++) {
                stringJoiner.add(String.format(Locale.ENGLISH, "%.2f", floats[j]));
            }
            fileWriter.write(stringJoiner.toString());
            fileWriter.write("\n");
        }

        fileWriter.write("\n");
    }

    public static float[][] createMatrix() {
        int size = MatricesUtils.N;
        float[][] matrix = new float[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = createMatrixItem();
            }
        }
        return matrix;
    }

    private static float createMatrixItem() {
        Random random = new Random();
        return random.nextFloat() * random.nextInt(100);
    }

    /**
     * Performs a matrices multiplication
     * c{ij} = a{i1}*b{1j} + a{i2}*b{2j} + ... + a{in}*b{nj} = sum{k=0}^{n-1} a{ik}*b{kj}
     */
    public static float[][] multiplyMatrices(float[][] matrix1, float[][] matrix2) {
        float[][] resultMatrix = new float[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return resultMatrix;
    }
}
