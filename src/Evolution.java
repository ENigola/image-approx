import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Evolution {

    protected BufferedImage originalImage;
    protected GUI gui;

    public Evolution(BufferedImage originalImage, GUI gui) {
        this.originalImage = originalImage;
        this.gui = gui;
    }

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
