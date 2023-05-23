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

package solutions.bellatrix.web.components.listeners;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Listener;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.services.BrowserService;
import solutions.bellatrix.web.validations.ComponentValidator;

public class BddToastNotificationsLogging extends Listener {
    private static boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        isBddLoggingTurnedOn = ConfigurationService.get(WebSettings.class).getToastNotificationBddLogging();
        if (isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> new BrowserService().injectInfoNotificationToast("clicking %s", x.getComponent().getComponentName()));
            Button.CLICKING.addListener((x) -> new BrowserService().injectInfoNotificationToast("clicking %s", x.getComponent().getComponentName()));
            CheckBox.CHECKING.addListener((x) -> new BrowserService().injectInfoNotificationToast("checking %s", x.getComponent().getComponentName()));
            CheckBox.UNCHECKING.addListener((x) -> new BrowserService().injectInfoNotificationToast("unchecking %s", x.getComponent().getComponentName()));
            ColorInput.SETTING_COLOR.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            DateInput.SETTING_DATE.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            DateTimeInput.SETTING_TIME.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            EmailInput.SETTING_EMAIL.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            FileInput.UPLOADING.addListener((x) -> new BrowserService().injectInfoNotificationToast("uploading '%s' to %s", x.getActionValue(), x.getComponent().getComponentName()));
            MonthInput.SETTING_MONTH.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            NumberInput.SETTING_NUMBER.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            PasswordInput.SETTING_PASSWORD.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            PhoneInput.SETTING_PHONE.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            RadioButton.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getComponentName()));
            Range.SETTING_RANGE.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            Reset.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getComponentName()));
            SearchField.SETTING_SEARCH.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            Select.SELECTING.addListener((x) -> new BrowserService().injectInfoNotificationToast("selecting '%s' from %s", x.getActionValue(), x.getComponent().getComponentName()));
            TextArea.SETTING_TEXT.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            TextInput.SETTING_TEXT.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            TimeInput.SETTING_TIME.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            UrlField.SETTING_URL.addListener((x) -> new BrowserService().injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            WeekInput.SETTING_WEEK.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            WebComponent.HOVERING.addListener((x) -> new BrowserService().injectInfoNotificationToast("hovering %s", x.getComponent().getComponentName()));
            WebComponent.FOCUSING.addListener((x) -> new BrowserService().injectInfoNotificationToast("focusing %s", x.getComponent().getComponentName()));
            WebComponent.SCROLLING_TO_VISIBLE.addListener((x) -> new BrowserService().injectInfoNotificationToast("scrolling to %s", x.getComponent().getComponentName()));
            WebComponent.SETTING_ATTRIBUTE.addListener((x) -> new BrowserService().injectInfoNotificationToast("setting %s to '%s' in %s", x.getActionValue(), x.getMessage(), x.getComponent().getComponentName()));
            ComponentValidator.VALIDATING_ATTRIBUTE.addListener((x) -> new BrowserService().injectInfoNotificationToast(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}
