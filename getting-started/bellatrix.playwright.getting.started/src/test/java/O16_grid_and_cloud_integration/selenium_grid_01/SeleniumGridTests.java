package O16_grid_and_cloud_integration.selenium_grid_01;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

@ExecutionBrowser (browser = Browsers.CHROME, browserVersion = 89, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class SeleniumGridTests extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");

        promotionsLink.click();
    }

    // There’s an option about execution type in the testFrameworkSettings.<env>.json file under the webSettings section.
    //
    //"webSettings": {
    //    "executionType": "grid"
    //}

    // You can set the grid URL and set some additional arguments under the gridSettings array, setting providerName to
    // the match the name you set in executionType. To set additional arguments for providers such as SauceLabs, BrowserStack or CrossBrowserTesting, you can use the arguments array parameter.
    //
    //"gridSettings": [
    //    {
    //        "providerName" : "grid",
    //        "url" : "http://127.0.0.1:4444/wd/hub",
    //        "arguments" : [
    //            {
    //                "name" : "bellatrix_run"
    //            }
    //        ]
    //    }
    //]
}