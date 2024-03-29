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
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.findstrategies.options.RoleOptions;
import solutions.bellatrix.playwright.findstrategies.utils.By;

@Getter
public class AriaRoleFindStrategy extends FindStrategy {
    private final AriaRole role;
    public AriaRoleFindStrategy(AriaRole role) {
        this.role = role;
    }
    public AriaRoleFindStrategy(AriaRole role, RoleOptions options) {
        this.role = role;
        this.options = options;
    }


    @Override
    public WebElement resolve(Page page) {
        return get(page, By.ROLE, role, ((RoleOptions)options).absolute());
    }

    @Override
    public WebElement resolve(WebElement webElement) {
        return get(webElement, By.ROLE, role, ((RoleOptions)options).absolute());

    }
}
