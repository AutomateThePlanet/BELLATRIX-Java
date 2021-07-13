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

package solutions.bellatrix.ios.components;

import solutions.bellatrix.ios.findstrategies.ClassFindStrategy;

import java.util.List;

public class RadioGroup extends IOSComponent {
    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void clickByText(String text) {
        var allRadioButtons = getAll();
        for (var radioButton : allRadioButtons) {
            if (radioButton.getText().equals(text)) {
                radioButton.click();
                break;
            }
        }
    }

    public void clickByIndex(int index) {
        var allRadioButtons = getAll();
        if (index > allRadioButtons.size() - 1)
            throw new IllegalArgumentException(String.format("Only %d radio buttons were present which is less than the specified index = %d.", allRadioButtons.size(), index));

        int currentIndex = 0;
        for (var radioButton : allRadioButtons) {
            if (currentIndex == index) {
                radioButton.click();
                break;
            }
            currentIndex++;
        }
    }

    public RadioButton getChecked() {
        return createByXPath(RadioButton.class, "//*[@checked='true']");
    }

    public List<RadioButton> getAll() {
        return createAll(RadioButton.class, new ClassFindStrategy("XCUIElementTypeRadioButton"));
    }
}
