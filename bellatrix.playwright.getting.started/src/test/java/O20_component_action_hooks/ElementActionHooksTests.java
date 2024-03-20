package O20_component_action_hooks;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class ElementActionHooksTests extends WebTest {

    @Override
    public void configure() {
        super.configure();
        LoggingButtonEvents.addEventListeners();
        // Once you have created the class, you need to tell BELLATRIX to use it. To do so call the addEventListeners
        // method that you created in the configure phase.
    }

    @Test
    public void purchaseRocketWithGloballyOverriddenMethods() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEndingWith(Select.class, "orderby");
        Anchor protonMReadMoreButton =
                app().create().byTextContaining(Anchor.class, "Read more");
        Anchor addToCartFalcon9 =
                app().create().byAttributeContaining(Anchor.class, "data-product_id", "28").toBeClickable();
        Anchor viewCartButton =
                app().create().byClassContaining(Anchor.class, "added_to_cart wc-forward").toBeClickable();
        TextInput couponCodeTextInput = app().create().byId(TextInput.class, "coupon_code");
        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");
        NumberInput quantityBox = app().create().byClassContaining(NumberInput.class, "input-text qty text");
        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");
        Button updateCart = app().create().byValueContaining(Button.class, "Update cart").toBeClickable();
        Anchor proceedToCheckout =
                app().create().byClassContaining(Anchor.class, "checkout-button button alt wc-forward");
        Heading billingDetailsHeading =
                app().create().byTextContaining(Heading.class, "Billing details");
        Span totalSpan = app().create().byXpath(Span.class, "//*[@class='order-total']//span");

        sortDropDown.selectByText("Sort by price: low to high");

        // TODO: Do the test
    }
}