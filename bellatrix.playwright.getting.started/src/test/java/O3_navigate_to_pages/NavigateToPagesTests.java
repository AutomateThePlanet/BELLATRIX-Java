package O3_navigate_to_pages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class NavigateToPagesTests extends WebTest{
    //  If you need each test to navigate each time to the same page, you can use the beforeEach annotation.

    @BeforeEach
    public void testInit() {
        app().navigate().to("http://demos.bellatrix.solutions/");
    }

    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        // app().navigate().to("http://demos.bellatrix.solutions/");

        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");

        promotionsLink.click();

        app().browser().waitUntilPageLoadsCompletely();
    }

    @Test
    public void blogPageOpened_When_PromotionsButtonClicked() {
      //  app().navigate().to("http://demos.bellatrix.solutions/");

        var blogLink = app().create().byLinkText(Anchor.class, "Blog");

        blogLink.click();

        app().navigate().waitForPartialUrl("/blog/");
        //  Sometimes before proceeding with searching and making actions on the next page, we need to wait for something.
        //  It is useful in some cases to wait for a partial URL instead hard-coding the whole URL since it can change
        //  depending on the environment. Keep in mind that usually this is not necessary since BELLATRIX has a complex
        //  built-in mechanism for handling element waits.
    }

    @Test
    public void TestFileOpened_When_NavigateToLocalPage() {
        // 6. Sometimes you may need to navigate to a local HTML file.
        // Make sure to copy the file to the resources folder of the module with your tests files.
        app().navigate().toLocalPage("testPage.html");
    }
}