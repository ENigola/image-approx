package triangle;

import java.awt.*;

public class Triangle implements Cloneable {
    private int[] x;
    private int[] y;
    private Color color;

    public Triangle(int[] x, int[] y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Triangle clone() {
        Triangle clone;
        try {
            clone = (Triangle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        clone.x = x.clone();
        clone.y = y.clone();
        return clone;
    }
}
