/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.playwright.components.shadowdom;

import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.components.contracts.ComponentHtml;
import solutions.bellatrix.playwright.utilities.Settings;

public class ShadowRoot extends WebComponent implements ComponentHtml {
    public ShadowRoot() {
        createService = new ShadowRootCreateService(this, CREATING, CREATED);
        webSettings = Settings.web();
    }

    public ShadowRoot(WebElement element) {
        this.wrappedElement = element;
        createService = new ShadowRootCreateService(this, CREATING, CREATED);
        webSettings = Settings.web();
    }

    @Override
    public ShadowRootCreateService create() {
        return (ShadowRootCreateService)createService;
    }

    /**
     * Returns the innerHTML of the shadowRoot of the shadow host.
     */
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }
}
