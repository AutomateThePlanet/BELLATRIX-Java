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

package solutions.bellatrix.playwright.infrastructure.junit;

import org.junit.jupiter.api.extension.ExtendWith;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.core.plugins.junit.TestResultWatcher;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.components.listeners.BddConsoleLogging;
import solutions.bellatrix.playwright.components.listeners.BddToastNotificationsLogging;
import solutions.bellatrix.playwright.components.listeners.HighlightElements;
import solutions.bellatrix.playwright.infrastructure.BrowserLifecyclePlugin;
import solutions.bellatrix.playwright.infrastructure.LogLifecyclePlugin;
import solutions.bellatrix.playwright.infrastructure.WebScreenshotPlugin;
import solutions.bellatrix.playwright.infrastructure.WebVideoPlugin;
import solutions.bellatrix.playwright.services.App;

@ExtendWith(TestResultWatcher.class)
public class WebTest extends BaseTest {
    public App app() {
        return SingletonFactory.getInstance(App.class);
    }

    @Override
    protected void configure() {
        addPlugin(BrowserLifecyclePlugin.class);
        addPluginWithKey(ScreenshotPlugin.class, WebScreenshotPlugin.class);
        addPlugin(WebVideoPlugin.class);
        addPlugin(LogLifecyclePlugin.class);
        addListener(BddConsoleLogging.class);
        addListener(BddToastNotificationsLogging.class);
        addListener(HighlightElements.class);
    }
}
