package main;

import main.triangle.SimpleTriangleEvolution;
import main.triangle.TriangleEvolution;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI {

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

    private void runEvolution() {
        BufferedImage image = getCurrentImage();
        String algorithmName = (String) representationSelect.getSelectedItem();
        assert algorithmName != null;
        if (evolutionThread != null && evolutionThread.isAlive()) {
            return;
        }
        Runnable runnable;
        if (algorithmName.equals("Simple Triangles")) {
            SimpleTriangleEvolution evolution = new SimpleTriangleEvolution(image, this, null, 10_000, 100);
            runnable = evolution::evolve;
        } else if (algorithmName.equals("Triangles")) {
            TriangleEvolution evolution = new TriangleEvolution(image, this, 100_000, 10_000, 10);
            runnable = evolution::evolve;
        } else if (algorithmName.equals("Voronoi")) {
            throw new NotImplementedException();
        } else {
            runnable = null;
        }
        evolutionThread = new Thread(runnable);
        evolutionThread.start();
    }

    private JComboBox<String> createRepresentationSelect() {
        String[] representations = {"Simple Triangles", "Triangles", "Voronoi"};
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
        createdLabel.setText("Generation " + generation);
        createdLabel.setBounds(20 + originalImageLabel.getWidth(), 70, originalImageLabel.getWidth(), 20);
    }

    public void setCreatedImage(BufferedImage createdImage) {
        createdImageLabel.setBounds(20 + originalImageLabel.getWidth(), 100,
                createdImage.getWidth(), createdImage.getHeight());
        createdImageLabel.setIcon(new ImageIcon(createdImage));
    }

    public static void main(String[] args) {
        new GUI();
    }
}