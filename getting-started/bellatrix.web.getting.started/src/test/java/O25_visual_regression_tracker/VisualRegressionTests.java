package O25_visual_regression_tracker;

import O25_visual_regression_tracker.data.MobileServiceMap;
import O25_visual_regression_tracker.data.MobileServicePage;
import org.junit.jupiter.api.Test;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.plugins.vrt.VisualRegression;
import solutions.bellatrix.plugins.vrt.VisualRegressionAssertions;
import solutions.bellatrix.plugins.vrt.VisualRegressionPlugin;
import solutions.bellatrix.web.infrastructure.WebScreenshotPlugin;
import solutions.bellatrix.web.infrastructure.junit.WebTest;
/*
    To begin tracking visual regression with BELLATRIX, one must have this in the config file:
    "visualRegressionSettings": {
      "apiUrl": "", (REQUIRED)
      "apiKey": "", (REQUIRED)
      "project": "", (REQUIRED)
      "branch": "vrt", (REQUIRED)
      "enableSoftAssert": "true", (REQUIRED)
      "ciBuildId": "vrt", (REQUIRED)
      "httpTimeout": "15", (REQUIRED)
      "defaultDiffTolerance": "0.1" (REQUIRED)
    }

    These settings are all required to start. After we have set them, we can then begin.
 */

// To mark a test class for visual regression, we simply use @VisualRegression annotation
// One can also provide the VRT project name and the resolution, otherwise default values will be used
@VisualRegression
public class VisualRegressionTests extends WebTest {
    protected void configure() {
        super.configure();
        addPlugin(VisualRegressionPlugin.class);

        // it is also important to have the screenshot plugin registered as the base class
        // addPluginAs(ScreenshotPlugin.class, WebScreenshotPlugin.class);
        // check in your base test if it is registered as that
    }
    // We need @VisualRegression annotation so that the tracker process could start at the beginning of the test
    // and be ready for use in said test.
    // After the test is over, the tracker is closed.

    @Test
    public void visualRegressionTest_using_assertions() {
        app().navigate().to("https://www.automatetheplanet.com/services/web-automation/");

        VisualRegressionAssertions.assertSameAsBaseline("web-automation-service-page");
        // This is the simplest way to start using VRT: by inserting a visual regression assertion method
        // here, the name required is the name of the baseline
    }

    @Test
    @VisualRegression // If only one of our tests is for visual regression tracking,
    // the @VisualRegression annotation can be used on the method instead
    public void visualRegressionTest_using_assertByPageObjectModel() {
        var mobileServicePage = new MobileServicePage();

        app().navigate().to(mobileServicePage);

        // If our baseline has the same name as the POM, we can just pass the POM.
        VisualRegressionAssertions.assertSameAsBaseline(mobileServicePage);
    }
}