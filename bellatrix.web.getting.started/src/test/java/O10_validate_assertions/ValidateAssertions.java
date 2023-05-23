package O10_validate_assertions;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class ValidateAssertions extends WebTest {
    @Test
    public void assertValidateCartPageFields() {
        app().navigate().to("http://demos.bellatrix.solutions/?add-to-cart=26");

        app().navigate().to("http://demos.bellatrix.solutions/cart/");

        TextField couponCodeTextField = app().create().byId(TextField.class, "coupon_code");

        couponCodeTextField.validatePlaceholderIs("Coupon code");
        // If we use the validate methods, BELLATRIX waits some time for the condition to pass. After this period if it
        // is not successful, a beautified meaningful exception message is displayed:
        // The component's placeholder should be 'Discount code' but was 'Coupon code'. The test failed on URL:
        // http://demos.bellatrix.solutions/cart/

        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");

        applyCouponButton.validateIsVisible();
        // If we use the validateIsVisible method and the check does not succeed the following error message is displayed:
        // The control should be visible but wasn't. The test failed on URL: http://demos.bellatrix.solutions/cart/

        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");

        messageAlert.validateNotVisible();

        Button updateCart = app().create().byValueContaining(Button.class, "Update cart");

        updateCart.validateIsDisabled();

        Span totalSpan = app().create().byXPath(Span.class, "//*[@class='order-total']//span");

        totalSpan.validateTextIs("120.00€");
        // Check the total price contained in the order-total span HTML element. By default, all Validate methods have 5
        // seconds timeout. However, you can specify a custom timeout and sleep interval (period for checking again)
    }
}