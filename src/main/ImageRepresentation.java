package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

public abstract class ImageRepresentation implements Cloneable {

    protected int width;
    protected int height;

    public ImageRepresentation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract BufferedImage toImage();

    public static int absoluteDifference(BufferedImage img1, BufferedImage img2) {
        assert img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight();
        int diff = 0;
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                int pixelDiff = Math.abs((rgb1 & 0xff) - (rgb2 & 0xff)) // blue
                        + Math.abs(((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8) // green
                        + Math.abs(((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16); // red
                diff += pixelDiff;
            }
        }
        return diff;
    }

    public void save() {
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.getHour() + "_" + now.getMinute() + "_" + now.getSecond();
        File outFile = new File(GUI.imageOutputPath + "output_" + currentTime + ".png");
        try {
            ImageIO.write(this.toImage(), "png", outFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
