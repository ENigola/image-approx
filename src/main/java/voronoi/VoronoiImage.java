package voronoi;

import com.sun.org.apache.bcel.internal.generic.DREM;
import core.ImageRepresentation;
import de.alsclo.voronoi.Voronoi;
import de.alsclo.voronoi.graph.Edge;
import de.alsclo.voronoi.graph.Graph;
import de.alsclo.voronoi.graph.Point;
import de.alsclo.voronoi.graph.Vertex;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.*;
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
        //if (true) { // TODO: do something about this
        //    return toImageNaive();
        //}
        Collection<Point> voronoiPoints = new ArrayList<>(points.size() + 4);
        for (ColorPoint colorPoint : points) {
            voronoiPoints.add(new Point(colorPoint.getX(), colorPoint.getY()));
        }
        // points outside image for bounding
        int extraDist = width + height;
        voronoiPoints.add(new Point(width / 2.0,  - extraDist)); // above
        voronoiPoints.add(new Point(width / 2.0, height + extraDist)); // below
        voronoiPoints.add(new Point(- extraDist, height / 2.0)); // left
        voronoiPoints.add(new Point(width + extraDist, height / 2.0)); // right

        Voronoi voronoi = new Voronoi(voronoiPoints);
        Graph graph = voronoi.getGraph();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        for (ColorPoint colorPoint : points) {
            graphics.setColor(colorPoint.getColor());
            Point current = new Point(colorPoint.getX(), colorPoint.getY());
            List<Edge> edges = new ArrayList<>();
            for (Point point : graph.getSitePoints()) {
                Optional<Edge> edge = graph.getEdgeBetweenSites(current, point);
                edge.ifPresent(edges::add);
            }
            int[][] xy = consecutivePoints(edges); /// TODO: PROBLEM
            graphics.fillPolygon(xy[0], xy[1], xy[0].length);
            // ------------ // TODO: remove
            //graphics.setColor(Color.BLACK);
            //int radius = 3;
            //graphics.fill(new Ellipse2D.Double(colorPoint.getX() - radius, colorPoint.getY() - radius, 2*radius, 2*radius));
            // ----------
        }
        return image;
    }

    // empties the input list
    private int[][] consecutivePoints(List<Edge> edges) {
        // remove edges of length 0
        List<Edge> toRemove = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getA() == null) {
                if (edge.getB() == null) {
                    toRemove.add(edge);
                }
            } else {
                if (edge.getA().equals(edge.getB())) {
                    toRemove.add(edge);
                }
            }
        }
        edges.removeAll(toRemove);
        List<Vertex> vertices = new ArrayList<>(edges.size() + 1);
        if (edges.size() == 0) {
            return new int[][] {new int[] {}, new int[] {}};
        }
        vertices.add(edges.get(0).getA());
        vertices.add(edges.get(0).getB());
        Vertex searchFor = edges.remove(0).getB();
        while (!edges.isEmpty()) {
            boolean updated = false;
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                if (searchFor == null) {
                    if (edge.getA() == null) {
                        searchFor = edge.getB();
                    } else if (edge.getB() == null) {
                        searchFor = edge.getA();
                    } else {
                        continue;
                    }
                    vertices.remove(vertices.size() - 1);
                    updated = true;
                    vertices.add(searchFor);
                    edges.remove(edge);
                    break;
                }

                if (searchFor.equals(edge.getA()) || searchFor.equals(edge.getB())) {
                    if (searchFor.equals(edge.getA())) {
                        searchFor = edge.getB();
                    } else {
                        searchFor = edge.getA();
                    }
                    vertices.add(searchFor);
                    edges.remove(i);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                break;
            }
        }
        assert vertices.get(0).equals(vertices.get(vertices.size() - 1));
        vertices.remove(vertices.size() - 1);
        // to arrays
        int[] xs = new int[vertices.size()];
        int[] ys = new int[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            xs[i] = (int) Math.round(vertices.get(i).getLocation().x);
            ys[i] = (int) Math.round(vertices.get(i).getLocation().y);
        }
        return new int[][] {xs, ys};
    }

    public BufferedImage toImageNaive() {
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
