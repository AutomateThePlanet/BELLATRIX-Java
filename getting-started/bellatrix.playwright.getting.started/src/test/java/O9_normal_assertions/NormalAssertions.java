package O9_normal_assertions;

import org.junit.jupiter.api.Test;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class NormalAssertions extends WebTest {
    @Test
    public void assertCartPageFields() {
        app().navigate().to("http://demos.bellatrix.solutions/?add-to-cart=26");

        app().navigate().to("http://demos.bellatrix.solutions/cart/");

        TextInput couponCodeTextInput = app().create().byId(TextInput.class, "coupon_code");

        Assert.assertEquals(couponCodeTextInput.getPlaceholder(), "Coupon code");

        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");

        Assert.assertTrue(applyCouponButton.isVisible());
        // Here we assert that the apply coupon button is visible on the page.
        // On fail the following message is displayed:
        // Expected :true
        // Actual   :false
        // Cannot learn much about what happened.

        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");

        Assert.assertFalse(messageAlert.isVisible());

        Button updateCart = app().create().byValueContaining(Button.class, "Update cart");

        Assert.assertTrue(updateCart.isDisabled());

        Span totalSpan = app().create().byXpath(Span.class, "//*[@class='order-total']//span");

        Assert.assertEquals(totalSpan.getText(), "120.00€");
        // We check the total price contained in the order-total span HTML element.

        SoftAssert multipleAssert = new SoftAssert();
        multipleAssert.assertEquals(totalSpan.getText(), "120.00€");
        multipleAssert.assertTrue(updateCart.isDisabled());
        multipleAssert.assertAll();
        // You can execute multiple assertions failing only once viewing all results.

        // One more thing you need to keep in mind is that normal assertion methods do not include BDD logging and any
        // available hooks. BELLATRIX provides you with a full BDD logging support for validate assertions and gives you
        // a way to hook your logic in multiple places.
    }
}