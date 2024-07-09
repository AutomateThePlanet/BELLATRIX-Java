package O7_common_services.dialog_service_02;

import com.microsoft.playwright.Dialog;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import solutions.bellatrix.playwright.components.Button;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;
import solutions.bellatrix.playwright.services.DialogButton;


public class DialogServiceTests extends WebTest {
    @Test
    public void happyBirthdayCouponDisplayed_When_ClickOnCouponButton() {
        // BELLATRIX gives you some methods for handling dialogs.
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var couponButton = app().create().byId(Button.class, "couponBtn");
        couponButton.click();

       app().dialogs().addDialogHandler(x -> {
           if (x.message().equals("Try the coupon- happybirthday")) {
               x.accept("OK");
           }
       });
    }

    @Test
    public void dismissDialogAlert() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var couponButton = app().create().byId(Button.class, "couponBtn");
        couponButton.click();

        app().dialogs().addDialogHandler(Dialog::dismiss);
    }
    @Test
    public void acceptDialogAlert() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var couponButton = app().create().byId(Button.class, "couponBtn");
        couponButton.click();

        app().dialogs().addDialogHandler(Dialog::accept);
    }
}
