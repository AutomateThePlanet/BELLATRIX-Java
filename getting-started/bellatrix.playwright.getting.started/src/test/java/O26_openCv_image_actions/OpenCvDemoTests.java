package O25_openCv_image_actions;

import O25_openCv_image_actions.data.enums.EncodedImageDemo;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.ActionImage;
import solutions.bellatrix.playwright.components.Span;
import solutions.bellatrix.playwright.components.TextArea;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class OpenCvDemoTests extends WebTest {
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

        app().browser().validateLandedOnPage("product/falcon-9");
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