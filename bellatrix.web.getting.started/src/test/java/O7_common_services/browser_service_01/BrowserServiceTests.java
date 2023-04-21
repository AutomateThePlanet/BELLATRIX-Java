package O7_common_services.browser_service_01;

import solutions.bellatrix.web.infrastructure.junit.WebTest;
import org.junit.jupiter.api.Test;
public class BrowserServiceTests extends WebTest {

    @Test
    public void printCurrentPageHtml() {
        // BELLATRIX gives you an interface to most common operations for controlling the started browser through the
        // browser method.
        app().navigate().to("http://demos.bellatrix.solutions/");

        // Get the current page’s HTML source.
        System.out.println(app().browser().getPageSource());
    }

    @Test
    public void controlBrowser() {
        app().navigate().to("http://demos.bellatrix.solutions/");
        app().browser().maximize();

        // Simulates clicking the browser’s back button.
        app().browser().back();

        // Simulates clicking the browser’s forward button.
        app().browser().forward();

        // Simulates clicking the browser’s refresh button.
        app().browser().refresh();
    }

    @Test
    public void getCurrentUrl() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        // Get the current tab URL.
        System.out.println(app().browser().getUrl());
    }
}