package O6_waits_for_components;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class WaitComponentsTests extends WebTest {
    @Test
    public void blogPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var blogLink = app().create().byLinkText(Anchor.class, "Blog");
        ((Anchor)blogLink.toBeClickable().toBeVisible()).click();
        // Besides the ToBe methods that you can use on element creation, you have a couple of other options if you need
        // to wait for elements.
    }
}