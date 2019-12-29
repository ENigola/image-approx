package main.line;

import main.BinaryEvolution;
import main.Evolution;
import main.GUI;
import main.ImageRepresentation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LineEvolution extends BinaryEvolution<LineImage> {

    private static int maxChanges = 1;
    private static int maxColorChange = 100;

    public LineEvolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, maxGenerations, maxNoChangeGenerations, displayFreq);
    }

    @Override
    protected void initializePopulation() {
        // leave to default color
        current = new LineImage(originalImage.getWidth(), originalImage.getHeight());
    }

    @Override
    protected void mutate(LineImage image) {
        int changes = random.nextInt(maxChanges) + 1;
        for (int i = 0; i < changes; i++) {
            Color[] lines = new Color[][] {image.getVertical(), image.getHorizontal(),
                    image.getPrimaryDiagonal(), image.getSecondaryDiagonal()}[random.nextInt(4)];
            int pos = random.nextInt(lines.length);
            lines[pos] = mutateColor(lines[pos], maxColorChange, false, false);
        }
    }
}
