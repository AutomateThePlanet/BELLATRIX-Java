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
import solutions.bellatrix.web.components.contracts.ComponentChecked;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentValue;

public class CheckBox extends WebComponent implements ComponentDisabled, ComponentChecked, ComponentValue {
    public final static EventListener<ComponentActionEventArgs> CHECKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CHECKED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UNCHECKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UNCHECKED = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void check() {
        defaultCheck(CHECKING, CHECKED);
    }

    public void check(boolean shouldCheck) {
        if (shouldCheck) {
            check();
        }
        else {
            uncheck();
        }
    }

    public void uncheck() {
        defaultUncheck(UNCHECKING, UNCHECKED);
    }

    @Override
    public boolean isChecked() {
        return findElement().isSelected();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public String getValue() {
        return null;
    }
}
