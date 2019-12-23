import java.awt.image.BufferedImage;

public abstract class ImageRepresentation implements Cloneable {

    protected int width;
    protected int height;

    public ImageRepresentation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract BufferedImage toImage();

    public static int absoluteDifference(BufferedImage img1, BufferedImage img2) {
        assert img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight();
        int diff = 0;
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                int pixelDiff = ((rgb1 & 0xff) - (rgb2 & 0xff))
                        + ((rgb1 & 0xff00) - (rgb2 & 0xff00))
                        + ((rgb1 & 0xff0000) - (rgb2 & 0xff0000));
                diff += pixelDiff;
            }
        }
        return diff;
    }
}
