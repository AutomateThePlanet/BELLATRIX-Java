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

package solutions.bellatrix.android.components.listeners;

import solutions.bellatrix.android.components.AndroidComponent;
import solutions.bellatrix.android.components.Button;
import solutions.bellatrix.core.plugins.Listener;

public class BddLogging extends Listener {
    private boolean isBddLoggingTurnedOn = false;

    @Override
    public void addListener() {
        if (!isBddLoggingTurnedOn) {
            Button.CLICKING.addListener((x) -> System.out.printf("clicking %s%n", x.getComponent().getElementName()));
            AndroidComponent.VALIDATED_ATTRIBUTE.addListener((x) -> System.out.println(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}