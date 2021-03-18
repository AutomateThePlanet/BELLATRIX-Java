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

package cartpage;


import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextField;
import solutions.bellatrix.web.pages.PageMap;

public class Map extends PageMap {
    public TextField couponCodeTextField() {
        return create().byId(TextField.class, "coupon_code");
    }

    public Button applyCouponButton() {
        return create().byCss(Button.class, "[value*='Apply coupon']");
    }

    public TextField quantityBox() {
        return create().byCss(TextField.class, "[class*='input-text qty text']");
    }

    public Button updateCart() {
        return create().byCss(Button.class, "[value*='Update cart']");
    }

    public Div messageAlert() {
        return create().byCss(Div.class, "[class*='woocommerce-message']");
    }

    public Span totalSpan() {
        return create().byXPath(Span.class, "//*[@class='order-total']//span");
    }

    public Button proceedToCheckout() {
        return create().byCss(Button.class, "[class*='checkout-button button alt wc-forward']");
    }
}
