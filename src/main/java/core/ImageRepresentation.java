package core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public abstract class ImageRepresentation implements Cloneable {

    protected static Color disallowed = new Color(254,254,254);
    protected int width;
    protected int height;

    public ImageRepresentation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract BufferedImage toImage();

    public static int absoluteDifference(BufferedImage img1, BufferedImage img2) {
        int diff = 0;
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if ((img2.getRGB(x, y) & 0xffffff) == (disallowed.getRGB() & 0xffffff)) { // to fix a problem in the external library
                    return Integer.MAX_VALUE;
                }
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

    public void save(int generation) {
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.getHour() + "_" + now.getMinute() + "_" + now.getSecond();
        File outFile = new File(GUI.imageOutputPath + "output_gen_" + generation + "_time_" + currentTime + ".png");
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
