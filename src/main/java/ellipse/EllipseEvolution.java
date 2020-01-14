package ellipse;

import core.BinaryEvolution;
import core.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EllipseEvolution extends BinaryEvolution<EllipseImage> {

    private static int ellipseCount = 100;
    private static int maxChanges = 2;
    private static int maxColorChange = 30;
    private static double maxMoveRatio = 0.2;
    private static int maxAngleChange = 170;

    private static double colorChangeProb = 0.3;
    private static double widthHeightChangeProb = 0.3;
    private static double angleChangeProb = 0.3;
    private static double locationChangeProb = 0.3;

    private int maxMoveX;
    private int maxMoveY;

    public EllipseEvolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, maxGenerations, maxNoChangeGenerations, displayFreq);
        this.maxMoveX = (int) (originalImage.getWidth() * maxMoveRatio) + 1;
        this.maxMoveY = (int) (originalImage.getHeight() * maxMoveRatio) + 1;
    }

    @Override
    protected void initializePopulation() {
        List<Ellipse> ellipses = new ArrayList<>(ellipseCount);
        for (int i = 0; i < ellipseCount; i++) {
            Ellipse ellipse = new Ellipse(random.nextInt(originalImage.getWidth()),
                    random.nextInt(originalImage.getHeight()),
                    random.nextInt(originalImage.getWidth()),
                    random.nextInt(originalImage.getHeight()),
                    random.nextInt(360),
                    new Color(random.nextInt(256),
                            random.nextInt(256),
                            random.nextInt(256),
                            random.nextInt(226) + 30));
            ellipses.add(ellipse);
        }
        current = new EllipseImage(originalImage.getWidth(), originalImage.getHeight(), ellipses);
    }

    @Override
    protected void mutate(EllipseImage image) {
        int nChanges = random.nextInt(maxChanges) + 1;
        for (int i = 0; i < nChanges; i++) {
            Ellipse ellipse = image.getEllipses().get(random.nextInt(image.getEllipses().size()));
            if (random.nextDouble() < colorChangeProb) {
                ellipse.color = mutateColor(ellipse.color, maxColorChange, true, false);
            }
            if (random.nextDouble() < widthHeightChangeProb) {
                ellipse.width = bound(ellipse.width + doubleRand(maxMoveX), 0, originalImage.getWidth());
                ellipse.height = bound(ellipse.height + doubleRand(maxMoveY), 0, originalImage.getHeight());
            }
            if (random.nextDouble() < angleChangeProb) {
                ellipse.angle = bound(ellipse.angle + doubleRand(maxAngleChange), 0, 359);
            }
            if (random.nextDouble() < locationChangeProb) {
                ellipse.x = bound(ellipse.x + doubleRand(maxMoveX), 0, originalImage.getWidth());
                ellipse.y = bound(ellipse.y + doubleRand(maxMoveY), 0, originalImage.getHeight());
            }
        }
    }
}
