package O24_add_new_component_wait_methods;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class NewElementWaitTests extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var promotionsLink = app().create().byLinkText(Anchor.class, "promo");
        // you can use the new wait method as shown in the example:
        promotionsLink.ensureState(ToHaveStyleWaitStrategy.of("padding: 1.618em 1em"));
        promotionsLink.click();
    }
}