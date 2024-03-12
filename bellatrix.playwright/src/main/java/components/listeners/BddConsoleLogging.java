/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package components.listeners;

import components.*;
import components.common.validate.ComponentValidator;
import solutions.bellatrix.core.plugins.Listener;
import solutions.bellatrix.core.utilities.Log;

public class BddConsoleLogging extends Listener {
    private static boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        if (!isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> Log.info("clicking %s", x.component().getComponentName()));
            Button.CLICKING.addListener((x) -> Log.info("clicking %s", x.component().getComponentName()));
            CheckBox.CHECKING.addListener((x) -> Log.info("checking %s", x.component().getComponentName()));
            CheckBox.UNCHECKING.addListener((x) -> Log.info("unchecking %s", x.component().getComponentName()));
            ColorInput.SETTING_COLOR.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            DateInput.SETTING_DATE.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            DateTimeInput.SETTING_TIME.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            EmailInput.SETTING_EMAIL.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            FileInput.UPLOADING.addListener((x) -> Log.info("uploading '%s' to %s", x.actionValue(), x.component().getComponentName()));
            MonthInput.SETTING_MONTH.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            NumberInput.SETTING_NUMBER.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            PasswordInput.SETTING_PASSWORD.addListener((x) -> Log.info("typing '*****' in %s", x.component().getComponentName()));
            PhoneInput.SETTING_PHONE.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            RadioButton.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.component().getComponentName()));
            Range.SETTING_RANGE.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            Reset.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.component().getComponentName()));
            SearchField.SETTING_SEARCH.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            Select.SELECTING.addListener((x) -> Log.info("selecting '%s' from %s", x.actionValue(), x.component().getComponentName()));
            TextArea.SETTING_TEXT.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            TextInput.SETTING_TEXT.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            TimeInput.SETTING_TIME.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            UrlField.SETTING_URL.addListener((x) -> Log.info("typing '%s' in %s", x.actionValue(), x.component().getComponentName()));
            WeekInput.SETTING_WEEK.addListener((x) -> Log.info("setting '%s' in %s", x.actionValue(), x.component().getComponentName()));
            WebComponent.HOVERING.addListener((x) -> Log.info("hovering %s", x.component().getComponentName()));
            WebComponent.FOCUSING.addListener((x) -> Log.info("focusing %s", x.component().getComponentName()));
            WebComponent.SCROLLING_TO_VISIBLE.addListener((x) -> Log.info("scrolling to %s", x.component().getComponentName()));
            WebComponent.SETTING_ATTRIBUTE.addListener((x) -> Log.info("setting %s to '%s' in %s", x.actionValue(), x.message(), x.component().getComponentName()));
            ComponentValidator.VALIDATING_ATTRIBUTE.addListener((x) -> Log.info(x.message()));
            isBddLoggingTurnedOn = true;
        }
    }
}
