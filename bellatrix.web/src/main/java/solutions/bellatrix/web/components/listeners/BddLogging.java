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

public class BddLogging extends Listener {
    private static boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        if (!isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> Log.info("clicking %s", x.getComponent().getElementName()));
            Button.CLICKING.addListener((x) -> Log.info("clicking %s", x.getComponent().getElementName()));
            CheckBox.CHECKING.addListener((x) -> Log.info("checking %s", x.getComponent().getElementName()));
            CheckBox.UNCHECKING.addListener((x) -> Log.info("unchecking %s", x.getComponent().getElementName()));
            ColorInput.SETTING_COLOR.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            DateInput.SETTING_DATE.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            DateTimeInput.SETTING_TIME.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            EmailInput.SETTING_EMAIL.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            FileInput.UPLOADING.addListener((x) -> Log.info("uploading '%s' to %s", x.getActionValue(), x.getComponent().getElementName()));
            MonthInput.SETTING_MONTH.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            NumberInput.SETTING_NUMBER.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            PasswordInput.SETTING_PASSWORD.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            PhoneInput.SETTING_PHONE.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            RadioButton.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            Range.SETTING_RANGE.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            Reset.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            SearchInput.SETTING_SEARCH.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            Select.SELECTING.addListener((x) -> Log.info("selecting '%s' from %s", x.getActionValue(), x.getComponent().getElementName()));
            TextArea.SETTING_TEXT.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            TextField.SETTING_TEXT.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            TimeInput.SETTING_TIME.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            UrlInput.SETTING_URL.addListener((x) -> Log.info("typing '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            WeekInput.SETTING_WEEK.addListener((x) -> Log.info("setting '%s' in %s", x.getActionValue(), x.getComponent().getElementName()));
            WebComponent.VALIDATED_ATTRIBUTE.addListener((x) -> System.out.println(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}