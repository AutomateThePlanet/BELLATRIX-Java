package O6_waits_for_components;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class WaitComponentsTests extends WebTest {
    @Test
    public void blogPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var blogLink = app().create().byLinkText(Anchor.class, "Blog");
        ((Anchor)blogLink.toBeClickable().toBeVisible()).click();
        // Besides the ToBe methods that you can use on element creation, you have a couple of other options if you need
        // to wait for elements. For example, if you want to reuse your element in multiple tests or if you use it through
        // page objects (more about that in later chapters), you may not want to wait for all conditions to be executed
        // every time. Sometimes the mentioned conditions during creation may not be correct for some specific test case.
        // E.g. button wait to be disabled, but in most cases, you need to wait for it to be enabled. To give you more
        // options BELLATRIX has a special method called waitToBe. The big difference compared to ToBe methods is that
        // it forces BELLATRIX to locate your element immediately and wait for the condition to be satisfied. This is
        // also valid syntax the conditions are performed once the click method is called. It is the same as placing toBe
        // methods after create().byLinkText.

        blogLink.toBeClickable().toBeVisible().waitToBe();
    }
}