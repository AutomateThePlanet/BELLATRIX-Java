package O19_plugins;

import org.junit.jupiter.api.Test;
import org.testng.Assert;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.components.Button;
import solutions.bellatrix.playwright.components.Span;
import solutions.bellatrix.playwright.components.TextInput;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class CustomTestCaseExtensionTests extends WebTest {
    @Override
    public void configure() {
        super.configure();
        addPlugin(AssociatedPlugin.class);
        // Once we created the test workflow plugin, we need to add it to the existing test workflow. It is done using
        // the addPlugin method of the BaseTest class.
    }

    @Test
    @ManualTestCase(id = 1532)
    public void addProductToCart() {
        var searchInput = app().create().byId(TextInput.class, "woocommerce-product-search-field-0");
        searchInput.setText("Saturn V");

        var addToCartSaturn = app().create().byAttributeContaining(Button.class, "aria-label", "Saturn V");
        addToCartSaturn.toBeClickable().waitToBe();
        addToCartSaturn.click();
        var cartContentButton = app().create().byXpath(Button.class, "//*[@id='site-header-cart']//a[contains(@href,'cart')]");
        cartContentButton.scrollToVisible();
        cartContentButton.hover();

        var viewCart = app().create().byXpath(Anchor.class,"//*[contains(@class,'woocommerce-mini-cart__buttons')]//a");
        viewCart.click();

        var proceedToCheckout = app().create().byInnerTextContaining( Anchor.class  ,"Proceed to checkout");
        proceedToCheckout.click();

        var placeOrder = app().create().byId( Button.class,"place_order");
        placeOrder.click();
        var woocommerceError = app().create().allByXpath(Span.class ,"//*[@class='woocommerce-error']/li");

        Assert.assertEquals(app().browser().getUrl(), "https://demos.bellatrix.solutions/checkout/");

        for (var item : woocommerceError) {
            item.validateIsVisible();
        }

        var firstName = app().create().byId(TextInput.class,"billing_first_name");
        firstName.validateInnerTextIs("");

        var productTotal = app().create().byClass(Span.class,"cart_item").createByClass(Span.class,"product-total");
        productTotal.validateInnerTextIs("120.00€");
    }
}