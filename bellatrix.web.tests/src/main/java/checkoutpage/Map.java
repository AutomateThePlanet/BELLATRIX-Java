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

package checkoutpage;

import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.pages.PageMap;

public class Map extends PageMap {
    public TextField billingFirstName() {
        return create().byId(TextField.class, "billing_first_name");
    }

    public TextField billingLastName() {
        return create().byId(TextField.class, "billing_last_name");
    }

    public TextField billingCompany() {
        return create().byId(TextField.class, "billing_company");
    }

    public Button billingCountryWrapper() {
        return create().byId(Button.class, "select2-billing_country-container");
    }

    public TextField billingCountryFilter() {
        return create().byClass(TextField.class, "select2-search__field");
    }

    public TextField billingAddress1() {
        return create().byId(TextField.class, "billing_address_1");
    }

    public TextField billingAddress2() {
        return create().byId(TextField.class, "billing_address_2");
    }

    public TextField billingCity() {
        return create().byId(TextField.class, "billing_city");
    }

    public TextField billingZip() {
        return create().byId(TextField.class, "billing_postcode");
    }

    public TextField billingPhone() {
        return create().byId(TextField.class, "billing_phone");
    }

    public TextField billingEmail() {
        return create().byId(TextField.class, "billing_email");
    }

    public CheckBox createAccountCheckBox() {
        return create().byId(CheckBox.class, "createaccount");
    }

    public RadioButton checkPaymentsRadioButton() {
        return create().byCss(RadioButton.class, "[for*='payment_method_cheque']");
    }

    public Button placeOrderButton() {
        return create().byId(Button.class, "place_order");
    }

    public Heading receivedMessage() {
        return create().byXPath(Heading.class, "//h1");
    }

    public Button getCountryOptionByName(String countryName) {
        return create().byXPath(Button.class, String.format("//li[contains(text(),'%s')]", countryName));
    }
}
