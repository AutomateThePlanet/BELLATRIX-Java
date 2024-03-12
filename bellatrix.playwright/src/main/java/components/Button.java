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

package components;

import com.microsoft.playwright.Locator;
import components.common.ComponentActionEventArgs;
import components.contracts.ComponentDisabled;
import components.contracts.ComponentText;
import components.contracts.ComponentValue;
import components.options.actions.ClickOptions;
import solutions.bellatrix.core.plugins.EventListener;

import java.util.function.Function;

public class Button extends WebComponent implements ComponentValue, ComponentDisabled, ComponentText {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }

    public void click(Function<Locator.ClickOptions, Locator.ClickOptions> options) {
        var clickOptions = ClickOptions.create(options);
        defaultClick(clickOptions, CLICKING, CLICKED);
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
