package O5_locate_and_wait_elements;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class LocateAndWaitElements extends WebTest {
    @Test
    public void blogPageOpened_When_PromotionsButtonClicked() {
        // The default timeouts that BELLATRIX uses are placed inside the testFrameworkSettings.<env>.json file, mentioned
        // in ‘File Structure’. Inside it, is the timeoutSettings section. All values are in seconds.
        // "timeoutSettings": {
        //  "elementWaitTimeout": "30",
        //  "pageLoadTimeout": "30",
        //  "scriptTimeout": "1",
        //  "waitForAjaxTimeout": "30",
        //  "sleepInterval": "1",
        //  "waitUntilReadyTimeout": "30",
        //  "waitForJavaScriptAnimationsTimeout": "30",
        //  "waitForAngularTimeout": "30",
        //  "waitForPartialUrl": "30",
        //  "validationsTimeout": "30",
        //  "elementToBeVisibleTimeout": "30",
        //  "elementToExistTimeout": "30",
        //  "elementToNotExistTimeout": "30",
        //  "elementToBeClickableTimeout": "30",
        //  "elementNotToBeVisibleTimeout": "30",
        //  "elementToHaveContentTimeout": "15"
        //}

        app().navigate().to("http://demos.bellatrix.solutions/");

        Anchor blogLink = app().create().byLinkText(Anchor.class, "Blog").toBeClickable().toBeVisible();
        // Your test fulfill the initial condition and if you use vanilla WebDriver the test most probably fails because
        // WebDriver clicks too fast before your button is enabled by your code. So we created additional syntax sugar
        // methods to help you deal with this. You can use component “toBe” methods after the create().by and create().allBy
        // methods. As you can see in the example below you can chain multiple of this methods.

        // Note: Since BELLATRIX, components creation logic is lazy loading as mentioned before, BELLATRIX waits for the
        // conditions to be True on the first action you perform with the component.

        blogLink.click();

        Anchor promotionsLink = app().create().byLinkText(Anchor.class, "Promotions").toExist();

        promotionsLink.click();

        // All Available toBe methods:
        // toExist --> app().create().byLinkText(Anchor.class, "Blog").toExist();
        // toBeVisible --> app().create().byLinkText(Anchor.class, "Blog").toBeVisible();
        // toBeClickable --> app().create().byLinkText(Anchor.class, "Blog").toBeClickable();
        // toBeDisabled --> app().create().byLinkText(Anchor.class, "Blog").toBeDisabled();
        // toHaveContent --> app().create().byLinkText(Anchor.class, "Blog").toBeDisabled();
        // toNotBeVisible --> app().create().byLinkText(Anchor.class, "Blog").toNotBeVisible();
        // toNotExist --> app().create().byLinkText(Anchor.class, "Blog").toNotExist();
    }
}