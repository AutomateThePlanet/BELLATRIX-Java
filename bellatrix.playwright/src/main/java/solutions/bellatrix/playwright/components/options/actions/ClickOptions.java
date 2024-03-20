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

package solutions.bellatrix.playwright.components.options.actions;

import com.microsoft.playwright.Locator;
import solutions.bellatrix.playwright.components.options.Options;

import java.util.function.Function;

public class ClickOptions extends Options<Locator.ClickOptions> {
    protected void set(Function<Locator.ClickOptions, Locator.ClickOptions> options) {
        invoke(options);
    }

    public static ClickOptions create(Function<Locator.ClickOptions, Locator.ClickOptions> options) {
        var clickOptions = new ClickOptions();
        clickOptions.set(options);

        return clickOptions;
    }
}
