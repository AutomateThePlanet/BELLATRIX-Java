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
import components.contracts.ComponentDisabled;
import components.contracts.ComponentReadonly;
import components.contracts.ComponentRequired;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;

import java.util.List;


public class Select extends WebComponent implements ComponentDisabled, ComponentRequired, ComponentReadonly {
    public final static EventListener<ComponentActionEventArgs> SELECTING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SELECTED = new EventListener<>();

    public Option getSelected() {
        String optionValue = (String)this.wrappedElement.evaluate("selectElement => {" +
                "    const selectedOption = selectElement.options[selectElement.selectedIndex];" +
                "    return selectedOption.getAttribute('value');" +
                "}"
        );

        return this.create().byXpath(Option.class, String.format("//option[@value='%s']", optionValue));
    }

    public List<Option> getAllOptions() {
        try {
            return this.create().allByXpath(Option.class, "//option");
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            return null;
        }
    }

    public void selectByText(String text) {
        defaultSelectByText(SELECTING, SELECTED, text);
    }

    public void selectByValue(String value) {
        defaultSelectByValue(SELECTING, SELECTED, value);
    }

    public void selectByIndex(int index) {
        defaultSelectByIndex(SELECTING, SELECTED, index);
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }

    @Override
    public boolean isReadonly() {
        return defaultGetReadonlyAttribute();
    }

    @Override
    public boolean isRequired() {
        return defaultGetRequiredAttribute();
    }
}