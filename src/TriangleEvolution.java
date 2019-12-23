import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TriangleEvolution extends Evolution {

    Random random = new Random(); // TODO: add seed?

    private static int iterations = 10; // TODO
    private static int populationSize = 5;
    private static int triangleCount = 5; // TODO: to 50
    private static int childCount = 5;
    private static int maxNew = 5;
    private static int maxChanges = 20;
    private static double colorChangeProb = 0.5;
    private static int displayFreq = 1; // TODO

    private List<TriangleImage> population;

    public TriangleEvolution(BufferedImage originalImage, GUI gui) {
        super(originalImage, gui);
    }

    public void evolve() {
        initializePopulation();
        for (int i = 0; i < iterations; i++) {
            nextGeneration();
            if (i % displayFreq == 0) {
                displayTop(i + 1);
            }
        }
    }

    private void displayTop(int generation) {
        TriangleImage top = chooseFittest(1, population).get(0);
        gui.setCreatedImage(top.toImage(), generation);
    }

    private void nextGeneration() {
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
        int nNew = random.nextInt(maxNew);
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

    private void initializePopulation() {
        population = new ArrayList<>(25); // TODO: change
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
