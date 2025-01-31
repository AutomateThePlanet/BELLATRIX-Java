package opencv;

import opencv.data.EncodedImageDemo;
import opencv.data.EncodedImageNavigationDemo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.Keys;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class OpenCvTests extends WebTest {
    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        app().browser().maximize();
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickImage() {
        var falcon9Image = app().create().byImage(Anchor.class, EncodedImageDemo.FALCON_9);

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

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickButton() {
        Button falcon9Image = app().create().byImage(Button.class, EncodedImageDemo.FALCON_9);

        app().navigate().to("http://demos.bellatrix.solutions/");
        falcon9Image.click();

        app().browser().assertLandedOnPage("product/falcon-9");
    }

    @Test
    public void actionPerformed_when_locateComplexComponents() {
        SearchField websiteSearch = app().create().byImage(SearchField.class, EncodedImageDemo.SEARCH_INPUT);

        app().navigate().to("http://demos.bellatrix.solutions/");
        websiteSearch.setSearch("Falcon");
        websiteSearch.getWrappedElement().sendKeys(Keys.ENTER);

        app().browser().assertLandedOnPage("?s=Falcon&post_type=product");
        app().create().byImage(ActionImage.class, EncodedImageDemo.FALCON_RESULTS).validateIsVisible();
    }

    @ParameterizedTest
    @EnumSource(EncodedImageNavigationDemo.class)
    public void actionPerformed_when_locateButtonComponents(EncodedImageNavigationDemo navigationItem) {
        Button navgationButton = app().create().byImage(Button.class, navigationItem);

        app().navigate().to("http://demos.bellatrix.solutions/");
        navgationButton.click();

        app().browser().assertLandedOnPage(navigationItem.getExpectedUrl());
    }
}