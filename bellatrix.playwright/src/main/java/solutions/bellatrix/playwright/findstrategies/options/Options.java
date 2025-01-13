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

import solutions.bellatrix.core.utilities.ConverterService;

import java.util.function.Function;

public abstract class Options<AbsoluteT, RelativeT> {
    protected AbsoluteT pageOptions;
    protected RelativeT locatorOptions;

    public AbsoluteT absolute() {
        return pageOptions;
    }

    public RelativeT relative() {
        return locatorOptions;
    }

    protected Options<AbsoluteT, RelativeT> invokeAbsolute(Function<AbsoluteT, AbsoluteT> method) {
        if (method == null) return null;
        method.apply(pageOptions);
        locatorOptions = ConverterService.convert(pageOptions, locatorOptions);

        return this;
    }

    protected Options<AbsoluteT, RelativeT> invokeRelative(Function<RelativeT, RelativeT> method) {
        if (method == null) return null;
        method.apply(locatorOptions);
        pageOptions = ConverterService.convert(locatorOptions, pageOptions);

        return this;
    }
}
