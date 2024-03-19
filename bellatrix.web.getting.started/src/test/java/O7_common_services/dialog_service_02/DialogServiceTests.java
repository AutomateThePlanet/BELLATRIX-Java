package O7_common_services.dialog_service_02;

import org.junit.jupiter.api.Test;
import org.testng.Assert;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.infrastructure.junit.WebTest;
import solutions.bellatrix.web.services.DialogButton;


public class DialogServiceTests extends WebTest {
    @Test
    public void happyBirthdayCouponDisplayed_When_ClickOnCouponButton() {
        // BELLATRIX gives you some methods for handling dialogs.
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var couponButton = app().create().byId(Button.class, "couponBtn");
        couponButton.click();

       app().dialogs().handle(a -> Assert.assertEquals(a.getText(), "Try the coupon- happybirthday"), DialogButton.OK);
    }

    @Test
    public void dismissDialogAlert() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var couponButton = app().create().byId(Button.class, "couponBtn");
        couponButton.click();

       app().dialogs().handle(DialogButton.CANCEL);
    }
    @Test
    public void acceptDialogAlert() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var couponButton = app().create().byId(Button.class, "couponBtn");
        couponButton.click();

        app().dialogs().handle();
    }
}
