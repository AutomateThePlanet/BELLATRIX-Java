package O23_add_new_find_locators;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class NewFindLocatorsTests extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        // You can use the new FindStrategy in the default create().by and create().allBy methods like in this example:
        var promotionsLink = app().create().by(Anchor.class, new IdStartingWithFindStrategy("promo"));

        promotionsLink.click();
    }
}