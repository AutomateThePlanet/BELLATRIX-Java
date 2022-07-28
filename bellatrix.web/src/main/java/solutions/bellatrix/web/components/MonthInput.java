/*
 * Copyright 2022 Automate The Planet Ltd.
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

public class MonthInput extends WebComponent implements ComponentDisabled, ComponentValue, ComponentMonth, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxText, ComponentMinText, ComponentStep {
    public final static EventListener<ComponentActionEventArgs> SETTING_MONTH = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> MONTH_SET = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getMonth() {
        return getValue();
    }

    @Override
    public void setMonth(int year, int monthNumber) {
        defaultSetMonth(year, monthNumber);
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

    protected void defaultSetMonth(int year, int monthNumber) {
        if (monthNumber <= 0 || monthNumber > 12)
            throw new IllegalArgumentException(String.format("The month number should be between 0 and 12 but you specified: %d", monthNumber));
        if (year <= 0)
            throw new IllegalArgumentException(String.format("The year should be a positive number but you specified: %d", year));

        String valueToBeSet;

        if (monthNumber < 10) {
            valueToBeSet = String.format("%d-0%d", year, monthNumber);
        } else {
            valueToBeSet = String.format("%d-%d", year, monthNumber);
        }
        setValue(SETTING_MONTH, MONTH_SET, valueToBeSet);
    }
}
