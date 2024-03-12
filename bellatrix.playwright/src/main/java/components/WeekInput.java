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

public class WeekInput extends WebComponent implements ComponentDisabled, ComponentValue, ComponentWeek, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxText, ComponentMinText, ComponentStep {
    public final static EventListener<ComponentActionEventArgs> SETTING_WEEK = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> WEEK_SET = new EventListener<>();

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

    @Override
    public String getWeek() {
        return getValue();
    }

    @Override
    public void setWeek(int year, int weekNumber) {
        defaultSetWeek(year, weekNumber);
    }

    protected void defaultSetWeek(int year, int weekNumber) {
        if (weekNumber <= 0 || weekNumber > 52)
            throw new IllegalArgumentException(String.format("The week number should be between 0 and 53 but you specified: %d", weekNumber));
        if (year <= 0)
            throw new IllegalArgumentException(String.format("The year should be a positive number but you specified: %d", year));

        String valueToBeSet;

        if (weekNumber < 10) {
            valueToBeSet = String.format("%d-W0%d", year, weekNumber);
        } else {
            valueToBeSet = String.format("%d-W%d", year, weekNumber);
        }
        
        defaultSetValue(SETTING_WEEK, WEEK_SET, valueToBeSet);
    }
}
