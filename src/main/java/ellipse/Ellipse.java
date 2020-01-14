package ellipse;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Ellipse implements Cloneable {

    public int x;
    public int y;
    public int width;
    public int height;
    public int angle;
    public Color color;

    public Ellipse(int x, int y, int width, int height, int angle, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle; // [0,360)
        this.color = color;
    }

    public BufferedImage toImage() {
        int wh = width + height;
        if (wh == 0) {
            wh = 2;
        }
        BufferedImage image = new BufferedImage(wh, wh, BufferedImage.TYPE_INT_ARGB);
        Ellipse2D ellipse = new Ellipse2D.Double(height / 2.0, width / 2.0, width, height);
        Graphics2D graphics = image.createGraphics();
        graphics.rotate(Math.toRadians(angle), wh / 2.0, wh / 2.0);
        graphics.setColor(color);
        graphics.fill(ellipse);
        return image;
    }

    @Override
    public Ellipse clone() {
        try {
            return (Ellipse) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
