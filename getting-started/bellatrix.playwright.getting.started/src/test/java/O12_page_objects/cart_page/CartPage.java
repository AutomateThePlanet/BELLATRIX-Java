package O12_page_objects.cart_page;

import solutions.bellatrix.playwright.pages.WebPage;

public class CartPage extends WebPage<Map, Asserts> {
    // All BELLATRIX page objects are implemented as a package with three different classes which means that you have
    // separate files for different parts of it – actions, elements and assertions. This makes the maintainability and
    // readability of these classes much better. Also, you can easily locate what you need. You can always create BELLATRIX
    // page objects yourself by extending the WebPage class.
    @Override
    protected String getUrl() {
        return "http://demos.bellatrix.solutions/cart/";
    }
    // Overriding the getUrl method that comes from the base page object you can later you the open method to go to the page.
    @Override
    protected void waitForPageLoad() {
        map().couponCodeTextInput().toExist().waitToBe();
    }

    public void applyCoupon(String coupon) {
        // Elements are accessed through the map method. These elements are always used together when coupon is applied.
        // There are many test cases where you need to apply different coupons and so on. This way you reuse the code
        // instead of copy-paste it. If there is a change in the way how the coupon is applied, change the workflow only
        // here. Even single line of code is changed in your tests.
        map().couponCodeTextInput().setText(coupon);
        map().applyCouponButton().click();
        browser().waitForAjax();
    }

    public void increaseProductQuantity(int productNumber, int newQuantity) {
        if(productNumber > map().quantityBoxes().size()){
            throw new IllegalArgumentException("There are less added items in the cart. Please specify smaller product number.");
        }

        map().quantityBoxes().get(productNumber - 1).setNumber(newQuantity);
        browser().waitForAjax();
    }
}