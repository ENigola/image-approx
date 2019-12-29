package main.triangle;

import main.Evolution;
import main.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TriangleEvolution extends Evolution {

    private static int populationSize = 1;
    private static int triangleCount = 50;
    private static int childCount = 1;
    private static int maxNew = 0;
    private static int maxChanges = 3;
    private static double colorChangeProb = 0.3;

    private List<TriangleImage> population;

    public TriangleEvolution(BufferedImage originalImage, GUI gui, int generations, int maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, generations, maxNoChangeGenerations, displayFreq);
    }


    @Override
    protected void displayTop() {
        TriangleImage top = chooseFittest(1, population).get(0);
        gui.setCreatedImage(top.toImage());
    }

    protected void nextGeneration() {
        List<TriangleImage> newInstances = new ArrayList<>(population);
        for (TriangleImage parent : population) {
            for (int i = 0; i < childCount; i++) {
                TriangleImage clone = parent.clone();
                mutate(clone);
                newInstances.add(clone);
            }
        }
        population = chooseFittest(populationSize, newInstances);
    }

    private void mutate(TriangleImage triangleImage) {
        List<Triangle> triangles = triangleImage.getTriangles();
        int nNew = random.nextInt(maxNew + 1);
        for (int i = 0; i < nNew; i++) {
            triangles.remove(random.nextInt(triangles.size()));
            triangles.add(randomTriangle());
        }
        int nChanges = random.nextInt(maxChanges + 1);
        for (int i = 0; i < nChanges; i++) {
            Triangle triangle = triangles.get(random.nextInt(triangles.size()));
            mutateTriangle(triangle);
        }
    }

    private void mutateTriangle(Triangle triangle) {
        double p = random.nextDouble();
        if (p < colorChangeProb) {
            triangle.setColor(new Color(random.nextInt(256), random.nextInt(256),
                    random.nextInt(256), random.nextInt(256)));
        } else {
            int pos = random.nextInt(3);
            triangle.getX()[pos] = random.nextInt(originalImage.getWidth());
            triangle.getY()[pos] = random.nextInt(originalImage.getHeight());
        }
    }

    @Override
    protected void initializePopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Triangle> triangles = new ArrayList<>(triangleCount);
            for (int j = 0; j < triangleCount; j++) {
                triangles.add(randomTriangle());
            }
            TriangleImage triangleImage = new TriangleImage(originalImage.getWidth(), originalImage.getHeight(), triangles);
            population.add(triangleImage);
        }
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
