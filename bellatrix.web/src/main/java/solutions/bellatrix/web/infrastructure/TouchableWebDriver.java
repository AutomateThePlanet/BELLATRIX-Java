package solutions.bellatrix.web.infrastructure;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

public class TouchableWebDriver extends ChromeDriver implements WebDriver  {

    public TouchableWebDriver(ChromeOptions options) {
        super(options);
    }

    public void triggerSwipeEvent(WebElement swipeFromElement)
    {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        // trigger swipe event from the middle of the right edge if the element
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), swipeFromElement.getLocation().x + swipeFromElement.getSize().getWidth(), swipeFromElement.getLocation().y + (swipeFromElement.getSize().getHeight()/ 2 )));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerMove(Duration.ofMillis(100), PointerInput.Origin.viewport(), swipeFromElement.getLocation().x, swipeFromElement.getLocation().y + (swipeFromElement.getSize().getHeight()/ 2 )));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        this.perform(Arrays.asList(tap));
    }

    public void triggerTapEvent(WebElement swipeFromElement)
    {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        int centerX = swipeFromElement.getLocation().x + swipeFromElement.getSize().getWidth() / 2;
        int centerY = swipeFromElement.getLocation().y + swipeFromElement.getSize().getHeight() / 2;

        // Move to the center of the element and perform a tap
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        this.perform(Arrays.asList(tap));
    }

    public void triggerHoldEvent(WebElement swipeFromElement) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        int centerX = swipeFromElement.getLocation().x + swipeFromElement.getSize().getWidth() / 2;
        int centerY = swipeFromElement.getLocation().y + swipeFromElement.getSize().getHeight() / 2;

        // Move to the center of the element and perform a tap
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        this.perform(Arrays.asList(tap));
    }

//
//    public void triggerTapEvent(WebElement tapElement)
//    {
//        var builder = new TouchActions(this);
//        IAction builtAction = builder.SingleTap(tapElement).Build();
//        builtAction.Perform();
//    }
//
//    public void triggerDoubleTapEvent(WebElement tapElement)
//    {
//        var builder = new TouchAction(this);
//        IAction builtAction = builder.DoubleTap(tapElement).Build();
//        builtAction.Perform();
//    }
//
//    private void TriggerScrollEvent(WebElement tapElement, int xOffset, int yOffset)
//    {
//        var builder = new TouchActions(this);
//        IAction builtAction = builder.Scroll(tapElement, xOffset, yOffset).Build();
//        builtAction.Perform();
//    }
//
//    private void TriggerScrollEvent(int xOffset, int yOffset)
//    {
//        var builder = new TouchActions(this);
//        IAction builtAction = builder.Scroll(xOffset, yOffset).Build();
//        builtAction.Perform();
//    }
//
//    public void scrollPage(WebElement fromElement, WebElement toElement)
//    {
//        TriggerScrollEvent(fromElement, 0, toElement.Location.Y - fromElement.Location.Y);
//    }
}
