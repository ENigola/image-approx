package core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

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
        this.random = new Random(0); // TODO: UNSEED
        this.originalImage = originalImage;
        this.gui = gui;
        this.maxGenerations = maxGenerations;
        this.maxNoChangeGenerations = maxNoChangeGenerations;
        this.displayFreq = displayFreq;
        if (maxGenerations == null && maxNoChangeGenerations == null) {
            throw new RuntimeException("maxGenerations and maxNoChangeGenerations can't both be null");
        }
    }

    public void evolve() {
        initializePopulation();
        currentLoss = getCurrentLoss();
        while (running) {
            if (maxGenerations != null && generation > maxGenerations) {
                gui.setInfoText("" + maxGenerations + " generations finished.");
                break;
            }
            int oldLoss = currentLoss;
            nextGeneration();
            if (generation % displayFreq == 0) {
                gui.setGeneration(generation);
                System.out.println("" + currentLoss + " " + noChangeGenerations); // TODO: delete
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
        System.out.println("fin!!!");
        gui.setGeneration(generation);
        displayTop();
    }

    protected abstract int getCurrentLoss();

    protected abstract void displayTop();

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
            return new Color(colorArray[0], colorArray[1], colorArray[2], colorArray[3]);
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
