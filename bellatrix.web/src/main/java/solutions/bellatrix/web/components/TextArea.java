/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.components;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.web.components.contracts.*;

public class TextArea extends WebComponent implements ComponentText, ComponentValue, ComponentDisabled, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxLength, ComponentMinLength, ComponentRows, ComponentCols, ComponentPlaceholder, ComponentSpellCheck, ComponentWrap {
    public final static EventListener<ComponentActionEventArgs> SETTING_TEXT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> TEXT_SET = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getText() {
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
    public int getCols() {
        return Integer.parseInt(defaultGetColsAttribute());
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
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
    public int getRows() {
        return Integer.parseInt(defaultGetRowsAttribute());
    }

    @Override
    public boolean isSpellCheck() {
        return defaultGetAutoCompleteAttribute();
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }

    @Override
    public int getWrap() {
        return Integer.parseInt(defaultGetWrapAttribute());
    }
}