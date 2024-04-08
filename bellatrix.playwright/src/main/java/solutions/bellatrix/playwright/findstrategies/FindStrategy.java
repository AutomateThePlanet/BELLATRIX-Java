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

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.findstrategies.options.Options;
import solutions.bellatrix.playwright.findstrategies.utils.By;
import solutions.bellatrix.playwright.findstrategies.utils.LocatorUtilities;
import solutions.bellatrix.playwright.infrastructure.WrappedBrowser;
import solutions.bellatrix.playwright.components.common.webelement.FrameElement;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;

import java.lang.reflect.Method;

@Getter @Setter
public abstract class FindStrategy implements Cloneable {
    protected Options options;
    protected WebElement webElement;

    public abstract WebElement convert(Page page);
    public abstract WebElement convert(WebElement locator);

    @SneakyThrows
    protected WebElement get(Page page, By by, Object... args) {
        Method method = LocatorUtilities.getGetMethod(Page.class, by, args);

        if (by == By.FRAME_LOCATOR) {
            webElement = new FrameElement((Locator)method.invoke(page, args));
        } else {
            webElement = new WebElement((Locator)method.invoke(page, args));
        }

        return webElement;
    }

    @SneakyThrows
    protected WebElement get(WebElement baseLocator, By by, Object... args) {
        if (baseLocator == null) {
            return get(SingletonFactory.getInstance(WrappedBrowser.class).getCurrentPage(), by, args);
        }

        Method method = LocatorUtilities.getGetMethod(WebElement.class, by, args);

        if (by == By.FRAME_LOCATOR) {
            webElement = new FrameElement((WebElement)method.invoke(baseLocator, args));
        } else {
            webElement = (WebElement)method.invoke(baseLocator, args);
        }

        return webElement;
    }

    @Override
    public String toString() {
        return webElement.toString();
    }

    public String getValue() {
        return toString();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
