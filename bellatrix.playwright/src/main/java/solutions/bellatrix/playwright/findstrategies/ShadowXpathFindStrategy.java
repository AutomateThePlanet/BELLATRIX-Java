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

package solutions.bellatrix.playwright.findstrategies;

import com.microsoft.playwright.Page;
import lombok.Getter;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.findstrategies.utils.By;

/**
 * Strategy that uses CSS to locate elements, but saves the original XPath for debugging purposes. <br>
 */
@Getter
public class ShadowXpathFindStrategy extends CssFindStrategy {
    private final String originalXpath;

    public ShadowXpathFindStrategy(String xpath, String cssLocator) {
        super(cssLocator);
        originalXpath = xpath;
    }

    @Override
    public String toString() {
        return String.format("xpath = %s ; css = %s", originalXpath, getValue());
    }
}
