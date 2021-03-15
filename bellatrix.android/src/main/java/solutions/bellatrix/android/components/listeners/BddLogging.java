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

import solutions.bellatrix.android.components.Anchor;
import solutions.bellatrix.android.components.AndroidComponent;

public class BddLogging {
    private static boolean isBddLoggingTurnedOn = false;
    public static void addPlugin() {
        if (!isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener((x) -> System.out.println(String.format("clicking %s\n", x.getComponent().getElementName())));
            AndroidComponent.VALIDATED_ATTRIBUTE.addListener((x) -> System.out.println(x.getMessage()));
            isBddLoggingTurnedOn = true;
        }
    }
}