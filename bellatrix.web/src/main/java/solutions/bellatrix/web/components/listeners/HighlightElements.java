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

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.components.WebComponent;

public class HighlightElements {
    private static boolean isHighlightElementsAdded = false;
    public static void addPlugin() {
        if (!isHighlightElementsAdded) {
            var shouldHighlightElements = ConfigurationService.get(WebSettings.class).getShouldHighlightElements();
            if (shouldHighlightElements) {
                WebComponent.RETURNING_WRAPPED_ELEMENT.addListener((x) -> x.getComponent().highlight());
            }

            isHighlightElementsAdded = true;
        }
    }
}