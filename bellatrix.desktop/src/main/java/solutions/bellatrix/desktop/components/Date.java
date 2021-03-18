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

package solutions.bellatrix.desktop.components;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.desktop.components.contracts.ComponentDate;
import solutions.bellatrix.desktop.components.contracts.ComponentDisabled;

public class Date extends DesktopComponent implements ComponentDisabled, ComponentDate {
    public final static EventListener<ComponentActionEventArgs> SETTING_DATE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> DATE_SET = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void setDate(int year, int month, int day) {
        if (year <= 0) {
            throw new IllegalArgumentException(String.format("The year should be a positive number but you specified: %d", year));
        }

        if (month <= 0 || month > 12) {
            throw new IllegalArgumentException(String.format("The month should be between 0 and 12 but you specified: %d", month));
        }

        if (day <= 0 || day > 31) {
            throw new IllegalArgumentException(String.format("The day should be between 0 and 31 but you specified: %d", day));
        }

        String valueToBeSet;

        if (month < 10) {
            valueToBeSet = String.format("0%d\\%d", month, year);
        } else {
            valueToBeSet = String.format("%d\\%d", month, year);
        }

        if (day < 10) {
            valueToBeSet = String.format("%s-0%d", valueToBeSet, day);
        } else {
            valueToBeSet = String.format("%s-%d", valueToBeSet, day);
        }

        defaultSetText(SETTING_DATE, DATE_SET, valueToBeSet);
    }

    @Override
    public String getDate() {
        return defaultGetText();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
