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
import solutions.bellatrix.desktop.components.contracts.ComponentDisabled;
import solutions.bellatrix.desktop.components.contracts.ComponentText;

public class ComboBox extends DesktopComponent implements ComponentDisabled, ComponentText {
    public final static EventListener<ComponentActionEventArgs> SELECTING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SELECTED = new EventListener<>();

    public void selectByText(String value) {
        defaultSelectByText(SELECTING, SELECTED, value);
    }

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
