import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI {

    private static String imagesPath = "resources/images/";

    private JFrame frame;
    private JComboBox<String> representationSelect;
    private JComboBox<String > imageSelect;
    private JButton runButtion;

    private JLabel originalLabel;
    private JLabel originalImageLabel;
    private JLabel createdLabel;
    private JLabel createdImageLabel;


    public GUI() {
        frame = new JFrame("Evolutionary Image Approximation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        representationSelect = createRepresentationSelect();
        imageSelect = createImageSelect();
        runButtion = createRunButtion();

        originalLabel = new JLabel("Original");
        originalImageLabel = new JLabel();

        createdLabel = new JLabel();
        createdImageLabel = new JLabel();

        frame.add(representationSelect);
        frame.add(imageSelect);
        frame.add(runButtion);
        frame.add(originalImageLabel);
        frame.add(createdImageLabel);

        frame.setLayout(null);
        frame.setVisible(true);
    }

    private JComboBox<String> createRepresentationSelect() {
        String[] representations = {"Polygon", "Voronoi"};
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
        return runButton;
    }

    private void showOriginalImage(String filename) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(imagesPath + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        originalImageLabel.setBounds(10, 40, image.getWidth(), image.getHeight());
        originalImageLabel.setIcon(new ImageIcon(image));

        Image x = ((ImageIcon) originalImageLabel.getIcon()).getImage();
        System.out.println(x.equals(image));
    }

    public BufferedImage getCurrentImage() {
        ImageIcon imageIcon = (ImageIcon) originalImageLabel.getIcon();
        return (BufferedImage) imageIcon.getImage();
    }

    public static void main(String[] args) {
        new GUI();
    }
}
