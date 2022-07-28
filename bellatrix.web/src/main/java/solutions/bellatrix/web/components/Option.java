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

import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentSelected;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.components.contracts.ComponentValue;

public class Option extends WebComponent implements ComponentText, ComponentValue, ComponentDisabled, ComponentSelected {
    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }

    @Override
    public boolean isSelected() {
        return findElement().isSelected();
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public String getValue() {
        return defaultGetValue();
    }
}
