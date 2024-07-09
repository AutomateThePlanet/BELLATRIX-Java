package O12_page_objects;

import org.junit.jupiter.api.Test;
import O12_page_objects.cart_page.CartPage;
import O12_page_objects.checkoutpage.CheckoutPage;
import O12_page_objects.main_page.MainPage;
import O12_page_objects.models.PurchaseInfo;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class PageObjectsTests extends WebTest {
    @Test
    public void purchaseRocketWithPageObjects() {
        var homePage = app().goTo(MainPage.class);
        homePage.addRocketToShoppingCart("Falcon 9");

        var cartPage = app().create(CartPage.class);

        cartPage.applyCoupon("happybirthday");
        cartPage.increaseProductQuantity(1, 2);
        cartPage.asserts().totalPrice("54.00");
        cartPage.map().proceedToCheckout().click();

        var purchaseInfo = new PurchaseInfo();
        purchaseInfo.setFirstName("In");
        purchaseInfo.setLastName("Deepthought");
        purchaseInfo.setCompany("Automate The Planet Ltd.");
        purchaseInfo.setCountry("Bulgaria");
        purchaseInfo.setAddress1("bul. Yerusalim 5");
        purchaseInfo.setAddress2("bul. Yerusalim 6");
        purchaseInfo.setCity("Sofia");
        purchaseInfo.setZip("1000");
        purchaseInfo.setPhone("+00359894646464");
        purchaseInfo.setEmail("info@bellatrix.solutions");
        purchaseInfo.setShouldCreateAccount(true);

        var checkoutPage = app().create(CheckoutPage.class);
        checkoutPage.fillBillingInfo(purchaseInfo);
        checkoutPage.map().checkPaymentsRadioButton().click();
    }
}