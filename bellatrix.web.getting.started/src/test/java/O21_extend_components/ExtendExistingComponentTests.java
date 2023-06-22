package O21_extend_components;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class ExtendExistingComponentTests extends WebTest {
    @Test
    public void PurchaseRocket() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEnding(Select.class, "orderby");
        Anchor protonMReadMoreButton = app().create().byInnerTextContaining(Anchor.class, "Read more");
        Anchor addToCartFalcon9 = app().create().byAttributeContaining(Anchor.class, "data-product_id", "28").toBeClickable();
        Anchor viewCartButton = app().create().byClassContaining(Anchor.class, "added_to_cart wc-forward").toBeClickable();
        TextInput couponCodeTextField = app().create().byId(TextInput.class, "coupon_code");
        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");
        NumberInput quantityBox = app().create().byClassContaining(NumberInput.class, "input-text qty text");
        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");
        Button updateCart = app().create().byValueContaining(Button.class, "Update cart").toBeClickable();
        ExtendedButton proceedToCheckout = app().create().byClassContaining(ExtendedButton.class, "checkout-button button alt wc-forward");
        // Instead of the regular button, we create the ExtendedButton, this way we can use its new methods.
        Heading billingDetailsHeading = app().create().byInnerTextContaining(Heading.class, "Billing details");
        Span totalSpan = app().create().byXPath(Span.class, "//*[@class='order-total']//span");

        sortDropDown.selectByText("Sort by price: low to high");
        protonMReadMoreButton.hover();
        addToCartFalcon9.focus();
        addToCartFalcon9.click();
        viewCartButton.click();
        couponCodeTextField.setText("happybirthday");
        applyCouponButton.click();

        messageAlert.toBeVisible().waitToBe();
        messageAlert.validateTextIs("Coupon code applied successfully.");
        quantityBox.setNumber(0);
        quantityBox.setNumber(2);

        updateCart.click();

        totalSpan.validateTextIs("95.00€");

        proceedToCheckout.submitButtonWithEnter();
        billingDetailsHeading.toBeVisible().waitToBe();
    }
}