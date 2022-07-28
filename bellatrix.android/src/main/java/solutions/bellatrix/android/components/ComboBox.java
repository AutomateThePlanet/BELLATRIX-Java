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

package solutions.bellatrix.android.components;

import solutions.bellatrix.android.components.contracts.ComponentDisabled;
import solutions.bellatrix.android.components.contracts.ComponentText;
import solutions.bellatrix.android.findstrategies.ClassFindStrategy;
import solutions.bellatrix.android.findstrategies.TextContainingFindStrategy;
import solutions.bellatrix.core.plugins.EventListener;

public class ComboBox extends AndroidComponent implements ComponentDisabled, ComponentText {

    public final static EventListener<ComponentActionEventArgs> SELECTING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SELECTED = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getText() {
        var result = defaultGetText();
        if (result.isEmpty()) {
            var textField = create(TextField.class, new ClassFindStrategy("android.widget.TextView"));
            result = textField.getText();
        }

        return result;
    }

    public void selectByText(String value) {
        SELECTING.broadcast(new ComponentActionEventArgs(this));

        if (!findElement().getText().equals(value)) {
            findElement().click();
            var innerElementToClick = create(RadioButton.class, new TextContainingFindStrategy(value));
            innerElementToClick.click();
        }

        SELECTED.broadcast(new ComponentActionEventArgs(this));
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }
}
