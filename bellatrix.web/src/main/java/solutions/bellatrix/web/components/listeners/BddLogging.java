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

package solutions.bellatrix.web.components.listeners;

import solutions.bellatrix.core.plugins.Listener;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.validations.ComponentValidator;

public class BddLogging extends Listener {
    private static boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        if (!isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> Log.info("clicking %s", x.getComponent().getComponentName()));
            Button.CLICKING.addListener((x) -> Log.info("clicking %s", x.getComponent().getComponentName()));
            CheckBox.CHECKING.addListener((x) -> Log.info("checking %s", x.getComponent().getComponentName()));
            CheckBox.UNCHECKING.addListener((x) -> Log.info("unchecking %s", x.getComponent().getComponentName()));
            ColorInput.SETTING_COLOR.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            DateField.SETTING_DATE.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            DateTimeField.SETTING_TIME.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            EmailField.SETTING_EMAIL.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            FileInput.UPLOADING.addListener((x) -> Log.info("uploading '%s' to %s", x.getActionValue(), x.getComponent().getComponentName()));
            MonthInput.SETTING_MONTH.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            NumberInput.SETTING_NUMBER.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            PasswordField.SETTING_PASSWORD.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            PhoneField.SETTING_PHONE.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            RadioButton.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getComponentName()));
            Range.SETTING_RANGE.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            Reset.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getComponentName()));
            SearchField.SETTING_SEARCH.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            Select.SELECTING.addListener((x) -> Log.info("selecting '%s' from %s", x.getActionValue(), x.getComponent().getComponentName()));
            TextArea.SETTING_TEXT.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            TextField.SETTING_TEXT.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            TimeInput.SETTING_TIME.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            UrlField.SETTING_URL.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            WeekInput.SETTING_WEEK.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            WebComponent.HOVERING.addListener((x) -> Log.info("hovering %s", x.getComponent().getComponentName()));
            WebComponent.FOCUSING.addListener((x) -> Log.info("focusing %s", x.getComponent().getComponentName()));
            WebComponent.SCROLLING_TO_VISIBLE.addListener((x) -> Log.info("scrolling to %s", x.getComponent().getComponentName()));
            WebComponent.SETTING_ATTRIBUTE.addListener((x) -> Log.info("setting %s to '%s' in %s", x.getActionValue(), x.getMessage(), x.getComponent().getComponentName()));
            ComponentValidator.VALIDATING_ATTRIBUTE.addListener((x) -> Log.info(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}