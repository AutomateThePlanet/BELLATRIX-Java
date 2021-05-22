/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package junit;

import cartpage.CartPage;
import checkoutpage.CheckoutPage;
import checkoutpage.PurchaseInfo;
import mainpage.MainPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.findstrategies.TextContains;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

//@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class ProductPurchaseTests extends WebTest {
    @Test
    public void completePurchaseSuccessfully_first() {
        app().navigate().to("http://demos.bellatrix.solutions/");
        var addToCartFalcon9 = app().create().byCss(Anchor.class, "[data-product_id*='28']");
        var blogLink = app().create().by(Anchor.class, TextContains.by("Blog"));
        addToCartFalcon9.click();
        blogLink.above(addToCartFalcon9).validate();
        new MainPage().asserts().productBoxLink("Falcon 9", "http://demos.bellatrix.solutions/product/falcon-9/");
    }

    @Test
    public void completePurchaseSuccessfully_second() {
        app().navigate().to("http://demos.bellatrix.solutions/");
        var addToCartFalcon9 = app().create().byCss(Anchor.class, "[data-product_id*='28']");
        addToCartFalcon9.click();
    }

    @Test
    public void falcon9LinkAddsCorrectProduct() {
        var mainPage = app().goTo(MainPage.class);

        mainPage.asserts().productBoxLink("Falcon 9", "http://demos.bellatrix.solutions/product/falcon-9/");
    }

    @Test
    public void saturnVLinkAddsCorrectProduct() {
        var mainPage = app().goTo(MainPage.class);

        mainPage.asserts().productBoxLink("Saturn V", "http://demos.bellatrix.solutions/product/saturn-v/");
    }

    @Test
    public void purchaseFalcon9WithoutFacade() {
        var mainPage = app().goTo(MainPage.class);
        mainPage.addRocketToShoppingCart("Falcon 9");

        var cartPage = app().create(CartPage.class);
        cartPage.applyCoupon("happybirthday");
        cartPage.asserts().couponAppliedSuccessfully();
        cartPage.increaseProductQuantity(2);
        cartPage.asserts().totalPrice("114.00€");
        cartPage.clickProceedToCheckout();

        var purchaseInfo = new PurchaseInfo();
        purchaseInfo.setEmail("info@berlinspaceflowers.com");
        purchaseInfo.setFirstName("Anton");
        purchaseInfo.setLastName("Angelov");
        purchaseInfo.setCompany("Space Flowers");
        purchaseInfo.setCountry("Germany");
        purchaseInfo.setAddress1("1 Willi Brandt Avenue Tiergarten");
        purchaseInfo.setAddress2("Lützowplatz 17");
        purchaseInfo.setCity("Berlin");
        purchaseInfo.setZip("10115");
        purchaseInfo.setPhone("+00498888999281");

        var checkoutPage = app().create(CheckoutPage.class);
        checkoutPage.fillBillingInfo(purchaseInfo);
        checkoutPage.asserts().orderReceived();
    }

    @Test
    public void purchaseSaturnVWithoutFacade() {
        var mainPage = app().goTo(MainPage.class);
        mainPage.addRocketToShoppingCart("Saturn V");

        var cartPage = app().create(CartPage.class);
        cartPage.applyCoupon("happybirthday");
        cartPage.asserts().couponAppliedSuccessfully();
        cartPage.increaseProductQuantity(3);
        cartPage.asserts().totalPrice("355.00€");
        cartPage.clickProceedToCheckout();

        var purchaseInfo = new PurchaseInfo();
        purchaseInfo.setEmail("info@berlinspaceflowers.com");
        purchaseInfo.setFirstName("Anton");
        purchaseInfo.setLastName("Angelov");
        purchaseInfo.setCompany("Space Flowers");
        purchaseInfo.setCountry("Germany");
        purchaseInfo.setAddress1("1 Willi Brandt Avenue Tiergarten");
        purchaseInfo.setAddress2("Lützowplatz 17");
        purchaseInfo.setCity("Berlin");
        purchaseInfo.setZip("10115");
        purchaseInfo.setPhone("+00498888999281");

        var checkoutPage = app().create(CheckoutPage.class);
        checkoutPage.fillBillingInfo(purchaseInfo);
        checkoutPage.asserts().orderReceived();
    }
}
