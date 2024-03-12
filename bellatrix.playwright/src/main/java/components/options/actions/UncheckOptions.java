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

package components.options.actions;

import com.microsoft.playwright.Locator;
import components.options.Options;

import java.util.function.Function;

public class UncheckOptions extends Options<Locator.UncheckOptions> {
    protected void set(Function<Locator.UncheckOptions, Locator.UncheckOptions> options) {
        invoke(options);
    }

    public static UncheckOptions create(Function<Locator.UncheckOptions, Locator.UncheckOptions> options) {
        var uncheckOptions = new UncheckOptions();
        uncheckOptions.set(options);

        return uncheckOptions;
    }
}
