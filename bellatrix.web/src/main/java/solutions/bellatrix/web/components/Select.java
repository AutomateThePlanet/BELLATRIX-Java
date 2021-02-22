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

import solutions.bellatrix.web.core.utilities.InstanceFactory;

import java.util.ArrayList;
import java.util.List;

public class Select extends WebComponent {
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
        new org.openqa.selenium.support.ui.Select(findElement()).selectByVisibleText(text);
    }

    public void selectByIndex(int index) {
        new org.openqa.selenium.support.ui.Select(findElement()).selectByIndex(index);
    }

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }
}
