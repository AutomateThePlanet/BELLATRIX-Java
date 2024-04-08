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

public class TextInput extends WebComponent implements ComponentDisabled, ComponentText, ComponentHtml, ComponentValue, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxLength, ComponentMinLength, ComponentSize, ComponentPlaceholder {
    public final static EventListener<ComponentActionEventArgs> SETTING_TEXT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> TEXT_SET = new EventListener<>();

    @Override
    public String getInnerText() {
        String text = defaultGetText();

        if (text.isEmpty()) {
            return defaultGetValue();
        }

        return text;
    }

    public void setText(String value) {
        defaultSetText(SETTING_TEXT, TEXT_SET, value);
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
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    @Override
    public int getMaxLength() {
        return Integer.parseInt(defaultGetMaxLength());
    }

    @Override
    public int getMinLength() {
        return Integer.parseInt(defaultGetMinLength());
    }

    @Override
    public String getPlaceholder() {
        return defaultGetPlaceholderAttribute();
    }

    @Override
    public boolean isReadonly() {
        return defaultGetReadonlyAttribute();
    }

    @Override
    public boolean isRequired() {
        return defaultGetRequiredAttribute();
    }

    @Override
    public int getSizeAttribute() {
        return Integer.parseInt(defaultGetSizeAttribute());
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }
}