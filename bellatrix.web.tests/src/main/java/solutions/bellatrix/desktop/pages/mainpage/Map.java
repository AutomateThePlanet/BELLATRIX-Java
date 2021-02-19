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

package solutions.bellatrix.desktop.pages.mainpage;

import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.pages.PageMap;

public class Map extends PageMap {

    public Anchor addToCartFalcon9() {
        return create().byCss(Anchor.class, "[data-product_id*='28']");
    }

    public Button viewCartButton() {
        return create().byCss(Button.class, "[class*='added_to_cart wc-forward']");
    }

    public Anchor getProductBoxByName(String name) {
        return create().byXPath(Anchor.class, String.format("//h2[text()='%s']/parent::a[1]", name));
    }
}
