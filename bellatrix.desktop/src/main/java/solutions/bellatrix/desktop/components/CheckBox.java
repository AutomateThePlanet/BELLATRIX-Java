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

package solutions.bellatrix.desktop.components;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.desktop.components.contracts.ComponentChecked;
import solutions.bellatrix.desktop.components.contracts.ComponentDisabled;

public class CheckBox extends DesktopComponent implements ComponentDisabled, ComponentChecked {
    public final static EventListener<ComponentActionEventArgs> CHECKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CHECKED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UNCHECKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UNCHECKED = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void check() {
        if (!findElement().isSelected()) {
            defaultClick(CHECKING, CHECKED);
        }
    }

    public void uncheck() {
        if (findElement().isSelected()) {
            defaultClick(UNCHECKING, UNCHECKED);
        }
    }

    @Override
    public boolean isChecked() {
        return findElement().isSelected();
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
