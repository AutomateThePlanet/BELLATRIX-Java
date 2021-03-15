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

package solutions.bellatrix.android.components;

import solutions.bellatrix.android.components.contracts.ComponentDisabled;
import solutions.bellatrix.android.components.contracts.ComponentNumber;
import solutions.bellatrix.android.findstrategies.ClassFindStrategy;
import solutions.bellatrix.core.plugins.EventListener;

public class NumberInput extends AndroidComponent implements ComponentDisabled, ComponentNumber {
    public final static EventListener<ComponentActionEventArgs> SETTING_NUMBER = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> NUMBER_SET = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void setNumber(Number value) {
        defaultSetText(SETTING_NUMBER, NUMBER_SET, value.toString());
    }

    @Override
    public double getNumber() {
        var resultText = defaultGetText();
        if (resultText.isEmpty()) {
            var textField = create(TextField.class, new ClassFindStrategy("android.widget.EditText"));
            resultText = textField.getText();
        }

        return Double.parseDouble(resultText);
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
