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

package pages.CartPage;

import solutions.bellatrix.pages.NavigatableWebPage;

public class CartPage extends NavigatableWebPage<CartPageElements> {
    @Override
    protected String getUrl() {
        return "http://demos.bellatrix.solutions/cart/";
    }

    @Override
    protected void waitForPageLoad() {
        elements().couponCodeTextField().toExists(elements().couponCodeTextField()).waitToBe();
    }

    public void applyCoupon(String coupon) {
        elements().couponCodeTextField().setText(coupon);
        elements().applyCouponButton().click();
        browser().waitForAjax();
    }

    public void increaseProductQuantity(int newQuantity) throws InterruptedException {
        elements().quantityBox().setText(String.valueOf(newQuantity));
        elements().updateCart().click();
        browser().waitForAjax();
    }

    public void clickProceedToCheckout() {
        elements().proceedToCheckout().click();
        browser().waitUntilPageLoadsCompletely();
    }

    public String getTotal() {
        return elements().totalSpan().getText();
    }

    public String getMessageNotification() {
        return elements().messageAlert().getText();
    }
}
