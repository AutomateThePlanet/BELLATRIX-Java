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

public class TimeInput extends WebComponent implements ComponentDisabled, ComponentValue, ComponentTime, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxText, ComponentMinText, ComponentStep {
    public final static EventListener<ComponentActionEventArgs> SETTING_TIME = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> TIME_SET = new EventListener<>();

    @Override
    public String getTime() {
        return null;
    }

    @Override
    public void setTime(int hours, int minutes) {
        defaultSetValue(SETTING_TIME, TIME_SET, String.format("%d:%d:00", hours, minutes));
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
}