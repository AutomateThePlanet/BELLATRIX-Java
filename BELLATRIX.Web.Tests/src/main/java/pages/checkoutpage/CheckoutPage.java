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

package pages.checkoutpage;

import solutions.bellatrix.pages.WebPage;

public class CheckoutPage extends WebPage<Components, Asserts> {
    @Override
    protected String getUrl() {
        return "http://demos.bellatrix.solutions/checkout/";
    }

    public void fillBillingInfo(PurchaseInfo purchaseInfo) throws InterruptedException {
        elements().billingFirstName().setText(purchaseInfo.getFirstName());
        elements().billingLastName().setText(purchaseInfo.getLastName());
        elements().billingCompany().setText(purchaseInfo.getCompany());
        elements().billingCountryWrapper().click();
        elements().billingCountryFilter().setText(purchaseInfo.getCountry());
        elements().getCountryOptionByName(purchaseInfo.getCountry()).click();
        elements().billingAddress1().setText(purchaseInfo.getAddress1());
        elements().billingAddress2().setText(purchaseInfo.getAddress2());
        elements().billingCity().setText(purchaseInfo.getCity());
        elements().billingZip().setText(purchaseInfo.getZip());
        elements().billingPhone().setText(purchaseInfo.getPhone());
        elements().billingEmail().setText(purchaseInfo.getEmail());
        if (purchaseInfo.getShouldCreateAccount()) {
            elements().createAccountCheckBox().check();
        }

        if (purchaseInfo.getShouldCheckPayment()) {
            elements().checkPaymentsRadioButton().click();
        }

        elements().placeOrderButton().click();
        browser().waitForAjax();
    }
}
