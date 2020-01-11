package line;

import core.ImageRepresentation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LineImage extends ImageRepresentation {

    private static int rgbMask = 0x00ffffff; // for excluding alpha value TODO: is it necessary?
    private static Color initialColor = Color.BLACK;

    // order: left to right
    private Color[] vertical;
    // order: top to bottom
    private Color[] horizontal;
    // order: bottom left to top left to top right
    private Color[] primaryDiagonal; // top left to bottom right direction
    // order: top left to bottom left to bottom right
    private Color[] secondaryDiagonal; // bottom left to top right direction

    public LineImage(int width, int height) {
        super(width, height);
        vertical = new Color[width];
        for (int i = 0; i < width; i++) {
            vertical[i] = initialColor;
        }
        horizontal = new Color[height];
        for (int i = 0; i < height; i++) {
            horizontal[i] = initialColor;
        }
        primaryDiagonal = new Color[width + height - 1];
        for (int i = 0; i < width + height - 1; i++) {
            primaryDiagonal[i] = initialColor;
        }
        secondaryDiagonal = new Color[width + height - 1];
        for (int i = 0; i < width + height - 1; i++) {
            secondaryDiagonal[i] = initialColor;
        }
    }

    public Color[] getVertical() {
        return vertical;
    }

    public Color[] getHorizontal() {
        return horizontal;
    }

    public Color[] getPrimaryDiagonal() {
        return primaryDiagonal;
    }

    public Color[] getSecondaryDiagonal() {
        return secondaryDiagonal;
    }

    @Override
    public BufferedImage toImage() {
        int[][] img = new int[width][];
        for (int x = 0; x < width; x++) {
            img[x] = new int[height];
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                img[x][y] = colorAverage(new Color[] {
                        vertical[x],
                        horizontal[y],
                        primaryDiagonal[height - 1 - y + x],
                        secondaryDiagonal[x + y]
                });
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, img[x][y]);
            }
        }
        return image;
    }

    private static int colorAverage(Color[] colors) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Color color : colors) {
            red += color.getRed();
            green += color.getGreen();
            blue += color.getBlue();
        }
        red /= colors.length;
        green /= colors.length;
        blue /= colors.length;
        return (red << 16) + (green << 8) + blue;
    }

    public LineImage clone() {
        LineImage clone = (LineImage) super.clone();
        clone.vertical = this.vertical.clone();
        clone.horizontal = this.horizontal.clone();
        clone.primaryDiagonal = this.primaryDiagonal.clone();
        clone.secondaryDiagonal = this.secondaryDiagonal.clone();
        return clone;
    }
}
