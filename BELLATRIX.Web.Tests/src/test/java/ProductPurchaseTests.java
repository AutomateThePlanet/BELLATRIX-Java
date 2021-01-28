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

import org.testng.annotations.Test;
import solutions.bellatrix.components.Anchor;
import solutions.bellatrix.infrastructure.Browser;
import solutions.bellatrix.infrastructure.ExecutionBrowser;
import solutions.bellatrix.infrastructure.Lifecycle;
import solutions.bellatrix.infrastructure.WebTest;

@ExecutionBrowser(browser = Browser.CHROME, browserBehavior = Lifecycle.REUSE_IF_STARTED)
public class ProductPurchaseTests extends WebTest {
    @Test
    public void completePurchaseSuccessfully_first() {
        getApp().getNavigationService().open("http://demos.bellatrix.solutions/");
        var addToCartFalcon9 = getApp().getComponentCreationService().createByCss(Anchor.class,"[data-product_id*='28']");
        addToCartFalcon9.click();
    }

    @Test
    public void completePurchaseSuccessfully_second() {
        getApp().getNavigationService().open("http://demos.bellatrix.solutions/");
        var addToCartFalcon9 = getApp().getComponentCreationService().createByCss(Anchor.class,"[data-product_id*='28']");
        addToCartFalcon9.click();
    }
}