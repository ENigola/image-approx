package main;

import java.awt.image.BufferedImage;
import java.util.*;

public abstract class Evolution {

    protected Random random;
    protected BufferedImage originalImage;
    protected GUI gui;

    private Integer maxGenerations;
    private Integer maxNoChangeGenerations;
    private int displayFreq;

    protected double currentLoss;
    private int noChangeGenerations;

    public Evolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        this.random = new Random();
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
        int generation = 1;
        while (true) {
            if (maxGenerations != null && generation > maxGenerations) {
                generation--;
                break;
            }
            double oldLoss = currentLoss;
            nextGeneration();
            if (generation % displayFreq == 0) {
                gui.setGeneration(generation);
                displayTop();
            }
            if (currentLoss == oldLoss) {
                noChangeGenerations++;
            } else {
                noChangeGenerations = 0;
            }
            if (maxNoChangeGenerations != null && noChangeGenerations > maxNoChangeGenerations) {
                gui.setInfoText("No improvement for " + noChangeGenerations + " generations");
                break;
            }
            generation++;
        }
        gui.setGeneration(generation);
        displayTop();
    }

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
}
