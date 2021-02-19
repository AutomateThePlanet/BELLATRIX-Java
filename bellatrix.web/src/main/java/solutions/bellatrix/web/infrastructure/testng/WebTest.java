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

package solutions.bellatrix.web.infrastructure.testng;

import solutions.bellatrix.web.components.listeners.BddLogging;
import solutions.bellatrix.web.components.listeners.HighlightElements;
import solutions.bellatrix.web.infrastructure.BrowserLifecyclePlugin;
import solutions.bellatrix.web.infrastructure.WebScreenshotPlugin;
import solutions.bellatrix.web.infrastructure.WebVideoPlugin;
import solutions.bellatrix.core.plugins.testng.BaseTest;
import solutions.bellatrix.web.services.App;

public class WebTest extends BaseTest {

    public App app() {
        return new App();
    }

    @Override
    protected void configure() {
        addPlugin(BrowserLifecyclePlugin.of());
        addPlugin(WebScreenshotPlugin.of());
        addPlugin(WebVideoPlugin.of());
        BddLogging.addPlugin();
        HighlightElements.addPlugin();
    }
}