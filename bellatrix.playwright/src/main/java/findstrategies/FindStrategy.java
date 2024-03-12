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
import findstrategies.options.Options;
import findstrategies.utils.By;
import findstrategies.utils.LocatorUtilities;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

@Getter @Setter
public abstract class FindStrategy {
    protected Options options;
    protected Locator locator;

    public abstract Locator convert(Page page);
    public abstract Locator convert(Locator locator);

    @SneakyThrows
    protected Locator get(Page page, By by, Object... args) {
        Method method = LocatorUtilities.getGetMethod(Page.class, by, args);
        locator = (Locator)method.invoke(page, args);
        return locator;
    }

    @SneakyThrows
    protected Locator get(Locator baseLocator, By by, Object... args) {
        Method method = LocatorUtilities.getGetMethod(Locator.class, by, args);
        locator = (Locator)method.invoke(baseLocator, args);

        return locator;
    }

    @Override
    public String toString() {
        return locator.toString();
    }
}
