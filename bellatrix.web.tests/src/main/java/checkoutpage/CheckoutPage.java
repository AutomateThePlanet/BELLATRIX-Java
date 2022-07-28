/*
 * Copyright 2022 Automate The Planet Ltd.
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

package checkoutpage;

import solutions.bellatrix.web.pages.WebPage;

public class CheckoutPage extends WebPage<Map, Asserts> {
    @Override
    protected String getUrl() {
        return "http://demos.bellatrix.solutions/checkout/";
    }

    public void fillBillingInfo(PurchaseInfo purchaseInfo) {
        map().billingFirstName().setText(purchaseInfo.getFirstName());
        map().billingLastName().setText(purchaseInfo.getLastName());
        map().billingCompany().setText(purchaseInfo.getCompany());
        map().billingCountryWrapper().click();
        map().billingCountryFilter().setText(purchaseInfo.getCountry());
        map().getCountryOptionByName(purchaseInfo.getCountry()).click();
        map().billingAddress1().setText(purchaseInfo.getAddress1());
        map().billingAddress2().setText(purchaseInfo.getAddress2());
        map().billingCity().setText(purchaseInfo.getCity());
        map().billingZip().setText(purchaseInfo.getZip());
        map().billingPhone().setText(purchaseInfo.getPhone());
        map().billingEmail().setText(purchaseInfo.getEmail());
        if (purchaseInfo.getShouldCreateAccount()) {
            map().createAccountCheckBox().check();
        }

        if (purchaseInfo.getShouldCheckPayment()) {
            map().checkPaymentsRadioButton().click();
        }

        map().placeOrderButton().click();
        browser().waitForAjax();
    }
}
