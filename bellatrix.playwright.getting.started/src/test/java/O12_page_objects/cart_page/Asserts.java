package O12_page_objects.cart_page;

import solutions.bellatrix.playwright.pages.PageAsserts;

public class Asserts extends PageAsserts<Map> {
    public void couponAppliedSuccessfully() {
        map().messageAlert().validateInnerTextIs("Coupon code applied successfully.");
    }

    // In the Asserts file, we have a method called totalPrice. We can access it through the asserts method, just like
    // we used the map method above. With this validation, reuse the formatting of the currency. Also, since the method
    // is called from the page it makes your tests a little more readable. If there is a change what needs to be
    // checked –> for example, not span but different element you can change it in a single place.
    public void totalPrice(String expectedPrice) {
        map().totalSpan().validateInnerTextIs(expectedPrice + "€");
    }
}
