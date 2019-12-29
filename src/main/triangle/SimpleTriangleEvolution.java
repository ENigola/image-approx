package main.triangle;

import main.Evolution;
import main.GUI;
import main.ImageRepresentation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SimpleTriangleEvolution extends Evolution {

    private static int triangle_count = 50;
    private static int maxChanges = 3;
    private static double colorChangeProb = 0.5;
    private static double maxMoveRatio = 0.3;
    private static int maxColorChange = 10;

    private int maxMoveX;
    private int maxMoveY;

    private TriangleImage current;

    public SimpleTriangleEvolution(BufferedImage originalImage, GUI gui, Integer generations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, generations, maxNoChangeGenerations, displayFreq);
        this.maxMoveX = (int) (originalImage.getWidth() * maxMoveRatio) + 1;
        this.maxMoveY = (int) (originalImage.getHeight() * maxMoveRatio) + 1;
    }

    @Override
    protected void displayTop() {
        gui.setCreatedImage(current.toImage());
    }

    @Override
    protected void initializePopulation() {
        List<Triangle> triangles = new ArrayList<>(triangle_count);
        for (int i = 0; i < triangle_count; i++) {
            triangles.add(randomTriangle());
        }
        current = new TriangleImage(originalImage.getWidth(), originalImage.getHeight(), triangles);
        currentLoss = ImageRepresentation.absoluteDifference(originalImage, current.toImage());
    }

    @Override
    protected void nextGeneration() {
        TriangleImage newImage = current.clone();
        int changes = random.nextInt(maxChanges) + 1;
        for (int i = 0; i < changes; i++) {
            Triangle triangle = newImage.getTriangles().get(random.nextInt(triangle_count));
            if (random.nextDouble() < colorChangeProb) {
                mutateColor(triangle);
            } else {
                mutateVertex(triangle);
            }
        }
        int newLoss = ImageRepresentation.absoluteDifference(originalImage, newImage.toImage());
        if (newLoss < currentLoss) {
            currentLoss = newLoss;
            current = newImage;
        }
    }

    private void mutateColor(Triangle triangle) {
        Color oldColor = triangle.getColor();
        Color newColor = new Color(
                bound(oldColor.getRed() + doubleRand(maxColorChange), 0, 255),
                bound(oldColor.getGreen() + doubleRand(maxColorChange), 0, 255),
                bound(oldColor.getBlue() + doubleRand(maxColorChange), 0, 255),
                bound(oldColor.getAlpha() + doubleRand(maxColorChange),0, 255)
        );
        triangle.setColor(newColor);
    }

    private void mutateVertex(Triangle triangle) {
        int pos = random.nextInt(3);
        int xChange = doubleRand(maxMoveX);
        int yChange = doubleRand(maxMoveY);
        triangle.getX()[pos] = bound(triangle.getX()[pos] + xChange, 0, originalImage.getWidth());
        triangle.getY()[pos] = bound(triangle.getY()[pos] + yChange, 0, originalImage.getHeight());
    }

    private int doubleRand(int n) {
        return random.nextInt(2 * n) - n;
    }

    private int bound(int n, int min, int max) {
        n = Math.max(n, min);
        return Math.min(n, max);
    }

    private Triangle randomTriangle() {
        int[] x = new int[3];
        int[] y = new int[3];
        for (int i = 0; i < 3; i++) {
            x[i] = random.nextInt(originalImage.getWidth());
            y[i] = random.nextInt(originalImage.getHeight());
        }
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        return new Triangle(x, y, color);
    }
}
