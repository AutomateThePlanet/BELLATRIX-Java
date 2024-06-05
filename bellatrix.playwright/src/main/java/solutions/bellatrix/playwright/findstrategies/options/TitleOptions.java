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

public class TitleOptions extends Options<Page.GetByTitleOptions, Locator.GetByTitleOptions> {
    private void setAbsolute(Function<Page.GetByTitleOptions, Page.GetByTitleOptions> method) {
        invokeAbsolute(method);
    }

    public static TitleOptions createAbsolute(Function<Page.GetByTitleOptions, Page.GetByTitleOptions> method) {
        var options = new TitleOptions();
        options.setAbsolute(method);
        return options;
    }

    private void setRelative(Function<Locator.GetByTitleOptions, Locator.GetByTitleOptions> method) {
        invokeRelative(method);
    }

    public static TitleOptions createRelative(Function<Locator.GetByTitleOptions, Locator.GetByTitleOptions> method) {
        var options = new TitleOptions();
        options.setRelative(method);

        return options;
    }
}
