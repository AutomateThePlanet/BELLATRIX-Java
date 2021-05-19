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
import solutions.bellatrix.web.components.*;

public class BddLogging extends Listener {
    private static boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        if (!isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            Button.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            CheckBox.CHECKING.addListener((x) -> System.out.printf("checking %s%n", x.getComponent().getElementName()));
            CheckBox.UNCHECKING.addListener((x) -> System.out.printf("unchecking %s%n", x.getComponent().getElementName()));
            ColorInput.SETTING_COLOR.addListener((x) -> System.out.printf("setting '%s' in %s%n", x.getActionValue(), x.getComponent().getElementName()));
            DateInput.SETTING_DATE.addListener((x) -> System.out.printf("setting '%s' in %s%n", x.getActionValue(), x.getComponent().getElementName()));
            DateTimeInput.SETTING_TIME.addListener((x) -> System.out.printf("setting '%s' in %s%n", x.getActionValue(), x.getComponent().getElementName()));
            EmailInput.SETTING_EMAIL.addListener((x) -> System.out.printf("typing '%s' in %s%n", x.getActionValue(), x.getComponent().getElementName()));
            FileInput.UPLOADING.addListener((x) -> System.out.printf("uploading '%s' to %s%n", x.getActionValue(), x.getComponent().getElementName()));
            Reset.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            RadioButton.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            WebComponent.VALIDATED_ATTRIBUTE.addListener((x) -> System.out.println(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}