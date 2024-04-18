package O17_logging;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.BrowserTypes;
import solutions.bellatrix.playwright.infrastructure.ExecutionBrowser;
import solutions.bellatrix.playwright.infrastructure.Lifecycle;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;
@ExecutionBrowser (browser = BrowserTypes.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class LoggingTests extends WebTest {
    @Test
    public void addCustomMessagesToLog() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEndingWith(Select.class, "orderby");
        Anchor protonMReadMoreButton = app().create().byInnerTextContaining(Anchor.class, "Read more");
        Anchor addToCartFalcon9 = app().create().byAttributeContaining(Anchor.class, "data-product_id", "28").toBeClickable();

        sortDropDown.selectByText("Sort by price: low to high");
        protonMReadMoreButton.hover();

        Log.info("before adding Falcon 9 rocket to cart.");
        // Sometimes is useful to add information to the generated test log. To do it you can use the BELLATRIX built-in
        // logger through the Log class’s static methods.

        addToCartFalcon9.focus();
        addToCartFalcon9.click();

        // Generated Log, as you can see the above custom message is added to the log.
        // Start Test
        // Class = LoggingTests Name = AddCustomMessagesToLog
        // selecting 'Sort by price: low to high' from Select (name ending with orderby)
        // hovering Anchor (text containing Read more)
        // before adding Falcon 9 rocket to cart.
        // focusing Anchor (data-product_id containing 28)
        // clicking Anchor (data-product_id containing 28)
    }
}