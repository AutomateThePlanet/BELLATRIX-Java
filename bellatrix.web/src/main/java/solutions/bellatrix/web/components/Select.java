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

package solutions.bellatrix.web.components;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentReadonly;
import solutions.bellatrix.web.components.contracts.ComponentRequired;

import java.util.ArrayList;
import java.util.List;

public class Select extends WebComponent implements ComponentDisabled, ComponentRequired, ComponentReadonly {
    public final static EventListener<ComponentActionEventArgs> SELECTING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SELECTED = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public Option GetSelected() {
        var nativeSelect = new org.openqa.selenium.support.ui.Select(findElement());
        var optionComponent = InstanceFactory.create(Option.class);
        optionComponent.setFindStrategy(getFindStrategy());
        optionComponent.setElementIndex(0);
        optionComponent.setWrappedElement(nativeSelect.getFirstSelectedOption());
        return optionComponent;
    }

    public List<Option> getAllOptions() {
        var nativeSelect = new org.openqa.selenium.support.ui.Select(findElement());
        var options = new ArrayList<Option>();
        for (var nativeOption: nativeSelect.getOptions()) {
            var optionComponent = InstanceFactory.create(Option.class);
            optionComponent.setFindStrategy(getFindStrategy());
            optionComponent.setElementIndex(0);
            optionComponent.setWrappedElement(nativeOption);
            options.add(optionComponent);
        }

        return options;
    }

    public void selectByText(String text) {
        defaultSelectByText(SELECTING, SELECTED, text);
    }

    public void selectByIndex(int index) {
        defaultSelectByIndex(SELECTING, SELECTED, index);
    }

    @Override
    public boolean isDisabled() {
        return defaultGetDisabledAttribute();
    }

    @Override
    public boolean isReadonly() {
        return defaultGetReadonlyAttribute();
    }

    @Override
    public boolean isRequired() {
        return defaultGetRequiredAttribute();
    }
}
