package core;

import java.awt.image.BufferedImage;

public abstract class BinaryEvolution<T extends ImageRepresentation> extends Evolution {

    protected T current;

    public BinaryEvolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, maxGenerations, maxNoChangeGenerations, displayFreq);
    }

    @Override
    protected ImageRepresentation getTop() {
        return current;
    }

    @Override
    protected int getCurrentLoss() {
        return ImageRepresentation.absoluteDifference(originalImage, current.toImage());
    }

    @Override
    protected void nextGeneration() {
        T newImage = (T) current.clone();
        mutate(newImage);
        int newLoss;
        try {
            newLoss = ImageRepresentation.absoluteDifference(originalImage, newImage.toImage());
        } catch (IllegalStateException | NullPointerException e) {
            newLoss = Integer.MAX_VALUE;
        }
        if (newLoss < currentLoss) {
            currentLoss = newLoss;
            current = newImage;
        }
    }

    protected abstract void mutate(T image);
}
