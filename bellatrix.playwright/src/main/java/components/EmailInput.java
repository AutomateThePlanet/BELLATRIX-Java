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

import components.common.ComponentActionEventArgs;
import components.contracts.*;
import solutions.bellatrix.core.plugins.EventListener;

public class EmailInput extends WebComponent implements ComponentDisabled, ComponentValue, ComponentEmail, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxLength, ComponentMinLength, ComponentSize, ComponentPlaceholder {
    public final static EventListener<ComponentActionEventArgs> SETTING_EMAIL = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> EMAIL_SET = new EventListener<>();

    @Override
    public String getEmail() {
        return getValue();
    }

    @Override
    public void setEmail(String email) {
        defaultSetValue(SETTING_EMAIL, EMAIL_SET, email);
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
