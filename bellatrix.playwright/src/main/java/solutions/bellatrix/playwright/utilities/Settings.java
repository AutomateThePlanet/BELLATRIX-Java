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

package solutions.bellatrix.playwright.utilities;

import lombok.experimental.UtilityClass;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.playwright.configuration.*;

/**
 * Utility class that manages WebSettings, TimeoutSettings, GridSettings, UrlSettings, and ContextSettings. <br>
 * As there is no need to create a new *Settings object each time we get an option from the config file; <br>
 * this Utility class will create only once an instance of each Settings class when we try to access them for the first time. <br>
 * Otherwise it will return the cached Settings object.
 */
@UtilityClass
public class Settings {
    private static WebSettings webSettings;
    private static TimeoutSettings timeoutSettings;
    private static GridSettings gridSettings;
    private static UrlSettings urlSettings;
    private static ContextSettings contextSettings;

    public static WebSettings web() {
        if (webSettings != null) {
            return webSettings;
        }

        webSettings = ConfigurationService.get(WebSettings.class);

        return webSettings;
    }

    public static TimeoutSettings timeout() {
        if (timeoutSettings != null) {
            return timeoutSettings;
        }

        timeoutSettings = web().getTimeoutSettings();

        return timeoutSettings;
    }

    public static GridSettings grid() {
        if (gridSettings != null) {
            return gridSettings;
        }

        gridSettings = ConfigurationService.get(GridSettings.class);

        return gridSettings;
    }

    public static UrlSettings url() {
        if (urlSettings != null) {
            return urlSettings;
        }

        urlSettings = ConfigurationService.get(UrlSettings.class);

        return urlSettings;
    }

    public static ContextSettings context() {
        if (contextSettings != null) {
            return contextSettings;
        }

        contextSettings = web().getContextSettings();

        return contextSettings;
    }
}




