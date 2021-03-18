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

package solutions.bellatrix.ios.components;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.ios.components.contracts.ComponentDisabled;
import solutions.bellatrix.ios.components.contracts.ComponentNumber;

public class NumberInput extends IOSComponent implements ComponentDisabled, ComponentNumber {
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
        var resultText = defaultGetValueAttribute();
        return Double.parseDouble(resultText);
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
