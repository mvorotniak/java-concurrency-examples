package com.mvorodeveloper.latencyOptimization;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Finds all the shade of grey pixels and changes the color to purple in a single or multiple threaded way
 */
public class PictureRecolor {

    public static final String SOURCE_FILE = "src/main/resources/pictures/many-flowers.jpg";
    public static final String DESTINATION_FILE = "src/main/resources/pictures/many-flowers-recolored-singleThread.jpg";
    public static final String DESTINATION_FILE_2 = "src/main/resources/pictures/many-flowers-recolored-multiThread.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));

        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long start = System.currentTimeMillis();
        recolorSingleThreaded(originalImage, resultImage);
        long end = System.currentTimeMillis();
        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);
        System.out.println("Image recolored in " + (end - start) + "millis - single thread.");

        long start2 = System.currentTimeMillis();
        int numOfThreads = 6;
        recolorMultiThreaded(originalImage, resultImage, numOfThreads);
        long end2 = System.currentTimeMillis();
        File outputFile2 = new File(DESTINATION_FILE_2);
        ImageIO.write(resultImage, "jpg", outputFile2);
        System.out.println("Image recolored in " + (end2 - start2) + "millis - " + numOfThreads + " threads.");
    }

    private static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numOfThreads) {
        List<Thread> threads = new ArrayList<>();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numOfThreads;

        for (int i = 0; i < numOfThreads; i++) {
            int leftCorner = 0;
            int topCorner = height * i;

            Thread thread = new Thread(() -> {
                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted join for thread " + Thread.currentThread().getName());
            }
        });
    }

    private static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    /**
     * Iterates through every pixel and performs recoloring
     */
    private static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner,
        int width, int height) {

        for(int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for(int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x , y);
            }
        }
    }

    /**
     * Given an image, changes a pixel of a shade of gray that is in coordinates x,y to purple color
     */
    private static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y); // get the rgb for a particular pixel

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed, newGreen, newBlue;

        if (isShadeOfGrey(red, green, blue)) {
            // if the pixel is of a shade of grey then change the color to violet
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRgb = createRgbFromColors(newRed, newGreen, newBlue);
        resultImage.setRGB(x, y, newRgb);
    }

    /**
     * Determine if the pixel is a shade of grey
     */
    private static boolean isShadeOfGrey(int red, int green, int blue) {
        // check if the three components have the same color intensity (if yes - then it's a shade of grey)
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    private static int createRgbFromColors(int red, int green, int blue) {
        int rgb = 0;

        // setting the red, green and blue value
        rgb |= red << 16;
        rgb |= green << 8;
        rgb |= blue;

        // setting the alpha value
        rgb |= 0xFF000000;

        return rgb;
    }

    /**
     * Extract the red component from a number that represents a color in aRGB.
     * Uses `bitwise and` to zero all bits of rgb except those from the byte containing the red component
     */
    private static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    /**
     * Extract the green component from a number that represents a color in aRGB.
     * Uses `bitwise and` to zero all bits of rgb except those from the byte containing the green component
     */
    private static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    /**
     * Extract the blue component from a number that represents a color in aRGB.
     * Uses `bitwise and` to zero all bits of rgb except those from the byte containing the blue component
     */
    private static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
