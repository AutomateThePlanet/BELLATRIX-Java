package O25_openCv_image_actions;

import O25_openCv_image_actions.data.enums.EncodedImageDemo;
import org.junit.jupiter.api.Test;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.web.components.ActionImage;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.infrastructure.*;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

/**
 * Image locators are generated from real images using the tool: <a href="https://automatetheplanet.github.io/OpenCV-Image-Match-Finder/">OpenCV Image Match Finder</a>
 * Where you can supply either:
 *  - Image from disc
 *  - Paste image from clipboard
 *  - Drag and drop image
 *  In addition it helps in troubleshooting where you want to check the confidence level of a match in a context image.
 */
@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class OpenCvDemoTests extends WebTest {

    @Override
    protected void configure() {
        addPlugin(BrowserLifecyclePlugin.class);
        // Adding the screenshot plugin as a WebScreenshot plugin is required for the feature to work correctly
        addPluginAs(ScreenshotPlugin.class, WebScreenshotPlugin.class);
    }

    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        app().browser().setSize(1920, 1080);
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickImage() {
        var falcon9Image = app().create().byImage(ActionImage.class, EncodedImageDemo.FALCON_9);

        app().navigate().to("http://demos.bellatrix.solutions/");
        falcon9Image.click();

        app().browser().assertLandedOnPage("product/falcon-9");
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickButton() {
        var falcon9Image = app().create().byImage(Button.class, EncodedImageDemo.FALCON_9);

        app().navigate().to("http://demos.bellatrix.solutions/");
        falcon9Image.click();

        app().browser().assertLandedOnPage("product/falcon-9");
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_dragAndDropImage() {
        var emailNotes = app().create().byId(Span.class, "email-notes");
        var commentTextArea = app().create().byId(TextArea.class, "comment");
        var falcon9BackButtonImage = app().create().byImage(ActionImage.class, EncodedImageDemo.FALCON_9_BACK_BUTTON);
        var commentTextAreaImage = app().create().byImage(ActionImage.class, EncodedImageDemo.COMMENT_TEXT_AREA);

        app().navigate().to("https://demos.bellatrix.solutions/2018/04/06/proton-rocket-family/");
        emailNotes.scrollToVisible();
        falcon9BackButtonImage.dragAndDrop(commentTextAreaImage);

        commentTextArea.validateTextIs("https://demos.bellatrix.solutions/2018/04/06/hello-world/");
    }
}