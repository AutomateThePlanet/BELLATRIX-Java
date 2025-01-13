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

package solutions.bellatrix.playwright.components.listeners;

import lombok.Getter;
import solutions.bellatrix.core.plugins.Listener;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.components.common.validate.ComponentValidator;
import solutions.bellatrix.playwright.services.BrowserService;
import solutions.bellatrix.playwright.utilities.Settings;

import java.util.Objects;

@Getter
public class BddToastNotificationsLogging extends Listener {
    private static boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        isBddLoggingTurnedOn = Settings.web().getToastNotificationBddLogging();

        if (isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("clicking %s", x.getComponent().getComponentName()));
            Button.CLICKING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("clicking %s", x.getComponent().getComponentName()));
            CheckBox.CHECKING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("checking %s", x.getComponent().getComponentName()));
            CheckBox.UNCHECKING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("unchecking %s", x.getComponent().getComponentName()));
            ColorInput.SETTING_COLOR.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            DateInput.SETTING_DATE.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            DateTimeInput.SETTING_TIME.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            EmailInput.SETTING_EMAIL.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            FileInput.UPLOADING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("uploading '%s' to %s", x.getActionValue(), x.getComponent().getComponentName()));
            MonthInput.SETTING_MONTH.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            NumberInput.SETTING_NUMBER.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            PasswordInput.SETTING_PASSWORD.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            PhoneInput.SETTING_PHONE.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            RadioButton.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getComponentName()));
            Range.SETTING_RANGE.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            Reset.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getComponentName()));
            SearchField.SETTING_SEARCH.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            Select.SELECTING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("selecting '%s' from %s", x.getActionValue(), x.getComponent().getComponentName()));
            TextArea.SETTING_TEXT.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            TextInput.SETTING_TEXT.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            TimeInput.SETTING_TIME.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            UrlField.SETTING_URL.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("typing '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            WeekInput.SETTING_WEEK.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting '%s' in %s", x.getActionValue(), x.getComponent().getComponentName()));
            WebComponent.HOVERING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("hovering %s", x.getComponent().getComponentName()));
            WebComponent.FOCUSING.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("focusing %s", x.getComponent().getComponentName()));
            WebComponent.SCROLLING_TO_VISIBLE.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("scrolling to %s", x.getComponent().getComponentName()));
            WebComponent.SETTING_ATTRIBUTE.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast("setting %s to '%s' in %s", x.getActionValue(), x.getMessage(), x.getComponent().getComponentName()));
            ComponentValidator.VALIDATING_ATTRIBUTE.addListener((x) -> Objects.requireNonNull(SingletonFactory.getInstance(BrowserService.class)).injectInfoNotificationToast(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}
