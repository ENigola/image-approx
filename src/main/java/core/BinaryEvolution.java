package core;

import java.awt.image.BufferedImage;

public abstract class BinaryEvolution<T extends ImageRepresentation> extends Evolution {

    protected T current;

    public BinaryEvolution(BufferedImage originalImage, GUI gui, Integer maxGenerations, Integer maxNoChangeGenerations, int displayFreq) {
        super(originalImage, gui, maxGenerations, maxNoChangeGenerations, displayFreq);
    }

    @Override
    protected void displayTop() {
        gui.setCreatedImage(current.toImage());
    }

    @Override
    protected int getCurrentLoss() {
        return ImageRepresentation.absoluteDifference(originalImage, current.toImage());
    }

    @Override
    protected void nextGeneration() {
        T newImage = (T) current.clone();
        mutate(newImage);
        int newLoss = ImageRepresentation.absoluteDifference(originalImage, newImage.toImage());
        if (newLoss < currentLoss) {
            currentLoss = newLoss;
            current = newImage;
        }
    }

    protected abstract void mutate(T image);
}
