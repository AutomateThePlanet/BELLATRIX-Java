package O2_control_browser;

import solutions.bellatrix.playwright.infrastructure.ExecutionBrowser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

//  1. This is the attribute for automatic start/control of Playwright browsers by BELLATRIX. If you have to do it
//  manually properly, you will need thousands of lines of code.
//  1.1 BrowserChoice controls which browser is used. Available options are:
//  Chromium, Chrome, Firefox, Edge, WebKit, and each of them has an option to run in headless mode.
//  Note: Headless mode = executed in the browser but the browser’s UI is not rendered, in theory, should be faster.
//  In practice the time gain is little.
//
//  1.2 Lifecycle enum controls when the browser is started and stopped. This can drastically increase or decrease
//  the tests execution time, depending on your needs. However, you need to be careful because in case of tests
//  failures the browser
//  may need to be restarted. Available options:
//  RESTART_EVERY_TIME – for each test a separate Playwright and Browser instance is created and the previous ones are closed.
//  RESTART_ON_FAIL – the browser is only restarted if the previous test failed. Alternatively, if the previous test’s
//  browser was different.
//  REUSE_IF_STARTED – the browser is only restarted if the previous test’s browser was different. In all other cases,
//  the browser is reused if possible.
//  Note: However, use this option with caution since in some rare cases if you have not properly set up your tests
//  you may need to restart the browser if the test fails otherwise all other tests may fail too.
@ExecutionBrowser (browser = Browsers.FIREFOX, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class BellatrixBrowserBehaviourTests extends WebTest {
    // 1.3 All playwright BELLATRIX test classes should inherit from the WebTest base class. This way you can use all built-in
    // BELLATRIX tools and functionalities. If you place attribute over the class all tests inherit the Lifecycle.
    // It is possible to place it over each test and this way it overrides the class Lifecycle only for this particular
    // test.
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        // 1.4 There is more about the app class in the next sections. However, it is the primary point where you access the
        // BELLATRIX services. It comes from the WebTest class as a property. Here we use the BELLATRIX navigation service
        // to navigate to the demo page.
        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");

        promotionsLink.click();

        Assertions.assertEquals("https://demos.bellatrix.solutions/welcome/", app().browser().getUrl());
    }

    // 1.5 As mentioned above you can override the browser Lifecycle for a particular test. The global Lifecycle for all
    // tests in the class is to reuse an instance of Firefox browser. Only for this particular test, BELLATRIX opens
    // Chrome and restarts it only on fail.
    @ExecutionBrowser (browser = Browsers.CHROME, lifecycle = Lifecycle.RESTART_ON_FAIL)
    @Test
    public void blogPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var blogLink = app().create().byLinkText(Anchor.class, "Blog");

        blogLink.click();
    }
}