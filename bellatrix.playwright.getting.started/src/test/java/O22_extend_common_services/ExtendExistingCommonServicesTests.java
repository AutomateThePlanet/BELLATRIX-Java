package O22_extend_common_services;

import O21_extend_components.ExtendedButton;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class ExtendExistingCommonServicesTests extends WebTest {
    @Override
    public ExtendedApp app() {
        return new ExtendedApp();
    }

    @Test
    public void purchaseRocket() {
        app().navigate().viaJavaScript("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEndingWith(Select.class, "orderby");
        Anchor protonMReadMoreButton =
                app().create().byInnerTextContaining(Anchor.class, "Read more");
        Anchor addToCartFalcon9 =
                app().create().byAttributeContaining(Anchor.class, "data-product_id", "28").toBeClickable();
        Anchor viewCartButton =
                app().create().byClassContaining(Anchor.class, "added_to_cart wc-forward").toBeClickable();
        TextInput couponCodeTextInput = app().create().byId(TextInput.class, "coupon_code");
        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");
        NumberInput quantityBox = app().create().byClassContaining(NumberInput.class, "input-text qty text");
        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");
        Button updateCart = app().create().byValueContaining(Button.class, "Update cart").toBeClickable();
        ExtendedButton proceedToCheckout =
                app().create().byClassContaining(ExtendedButton.class, "checkout-button button alt wc-forward");
        Heading billingDetailsHeading =
                app().create().byInnerTextContaining(Heading.class, "Billing details");
        Span totalSpan = app().create().byXpath(Span.class, "//*[@class='order-total']//span");

        sortDropDown.selectByText("Sort by price: low to high");
        protonMReadMoreButton.hover();
        addToCartFalcon9.focus();
        addToCartFalcon9.click();
        viewCartButton.click();
        couponCodeTextInput.setText("happybirthday");
        applyCouponButton.click();

        messageAlert.toHaveContent().toBeVisible().waitToBe();
        messageAlert.validateInnerTextIs("Coupon code applied successfully.");
        quantityBox.setNumber(0);
        quantityBox.setNumber(2);

        updateCart.click();

        totalSpan.validateInnerTextIs("95.00€");

        proceedToCheckout.submitButtonWithEnter();
        billingDetailsHeading.toBeVisible().waitToBe();
    }
}
