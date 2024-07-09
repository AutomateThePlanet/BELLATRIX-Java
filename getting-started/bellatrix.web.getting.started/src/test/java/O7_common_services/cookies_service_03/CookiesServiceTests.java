package O7_common_services.cookies_service_03;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class CookiesServiceTests extends WebTest {
    @Test
    public void getAllCookies() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");
        Cookie cart1 = new Cookie("woocommerce_items_in_cart1", "3");
        Cookie cart2 = new Cookie("woocommerce_items_in_cart2", "3");
        Cookie cart3 = new Cookie("woocommerce_items_in_cart3", "3");

        // BELLATRIX gives you an interface for easier work with cookies using the cookie's method. You need to make sure
        // that you have navigated to the desired web page.
        // Add a new cookie.
        app().cookies().addCookie(cart1);
        app().cookies().addCookie(cart2);
        app().cookies().addCookie(cart3);

        // Get all cookies.
        var cookies = app().cookies().getAllCookies();

        Assert.assertEquals(cookies.size(), 3);
    }

    @Test
    public void getSpecificCookie() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");
        Cookie inCartCookie = new Cookie("woocommerce_items_in_cart", "3");
        app().cookies().addCookie(inCartCookie);

        // Get a specific cookie by name.
        var itemsInCartCookie = app().cookies().getCookie("woocommerce_items_in_cart");

        Assert.assertEquals(itemsInCartCookie.getValue(), "3");
    }

    @Test
    public void deleteAllCookies() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var protonRocketAddToCartBtn = app().create().allByInnerTextContaining(Anchor.class, "Add to cart").
                stream().findFirst().orElse(null);

        protonRocketAddToCartBtn.click();

        // Delete all cookies.
        app().cookies().deleteAllCookies();
    }

    @Test
    public void deleteSpecificCookie() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        var protonRocketAddToCartBtn = app().create().allByInnerTextContaining(Anchor.class, "Add to cart").stream().findFirst().orElse(null);
        protonRocketAddToCartBtn.click();

        // Delete a specific cookie by name.
        app().cookies().deleteCookie("woocommerce_items_in_cart");
    }

    @Test
    public void addNewCookie() {
        app().navigate().to("http://demos.bellatrix.solutions/welcome/");

        Cookie cookie = new Cookie("woocommerce_items_in_cart", "3");
        app().cookies().addCookie(cookie);
    }
}