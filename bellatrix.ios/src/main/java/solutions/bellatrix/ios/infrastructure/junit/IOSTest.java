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

package solutions.bellatrix.ios.infrastructure.junit;

import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.ios.components.listeners.BddLogging;
import solutions.bellatrix.ios.infrastructure.AppLifecyclePlugin;
import solutions.bellatrix.ios.infrastructure.DownloadDemoAppsPlugin;
import solutions.bellatrix.ios.infrastructure.MobileScreenshotPlugin;
import solutions.bellatrix.ios.infrastructure.MobileVideoPlugin;
import solutions.bellatrix.ios.services.App;

public class IOSTest extends BaseTest {
    public App app() {
        return new App();
    }

    @Override
    protected void configure() {
        addPlugin(AppLifecyclePlugin.of());
        addPlugin(MobileScreenshotPlugin.of());
        addPlugin(MobileVideoPlugin.of());
        addPlugin(DownloadDemoAppsPlugin.of());
        BddLogging.addPlugin();
    }
}