package O16_grid_and_cloud_integration.browser_stack_02;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class BrowserStackTests extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");

        promotionsLink.click();
    }
}