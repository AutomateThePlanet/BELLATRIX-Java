package O15_troubleshooting_video_recording;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class VideoRecordingTests extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");
        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");
        promotionsLink.click();

        Assertions.fail();
        // If you open the testFrameworkSettings.<env>.json file, you find the screenshot properties under webSettings
        // section that controls this behaviour.
        //
        //"webSettings": {
        //    "videosOnFailEnabled": "true",
        //    "videosSaveLocation": "user.home/BELLATRIX/Videos"
        //}
        // You can turn off the making of videos for all tests and specify where the videos to be saved. In the
        // extensibility chapters read more about how you can create custom video recorder or change the saving strategy.
    }
}