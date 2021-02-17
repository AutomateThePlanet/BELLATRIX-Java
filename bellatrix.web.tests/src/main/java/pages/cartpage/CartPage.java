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

package pages.cartpage;

import solutions.bellatrix.pages.WebPage;

public class CartPage extends WebPage<Map, Asserts> {
    @Override
    protected String getUrl() {
        return "http://demos.bellatrix.solutions/cart/";
    }

    @Override
    protected void waitForPageLoad() {
        map().couponCodeTextField().toExists().waitToBe();
    }

    public void applyCoupon(String coupon) {
        map().couponCodeTextField().setText(coupon);
        map().applyCouponButton().click();
        browser().waitForAjax();
    }

    public void increaseProductQuantity(int newQuantity) throws InterruptedException {
        map().quantityBox().setText(String.valueOf(newQuantity));
        map().updateCart().click();
        browser().waitForAjax();
    }

    public void clickProceedToCheckout() {
        map().proceedToCheckout().click();
        browser().waitUntilPageLoadsCompletely();
    }

    public String getTotal() {
        return map().totalSpan().getText();
    }

    public String getMessageNotification() {
        return map().messageAlert().getText();
    }
}
