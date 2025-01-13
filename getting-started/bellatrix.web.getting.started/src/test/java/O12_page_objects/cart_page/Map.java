package O12_page_objects.cart_page;

import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.pages.PageMap;

import java.util.List;

public class Map extends PageMap {
    public TextInput couponCodeTextInput() {
        return create().byId(TextInput.class, "coupon_code");
    }

    public Button applyCouponButton() {
        return create().byValueContaining(Button.class, "Apply coupon");
    }

    public List<NumberInput> quantityBoxes() {
        return create().allByClassContaining(NumberInput.class, "input-text qty text");
    }

    public Div messageAlert() {
        return create().byClassContaining(Div.class, "woocommerce-message");
    }

    public Span totalSpan() {
        return create().byXPath(Span.class, "//*[@class='order-total']//span");
    }

    public Button proceedToCheckout() {
        return create().byClassContaining(Button.class, "checkout-button button alt wc-forward");
    }
}