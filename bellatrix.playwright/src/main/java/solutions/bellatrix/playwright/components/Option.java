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

import solutions.bellatrix.playwright.components.contracts.ComponentDisabled;
import solutions.bellatrix.playwright.components.contracts.ComponentSelected;
import solutions.bellatrix.playwright.components.contracts.ComponentText;
import solutions.bellatrix.playwright.components.contracts.ComponentValue;

public class Option extends WebComponent implements ComponentText, ComponentValue, ComponentDisabled, ComponentSelected {
    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }

    @Override
    public boolean isSelected() {
        return (boolean)wrappedElement.evaluate("el => el.selected");
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }

    private boolean equals(Option option) {
        return this.getText().equals(option.getText()) &&
                this.getValue().equals(option.getValue());
    }
}
