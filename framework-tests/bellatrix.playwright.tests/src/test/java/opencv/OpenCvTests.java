package opencv;

import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.ViewportSize;
import opencv.data.EncodedImageDemo;
import opencv.data.EncodedImageNavigationDemo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.BrowserTypes;
import solutions.bellatrix.playwright.infrastructure.ExecutionBrowser;
import solutions.bellatrix.playwright.infrastructure.Lifecycle;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

@ExecutionBrowser(browser = BrowserTypes.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class OpenCvTests extends WebTest {
    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        app().browser().setSize(1920, 1080);
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickImage() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var falcon9Image = app().create().byImage(Anchor.class, EncodedImageDemo.FALCON_9);

        falcon9Image.click();

        app().browser().assertUrlPath("/product/falcon-9/");
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_dragAndDropImage() {
        app().navigate().to("https://demos.bellatrix.solutions/2018/04/06/proton-rocket-family/");

        var emailNotes = app().create().byId(Span.class, "email-notes");
        var commentTextArea = app().create().byId(TextArea.class, "comment");
        var falcon9BackButtonImage = app().create().byImage(ActionImage.class, EncodedImageDemo.FALCON_9_BACK_BUTTON);
        var commentTextAreaImage = app().create().byImage(ActionImage.class, EncodedImageDemo.COMMENT_TEXT_AREA);

        emailNotes.scrollToVisible();
        falcon9BackButtonImage.dragAndDrop(commentTextAreaImage);

        commentTextArea.validateTextIs("https://demos.bellatrix.solutions/2018/04/06/hello-world/");
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickButton() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Button falcon9Image = app().create().byImage(Button.class, EncodedImageDemo.FALCON_9);

        falcon9Image.click();

        app().browser().assertUrlPath("/product/falcon-9/");
    }

    @Test
    public void actionPerformed_when_locateComplexComponents() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        SearchField websiteSearch = app().create().byImage(SearchField.class, EncodedImageDemo.SEARCH_INPUT);

        DebugInformation.debugInfo(websiteSearch.getPlaceholder());

        websiteSearch.setSearch("Falcon");
        websiteSearch.getWrappedElement().press("Enter", new Locator.PressOptions().setDelay(1500));

        app().browser().assertUrlPath("?s=Falcon&post_type=product");
        app().create().byImage(ActionImage.class, EncodedImageDemo.FALCON_RESULTS).validateIsVisible();
    }

    @ParameterizedTest
    @EnumSource(EncodedImageNavigationDemo.class)
    public void actionPerformed_when_locateButtonComponents(EncodedImageNavigationDemo navigationItem) {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Button navgationButton = app().create().byImage(Button.class, navigationItem);

        navgationButton.click();

        app().browser().validateLandedOnPage(navigationItem.getExpectedUrl());
    }
}