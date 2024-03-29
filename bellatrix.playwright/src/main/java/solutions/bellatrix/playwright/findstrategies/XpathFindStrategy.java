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

@Getter
public class XpathFindStrategy extends FindStrategy {
    private final String value;
    public XpathFindStrategy(String value) {
       this.value = "xpath=" + value;
    }

    @Override
    public WebElement resolve(Page page) {
        return get(page, By.LOCATOR, value);

    }

    @Override
    public WebElement resolve(WebElement webElement) {
        return get(webElement, By.LOCATOR, value);
    }
}
