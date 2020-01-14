package voronoi;

import core.BinaryEvolution;
import core.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VoronoiEvolution extends BinaryEvolution<VoronoiImage> {

    private static int pointCount = 300;
    private static int maxChanges = 1;
    private static int maxColorChange = 30;
    private static boolean mutateAllColors = false;
    private static double maxMoveRatio = 0.05;
    private static double colorChangeProb = 0.3;
    private static double locationChangeProb = 0.3;

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
            ColorPoint newPoint = null;
            boolean changeColor = random.nextDouble() < colorChangeProb;
            boolean changeLocation = !changeColor || random.nextDouble() < locationChangeProb;
            if (changeColor) {
                Color newColor = mutateColor(oldPoint.getColor(), maxColorChange, false, mutateAllColors);
                newPoint = new ColorPoint(oldPoint.getX(), oldPoint.getY(), newColor);
            }
            if (changeLocation) {
                int extra = random.nextDouble() < 0.1 ? (originalImage.getWidth() + originalImage.getHeight()) / 4 : 0;
                int newX = bound(oldPoint.getX() + doubleRand(maxMoveX + extra), 0, originalImage.getWidth());
                int newY = bound(oldPoint.getY() + doubleRand(maxMoveY + extra), 0, originalImage.getHeight());
                newPoint = new ColorPoint(newX, newY, oldPoint.getColor());
            }
            image.getPoints().set(pos, newPoint);
        }
    }
}
