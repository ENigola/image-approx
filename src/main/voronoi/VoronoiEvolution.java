package main.voronoi;

import main.BinaryEvolution;
import main.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VoronoiEvolution extends BinaryEvolution<VoronoiImage> {

    private static int pointCount = 100;
    private static double colorChangeProb = 0.5;
    private static int maxChanges = 1;
    private static int maxColorChange = 50;
    private static boolean mutateAllColors = true;
    private static double maxMoveRatio = 0.1;

    private int maxMoveX;
    private int maxMoveY;

    public VoronoiEvolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, maxGenerations, maxNoChangeGenerations, displayFreq);
        this.maxMoveX = (int) (originalImage.getWidth() * maxMoveRatio) + 1;
        this.maxMoveY = (int) (originalImage.getHeight() * maxMoveRatio) + 1;
    }

    @Override
    protected void initializePopulation() {
        current = new VoronoiImage(originalImage.getWidth(), originalImage.getHeight());
        for (int i = 0; i < pointCount; i++) {
            int x = random.nextInt(originalImage.getWidth());
            int y = random.nextInt(originalImage.getHeight());
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            current.getPoints().add(new ColorPoint(x, y, color));
        }
    }

    @Override
    protected void mutate(VoronoiImage image) {
        int nChanges = random.nextInt(maxChanges) + 1;
        for (int i = 0; i < nChanges; i++) {
            int pos = random.nextInt(image.getPoints().size());
            ColorPoint oldPoint = image.getPoints().get(pos);
            ColorPoint newPoint;
            if (random.nextDouble() < colorChangeProb) {
                Color newColor = mutateColor(oldPoint.getColor(), maxColorChange, false, mutateAllColors);
                newPoint = new ColorPoint(oldPoint.getX(), oldPoint.getY(), newColor);
            } else {
                int newX = bound(oldPoint.getX() + doubleRand(maxMoveX), 0, originalImage.getWidth());
                int newY = bound(oldPoint.getY() + doubleRand(maxMoveY), 0, originalImage.getHeight());
                newPoint = new ColorPoint(newX, newY, oldPoint.getColor());
            }
            image.getPoints().set(pos, newPoint);
        }
    }
}
