package triangle;

import core.BinaryEvolution;
import core.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SimpleTriangleEvolution extends BinaryEvolution<TriangleImage> {

    private static int triangle_count = 100;

    private static int maxChanges = 2;
    private static double colorChangeProb = 0.3;
    private static double oneVertexChangeProb = 0.3;
    private static double anotherVertexChangeProb = 0.2;
    private static double maxMoveRatio = 0.2;
    private static int maxColorChange = 30;

    private int maxMoveX;
    private int maxMoveY;

    public SimpleTriangleEvolution(BufferedImage originalImage, GUI gui, Integer generations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, generations, maxNoChangeGenerations, displayFreq);
        this.maxMoveX = (int) (originalImage.getWidth() * maxMoveRatio) + 1;
        this.maxMoveY = (int) (originalImage.getHeight() * maxMoveRatio) + 1;
    }

    @Override
    protected void initializePopulation() {
        List<Triangle> triangles = new ArrayList<>(triangle_count);
        for (int i = 0; i < triangle_count; i++) {
            triangles.add(randomTriangle());
        }
        current = new TriangleImage(originalImage.getWidth(), originalImage.getHeight(), triangles);
    }

    @Override
    protected void mutate(TriangleImage image) {
        int nChanges = random.nextInt(maxChanges) + 1;
        for (int i = 0; i < nChanges; i++) {
            Triangle triangle = image.getTriangles().get(random.nextInt(triangle_count));
            boolean changeColor = random.nextDouble() < colorChangeProb;
            boolean changeOneVertex = !changeColor || random.nextDouble() < oneVertexChangeProb;
            boolean changeAnotherVertex = changeOneVertex && random.nextDouble() < anotherVertexChangeProb;
            if (changeColor) {
                triangle.setColor(mutateColor(triangle.getColor(), maxColorChange, true, false));
            }
            List<Integer> changePos = new ArrayList<>();
            if (changeOneVertex && !changeAnotherVertex) {
                changePos.add(random.nextInt(3));
            } else if (changeAnotherVertex) {
                changePos.add(0);
                changePos.add(1);
                changePos.add(2);
                changePos.remove(random.nextInt(3));
            }
            for (Integer pos : changePos) {
                mutateVertex(triangle, pos);
            }
        }
    }

    private void mutateVertex(Triangle triangle, int pos) {
        triangle.getX()[pos] = bound(triangle.getX()[pos] + doubleRand(maxMoveX), 0, originalImage.getWidth());
        triangle.getY()[pos] = bound(triangle.getY()[pos] + doubleRand(maxMoveY), 0, originalImage.getHeight());
    }

    private Triangle randomTriangle() {
        int[] x = new int[3];
        int[] y = new int[3];
        for (int i = 0; i < 3; i++) {
            x[i] = random.nextInt(originalImage.getWidth());
            y[i] = random.nextInt(originalImage.getHeight());
        }
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256),
                random.nextInt(226) + 30);
        return new Triangle(x, y, color);
    }
}
