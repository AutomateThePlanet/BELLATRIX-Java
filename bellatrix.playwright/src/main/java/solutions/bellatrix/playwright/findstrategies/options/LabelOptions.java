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

public class LabelOptions extends Options<Page.GetByLabelOptions, Locator.GetByLabelOptions> {
    private void setAbsolute(Function<Page.GetByLabelOptions, Page.GetByLabelOptions> method) {
        invokeAbsolute(method);
    }

    public static LabelOptions createAbsolute(Function<Page.GetByLabelOptions, Page.GetByLabelOptions> method) {
        var options = new LabelOptions();
        options.setAbsolute(method);
        return options;
    }

    private void setRelative(Function<Locator.GetByLabelOptions, Locator.GetByLabelOptions> method) {
        invokeRelative(method);
    }

    public static LabelOptions createRelative(Function<Locator.GetByLabelOptions, Locator.GetByLabelOptions> method) {
        var options = new LabelOptions();
        options.setRelative(method);

        return options;
    }
}
