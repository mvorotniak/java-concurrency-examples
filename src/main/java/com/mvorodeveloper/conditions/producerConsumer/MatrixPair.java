package com.mvorodeveloper.conditions.producerConsumer;

/**
 * Pair of matrices that will be used for different calculations
 */
public class MatrixPair {

    private final float[][] matrix1;
    private final float[][] matrix2;

    public MatrixPair(float[][] matrix1, float[][] matrix2) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    public float[][] getMatrix1() {
        return matrix1;
    }

    public float[][] getMatrix2() {
        return matrix2;
    }
}
