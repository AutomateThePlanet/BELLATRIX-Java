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

package findstrategies;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import findstrategies.utils.By;
import lombok.Getter;

@Getter
public class CssFindStrategy extends FindStrategy {
    private final String value;
    public CssFindStrategy(String value) {
        this.value = "css=" + value;
    }

    @Override
    public Locator convert(Page page) {
        return get(page, By.LOCATOR, value, new Page.LocatorOptions());

    }

    @Override
    public Locator convert(Locator baseLocator) {
        return get(baseLocator, By.LOCATOR, value, new Locator.LocatorOptions());
    }
}
