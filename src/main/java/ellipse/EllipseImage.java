package ellipse;

import core.ImageRepresentation;
import voronoi.VoronoiImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EllipseImage extends ImageRepresentation {

    private List<Ellipse> ellipses;

    public EllipseImage(int width, int height, List<Ellipse> ellipses) {
        super(width, height);
        this.ellipses = ellipses;
    }

    public List<Ellipse> getEllipses() {
        return ellipses;
    }

    @Override
    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK); // make initial background black
        graphics.fillRect(0, 0, width, height);
        for (Ellipse ellipse : ellipses) {
            BufferedImage ellipseImage = ellipse.toImage();
            int x = ellipse.x - ellipseImage.getWidth() / 2;
            int y = ellipse.y - ellipseImage.getHeight() / 2;
            graphics.drawImage(ellipseImage, x, y, null);
        }
        return image;
    }

    @Override
    public EllipseImage clone() {
        EllipseImage clone = (EllipseImage) super.clone();
        clone.ellipses = new ArrayList<>(ellipses.size());
        for (Ellipse ellipse : ellipses) {
            clone.ellipses.add(ellipse.clone());
        }
        return clone;
    }
}
