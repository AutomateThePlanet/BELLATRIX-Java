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

public class DateInput extends WebComponent implements ComponentDisabled, ComponentValue, ComponentDate, ComponentAutoComplete, ComponentRequired, ComponentReadonly, ComponentMaxText, ComponentMinText, ComponentStep {
    public final static EventListener<ComponentActionEventArgs> SETTING_DATE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> DATE_SET = new EventListener<>();

    @Override
    public String getDate() {
        return getValue();
    }

    @Override
    public void setDate(int year, int month, int day) {
        defaultSetDate(year, month, day);
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
    public String getMax() {
        return defaultGetMaxAttribute();
    }

    @Override
    public String getMin() {
        return defaultGetMinAttribute();
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
    public double getStep() {
        return Double.parseDouble(defaultGetStepAttribute());
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }

    protected void defaultSetDate(int year, int month, int day) {
        if (year <= 0)
            throw new IllegalArgumentException(String.format("The year should be a positive number but you specified: %d", year));
        if (month <= 0 || month > 12)
            throw new IllegalArgumentException(String.format("The month should be between 0 and 12 but you specified: %d", month));
        if (day <= 0 || day > 31)
            throw new IllegalArgumentException(String.format("The day should be between 0 and 31 but you specified: %d", day));

        String valueToBeSet;

        if (month < 10) {
            valueToBeSet = String.format("%d-0%d", year, month);
        } else {
            valueToBeSet = String.format("%d-%d", year, month);
        }

        if (day < 10) {
            valueToBeSet = String.format("%s-0%d", valueToBeSet, day);
        } else {
            valueToBeSet = String.format("%s-%d", valueToBeSet, day);
        }

        defaultSetValue(SETTING_DATE, DATE_SET, valueToBeSet);
    }
}
