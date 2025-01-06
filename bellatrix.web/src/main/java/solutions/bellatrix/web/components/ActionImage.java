package solutions.bellatrix.web.components;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.interactions.Actions;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.plugins.opencv.OpenCvService;
import solutions.bellatrix.web.findstrategies.ImageBase64FindStrategy;
import solutions.bellatrix.web.services.App;

import java.awt.*;

@Getter
@Setter
public class ActionImage extends WebComponent {
    private final App app = new App();
    private Actions actions = new Actions(app.browser().getWrappedDriver());
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> HOVERING = new EventListener<>();

    @Override
    public Point getLocation() {
        ImageBase64FindStrategy findStrategy;
        try {
            findStrategy = (ImageBase64FindStrategy)getFindStrategy();
        } catch (ClassCastException e) {
            throw new InvalidArgumentException("Invalid image base 64 format");
        }

        var encodedImage = findStrategy.getEncodedImage();

        var location = OpenCvService.getLocation(encodedImage, true);
        return new Point((int)location.x + encodedImage.getXOffset(), (int)location.y + encodedImage.getYOffset());
    }

    public void click() {
        CLICKING.broadcast(new ComponentActionEventArgs(this, null, "Coordinates: %d, %d".formatted(getLocation().x, getLocation().y)));
        actions.moveByOffset(getLocation().x, getLocation().y)
                .click()
                .perform();
    }

    public void rightClick() {
        CLICKING.broadcast(new ComponentActionEventArgs(this, null, "Coordinates: %d, %d".formatted(getLocation().x, getLocation().y)));
        actions.moveByOffset(getLocation().x, getLocation().y)
                .contextClick()
                .perform();
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this, null, "Coordinates: %d, %d".formatted(getLocation().x, getLocation().y)));
        actions.moveToLocation(getLocation().x, getLocation().y)
                .perform();
    }

    public void dragAndDrop(ActionImage image) {
        actions.moveByOffset(getLocation().x, getLocation().y)
                .clickAndHold()
                .moveToLocation(image.getLocation().x, image.getLocation().y)
                .release()
                .perform();
    }
}