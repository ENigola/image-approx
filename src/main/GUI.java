package main;

import main.line.LineEvolution;
import main.triangle.SimpleTriangleEvolution;
import main.triangle.TriangleEvolution;
import main.voronoi.VoronoiEvolution;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI {

    public static String  imageOutputPath = "output/";
    private static String imagesPath = "resources/images/";

    private JFrame frame;
    private JComboBox<String> representationSelect;
    private JComboBox<String > imageSelect;
    private JButton runButtion;

    private JLabel infoLabel;
    private JLabel originalLabel;
    private JLabel originalImageLabel;
    private JLabel createdLabel;
    private JLabel createdImageLabel;

    private Thread evolutionThread;

    public GUI() {
        frame = new JFrame("Evolutionary Image Approximation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        representationSelect = createRepresentationSelect();
        imageSelect = createImageSelect();
        runButtion = createRunButtion();

        infoLabel = new JLabel();
        infoLabel.setBounds(10, 40, 400, 20);

        originalLabel = new JLabel("Original", SwingConstants.CENTER);
        originalImageLabel = new JLabel();

        createdLabel = new JLabel("", SwingConstants.CENTER);
        createdImageLabel = new JLabel();

        frame.add(representationSelect);
        frame.add(imageSelect);
        frame.add(runButtion);
        frame.add(infoLabel);
        frame.add(originalLabel);
        frame.add(originalImageLabel);
        frame.add(createdLabel);
        frame.add(createdImageLabel);

        frame.setLayout(null);
        frame.setVisible(true);

        showOriginalImage((String) imageSelect.getSelectedItem());
    }

    private JComboBox<String> createRepresentationSelect() {
        String[] representations = {"Voronoi", "Line", "Simple Triangles", "Triangles"}; // TODO: fix order
        JComboBox<String> representationSelect = new JComboBox<>(representations);
        representationSelect.setBounds(10, 10, 140, 20);
        return  representationSelect;
    }

    private JComboBox<String> createImageSelect() {
        String[] imageFilenames = new File(imagesPath).list();
        assert imageFilenames != null;
        JComboBox<String> imageSelect = new JComboBox<>(imageFilenames);
        imageSelect.setBounds(160, 10, 140, 20);
        imageSelect.addActionListener(event -> {
            JComboBox<String> imageComboBox = (JComboBox<String>) event.getSource();
            String filename = (String) imageComboBox.getSelectedItem();
            showOriginalImage(filename);
        });
        return imageSelect;
    }

    private JButton createRunButtion() {
        JButton runButton = new JButton("Run");
        runButton.setBounds(310, 10, 60, 20);
        runButton.addActionListener(event -> runEvolution());
        return runButton;
    }

    public void setInfoText(String info) {
        infoLabel.setText(info);
    }

    private void showOriginalImage(String filename) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(imagesPath + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        originalLabel.setBounds(10, 70, image.getWidth(), 20);
        originalImageLabel.setBounds(10, 100, image.getWidth(), image.getHeight());
        originalImageLabel.setIcon(new ImageIcon(image));
    }

    public BufferedImage getCurrentImage() {
        ImageIcon imageIcon = (ImageIcon) originalImageLabel.getIcon();
        return (BufferedImage) imageIcon.getImage();
    }

    public void setGeneration(int generation) {
        createdLabel.setText(String.format("Generation %,d", generation));
        createdLabel.setBounds(20 + originalImageLabel.getWidth(), 70, Math.max(originalImageLabel.getWidth(), 120), 20);
    }

    public void setCreatedImage(BufferedImage createdImage) {
        createdImageLabel.setBounds(20 + originalImageLabel.getWidth(), 100,
                createdImage.getWidth(), createdImage.getHeight());
        createdImageLabel.setIcon(new ImageIcon(createdImage));
    }

    private void runEvolution() {
        BufferedImage image = getCurrentImage();
        String algorithmName = (String) representationSelect.getSelectedItem();
        assert algorithmName != null;
        if (evolutionThread != null && evolutionThread.isAlive()) {
            return;
        }
        Evolution evolution;
        if (algorithmName.equals("Simple Triangles")) {
            evolution = new SimpleTriangleEvolution(image, this, null, 10_000, 100);
        } else if (algorithmName.equals("Triangles")) {
            evolution = new TriangleEvolution(image, this, 100_000, 10_000, 10);
        } else if (algorithmName.equals("Voronoi")) {
            evolution = new VoronoiEvolution(image, this, 1_000_000, 10_000, 10);
        } else if (algorithmName.equals("Line")) {
            evolution = new LineEvolution(image, this, 1_000_000, 1_000_000, 10);
        } else {
            return;
        }
        Runnable runnable = evolution::evolve;
        evolutionThread = new Thread(runnable);
        evolutionThread.start();
    }

    public static void main(String[] args) {
        new GUI();
    }
}
