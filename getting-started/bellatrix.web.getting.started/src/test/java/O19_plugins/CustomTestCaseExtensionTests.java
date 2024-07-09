package O19_plugins;

import org.junit.jupiter.api.Test;
import org.testng.Assert;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

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
        var cartContentButton = app().create().byXPath(Button.class, "//*[@id='site-header-cart']//a[contains(@href,'cart')]");
        cartContentButton.scrollToVisible();
        cartContentButton.hover();

        var viewCart = app().create().byXPath(Anchor.class,"//*[contains(@class,'woocommerce-mini-cart__buttons')]//a");
        viewCart.click();

        var proceedToCheckout = app().create().byInnerTextContaining( Anchor.class  ,"Proceed to checkout");
        proceedToCheckout.click();

        var placeOrder = app().create().byId( Button.class,"place_order");
        placeOrder.click();
        var woocommerceError = app().create().allByXPath(Span.class ,"//*[@class='woocommerce-error']/li");

        Assert.assertTrue(app().browser().getUrl().equals("https://demos.bellatrix.solutions/checkout/"));

        for (var item : woocommerceError) {
            item.validateIsVisible();
        }

        var firstName = app().create().byId(TextInput.class,"billing_first_name");
        firstName.validateTextIs("");

        var productTotal = app().create().byClass(Span.class,"cart_item").createByClass(Span.class,"product-total");
        productTotal.validateTextIs("120.00€");
    }
}