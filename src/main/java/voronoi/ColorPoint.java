package voronoi;

import java.awt.*;

public class ColorPoint {

    private int x;
    private int y;
    private Color color;

    public ColorPoint(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ColorPoint)) {
            return false;
        }
        ColorPoint otherColorPoint = (ColorPoint) other;
        return otherColorPoint.x == x && otherColorPoint.y == y && otherColorPoint.color.equals(color);
    }

}
