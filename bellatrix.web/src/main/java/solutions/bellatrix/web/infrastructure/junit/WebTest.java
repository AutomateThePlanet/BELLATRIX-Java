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

package solutions.bellatrix.web.infrastructure.junit;

import org.junit.jupiter.api.extension.ExtendWith;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.core.plugins.junit.TestResultWatcher;
import solutions.bellatrix.web.components.listeners.BddConsoleLogging;
import solutions.bellatrix.web.components.listeners.BddToastNotificationsLogging;
import solutions.bellatrix.web.components.listeners.HighlightElements;
import solutions.bellatrix.web.infrastructure.BrowserLifecyclePlugin;
import solutions.bellatrix.web.infrastructure.WebScreenshotPlugin;
import solutions.bellatrix.web.infrastructure.WebVideoPlugin;
import solutions.bellatrix.web.services.App;

@ExtendWith(TestResultWatcher.class)
public class WebTest extends BaseTest {

    public App app() {
        return new App();
    }

    @Override
    protected void configure() {
        addPlugin(BrowserLifecyclePlugin.class);
        addPlugin(WebScreenshotPlugin.class);
        addPlugin(WebVideoPlugin.class);
        addListener(BddConsoleLogging.class);
        addListener(HighlightElements.class);
        addListener(BddToastNotificationsLogging.class);
    }
}