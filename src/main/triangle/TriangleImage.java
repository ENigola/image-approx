package main.triangle;

import main.ImageRepresentation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TriangleImage extends ImageRepresentation implements Cloneable {

    private List<Triangle> triangles;

    public TriangleImage(int width, int height, List<Triangle> triangles) {
        super(width, height);
        this.triangles = triangles;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK); // make initial background black
        graphics.fillRect(0, 0, width, height);
        for (Triangle triangle : triangles) {
            graphics.setColor(triangle.getColor());
            graphics.fillPolygon(triangle.getX(), triangle.getY(), 3);
        }
        return image;
    }

    public TriangleImage clone() {
        TriangleImage clone = (TriangleImage) super.clone();
        List<Triangle> trianglesCopy = new ArrayList<>(triangles.size());
        for (Triangle triangle : triangles) {
            trianglesCopy.add(triangle.clone());
        }
        clone.triangles = trianglesCopy;
        return clone;
    }

}
