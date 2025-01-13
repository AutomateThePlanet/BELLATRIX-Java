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

package solutions.bellatrix.playwright.components;

import com.microsoft.playwright.Locator;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.components.contracts.ComponentChecked;
import solutions.bellatrix.playwright.components.contracts.ComponentDisabled;
import solutions.bellatrix.playwright.components.contracts.ComponentValue;
import solutions.bellatrix.playwright.components.options.actions.CheckOptions;
import solutions.bellatrix.playwright.components.options.actions.UncheckOptions;

import java.util.function.Function;

public class CheckBox extends WebComponent implements ComponentDisabled, ComponentChecked, ComponentValue {
    public final static EventListener<ComponentActionEventArgs> CHECKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CHECKED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UNCHECKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UNCHECKED = new EventListener<>();

    public void check() {
        defaultCheck(CHECKING, CHECKED);
    }

    public void uncheck() {
        defaultUncheck(UNCHECKING, UNCHECKED);
    }

    public void check(Function<Locator.CheckOptions, Locator.CheckOptions> options) {
        var checkOptions = CheckOptions.create(options);
        defaultCheck(checkOptions, CHECKING, CHECKED);
    }

    public void uncheck(Function<Locator.UncheckOptions, Locator.UncheckOptions> options) {
        var uncheckOptions = UncheckOptions.create(options);
        defaultUncheck(uncheckOptions, CHECKING, CHECKED);
    }

    @Override
    public boolean isChecked() {
        return wrappedElement.isChecked();
    }

    @Override
    public boolean isDisabled() {
        return wrappedElement.isDisabled();
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }
}
