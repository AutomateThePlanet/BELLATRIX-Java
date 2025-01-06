package O25_openCv_image_actions;

import O25_openCv_image_actions.data.enums.EncodedImageDemo;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import solutions.bellatrix.web.components.ActionImage;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.findstrategies.ImageBase64FindStrategy;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class OpenCvDemoTests extends WebTest {
    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        app().browser().setSize(1920, 1080);
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_clickImage() {
        var falcon9Image = app().create().by(ActionImage.class, new ImageBase64FindStrategy(EncodedImageDemo.FALCON_9));

        app().navigate().to("http://demos.bellatrix.solutions/");
        falcon9Image.click();

        app().browser().assertLandedOnPage("product/falcon-9");
    }

    @Test
    public void actionPerformed_when_convertBase64ToImage_and_dragAndDropImage() {
        var emailNotes = app().create().byId(Span.class, "email-notes");
        var commentTextArea = app().create().byId(TextArea.class, "comment");
        var falcon9BackButtonImage = app().create().by(ActionImage.class, new ImageBase64FindStrategy(EncodedImageDemo.FALCON_9_BACK_BUTTON));
        var commentTextAreaImage = app().create().by(ActionImage.class, new ImageBase64FindStrategy(EncodedImageDemo.COMMENT_TEXT_AREA));

        app().navigate().to("https://demos.bellatrix.solutions/2018/04/06/proton-rocket-family/");
        emailNotes.scrollToVisible();
        falcon9BackButtonImage.dragAndDrop(commentTextAreaImage);

        commentTextArea.validateTextIs("https://demos.bellatrix.solutions/2018/04/06/hello-world/");
    }
}