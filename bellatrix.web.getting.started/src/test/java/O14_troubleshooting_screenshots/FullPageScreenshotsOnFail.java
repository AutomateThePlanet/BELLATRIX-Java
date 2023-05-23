package O14_troubleshooting_screenshots;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class FullPageScreenshotsOnFail extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");
        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");
        promotionsLink.click();
        
        Assertions.fail();
    }

    // If you open the testFrameworkSettings.<env>.json file, you find the screenshot properties under webSettings section
    // that controls this behaviour.
    //
    //"webSettings": {
    //    "screenshotsOnFailEnabled": "true",
    //    "screenshotsSaveLocation": "user.home/BELLATRIX/Screenshots"
    //}
}