package main.voronoi;

import main.ImageRepresentation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VoronoiImage extends ImageRepresentation {

    List<ColorPoint> points;

    public VoronoiImage(int width, int height) {
        super(width, height);
        points = new ArrayList<>();
    }

    public List<ColorPoint> getPoints() {
        return points;
    }

    @Override
    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ColorPoint closest = findClosest(new int[] {x, y});
                image.setRGB(x, y, closest.getColor().getRGB());
            }
        }
        return image;
    }

    private ColorPoint findClosest(int[] point) {
        ColorPoint closest = points.get(0);
        double closestDistance = squareDistance(point, closest);
        for (ColorPoint colorPoint : points) {
            double distance = squareDistance(point, colorPoint);
            if (distance < closestDistance) {
                closest = colorPoint;
                closestDistance = distance;
            }
        }
        return  closest;
    }

    private double squareDistance(int[] point, ColorPoint colorPoint) {
        return Math.pow(point[0] - colorPoint.getX(), 2) + Math.pow(point[1] - colorPoint.getY(), 2);
    }

    public VoronoiImage clone() {
        VoronoiImage clone = (VoronoiImage) super.clone();
        clone.points = new ArrayList<>(this.points);
        return clone;
    }
}
