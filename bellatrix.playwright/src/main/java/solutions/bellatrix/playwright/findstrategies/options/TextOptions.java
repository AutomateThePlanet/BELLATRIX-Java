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

package solutions.bellatrix.playwright.findstrategies.options;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.function.Function;

public class TextOptions extends Options<Page.GetByTextOptions, Locator.GetByTextOptions> {
    private void setAbsolute(Function<Page.GetByTextOptions, Page.GetByTextOptions> method) {
        invokeAbsolute(method);
    }

    public static TextOptions createAbsolute(Function<Page.GetByTextOptions, Page.GetByTextOptions> method) {
        var options = new TextOptions();
        options.setAbsolute(method);
        return options;
    }

    private void setRelative(Function<Locator.GetByTextOptions, Locator.GetByTextOptions> method) {
        invokeRelative(method);
    }

    public static TextOptions createRelative(Function<Locator.GetByTextOptions, Locator.GetByTextOptions> method) {
        var options = new TextOptions();
        options.setRelative(method);

        return options;
    }
}
