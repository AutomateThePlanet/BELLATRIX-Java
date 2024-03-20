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

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.components.contracts.*;

public class ColorInput extends WebComponent implements ComponentDisabled, ComponentValue, ComponentColor, ComponentList, ComponentAutoComplete, ComponentRequired {
    public final static EventListener<ComponentActionEventArgs> SETTING_COLOR = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> COLOR_SET = new EventListener<>();

    @Override
    public String getColor() {
        return getValue();
    }

    @Override
    public void setColor(String value) {
        defaultSetValue(SETTING_COLOR, COLOR_SET, value);
    }

    @Override
    public boolean isAutoComplete() {
        return defaultGetAutoCompleteAttribute();
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }

    @Override
    public String getList() {
        return defaultGetList();
    }

    @Override
    public boolean isRequired() {
        return defaultGetRequiredAttribute();
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }
}
