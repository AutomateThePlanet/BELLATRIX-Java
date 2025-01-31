package solutions.bellatrix.playwright.components;

import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.options.MouseButton;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.InvalidArgumentException;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.findstrategies.ImageBase64FindStrategy;
import solutions.bellatrix.playwright.infrastructure.PlaywrightService;
import solutions.bellatrix.plugins.opencv.OpenCvService;

import java.awt.*;

@Getter
@Setter
public class ActionImage extends WebComponent {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> HOVERING = new EventListener<>();

    private final Mouse mouse = PlaywrightService.wrappedBrowser().getCurrentPage().mouse();

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

        mouse.click(getLocation().x, getLocation().y);
    }

    public void rightClick() {
        CLICKING.broadcast(new ComponentActionEventArgs(this, null, "Coordinates: %d, %d".formatted(getLocation().x, getLocation().y)));

        mouse.click(getLocation().x, getLocation().y, new Mouse.ClickOptions().setButton(MouseButton.RIGHT));
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this, null, "Coordinates: %d, %d".formatted(getLocation().x, getLocation().y)));

        mouse.move(getLocation().x, getLocation().y);
    }

    public void dragAndDrop(ActionImage image) {
        mouse.move(getLocation().x, getLocation().y);
        mouse.down();
        mouse.move(image.getLocation().x, image.getLocation().y);
        mouse.up();
    }
}