package core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public abstract class Evolution {

    protected Random random;
    protected BufferedImage originalImage;
    protected GUI gui;

    private Integer maxGenerations;
    private Integer maxNoChangeGenerations;
    private int displayFreq;

    protected int currentLoss;
    private int noChangeGenerations = 0;
    protected int generation = 1;
    public boolean running = true;

    public Evolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        this.random = new Random();
        this.originalImage = originalImage;
        this.gui = gui;
        this.maxGenerations = maxGenerations;
        this.maxNoChangeGenerations = maxNoChangeGenerations;
        this.displayFreq = displayFreq;
    }

    public void evolve() {
        initializePopulation();
        currentLoss = getCurrentLoss();
        while (running) {
            checkSleep();
            if (saveCondition(generation)) {
                getTop().save(generation);
            }
            if (maxGenerations != null && generation > maxGenerations) {
                gui.setInfoText("" + maxGenerations + " generations finished.");
                break;
            }
            int oldLoss = currentLoss;
            nextGeneration();
            if (generation % displayFreq == 0) {
                gui.setGeneration(generation);
                // debug print
                System.out.println(String.format("Current loss: %d, no improvement generations: %d", currentLoss, noChangeGenerations));
                displayTop();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e);
                    System.exit(-789);
                }
            }
            if (currentLoss == oldLoss) {
                noChangeGenerations++;
            } else {
                noChangeGenerations = 0;
            }
            if (maxNoChangeGenerations != null && noChangeGenerations > maxNoChangeGenerations) {
                gui.setInfoText("No improvement for " + noChangeGenerations + " generations.");
                break;
            }
            if (currentLoss == 0) {
                gui.setInfoText("Loss 0 reached at generation " + generation + ".");
                break;
            }
            generation++;
        }
        gui.setGeneration(generation);
        displayTop();
    }

    private static int sleepAfter = 3000;
    private static int sleepFor = 1000;
    private long lastSleep = System.currentTimeMillis();

    private void checkSleep() {
        if (System.currentTimeMillis() - lastSleep > sleepAfter) {
            try {
                Thread.sleep(sleepFor);
                lastSleep = System.currentTimeMillis();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // non-100k save points
    private static List<Integer> saveGenerations = Arrays.asList(
            1,
            1_000,
            3_000,
            10_000,
            30_000,
            50_000,
            70_000
    );

    protected boolean saveCondition(int generation) {
        return generation % 100_000 == 0 || saveGenerations.contains(generation);
    }

    protected abstract int getCurrentLoss();

    private void displayTop() {
        gui.setCreatedImage(getTop().toImage());
    }

    protected abstract ImageRepresentation getTop();

    protected abstract void initializePopulation();

    protected abstract void nextGeneration();

    // may change order of input list
    public <T extends ImageRepresentation> List<T> chooseFittest(int n, List<T> images) {
        assert n <= images.size();
        Map<ImageRepresentation, Integer> imageToLoss = new HashMap<>();
        for (ImageRepresentation image : images) {
            int absoluteDifference = ImageRepresentation.absoluteDifference(originalImage, image.toImage());
            imageToLoss.put(image, absoluteDifference);
        }
        images.sort(Comparator.comparingInt(imageToLoss::get));
        return images.subList(0, n);
    }

    protected Color mutateColor(Color color, int maxColorChange, boolean mutateAlpha, boolean mutateAll) {
        int[] colorArray;
        if (mutateAlpha) {
            colorArray = new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
        } else {
            colorArray = new int[] {color.getRed(), color.getGreen(), color.getBlue()};
        }
        if (mutateAll) {
            for (int i = 0; i < colorArray.length; i++) {
                colorArray[i] = bound(colorArray[i] + doubleRand(maxColorChange), 0, 255);
            }
        } else {
            int pos = random.nextInt(colorArray.length);
            colorArray[pos] = bound(colorArray[pos] + doubleRand(maxColorChange), 0, 255);
        }
        if (mutateAlpha) {
            return new Color(colorArray[0], colorArray[1], colorArray[2], colorArray[3]); // Math.max(colorArray[3], 30)
        } else {
            return new Color(colorArray[0], colorArray[1], colorArray[2]);
        }
    }

    protected int doubleRand(int n) {
        return random.nextInt(2 * n) - n;
    }

    protected int bound(int n, int min, int max) {
        n = Math.max(n, min);
        return Math.min(Math.max(n, min), max);
    }
}
