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

package components.options.states;

import com.microsoft.playwright.Locator;
import components.options.Options;

import java.util.function.Function;

public class EnabledOptions extends Options<Locator.IsEnabledOptions> {

    protected void set(Function<Locator.IsEnabledOptions, Locator.IsEnabledOptions> options) {
        invoke(options);
    }

    public static EnabledOptions create(Function<Locator.IsEnabledOptions, Locator.IsEnabledOptions> options) {
        var enabledOptions = new EnabledOptions();
        enabledOptions.set(options);

        return enabledOptions;
    }
}