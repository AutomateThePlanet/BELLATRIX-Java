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

package solutions.bellatrix.web.infrastructure;

import org.openqa.selenium.Platform;

import java.util.HashMap;

public class BrowserConfiguration {
    private Browser browser;
    private Lifecycle lifecycle;
    private int height;
    private int width;
    private int version;
    private Platform platform;
    HashMap<String, String> driverOptions;

    public HashMap<String, String> getDriverOptions() {
        return driverOptions;
    }

    public BrowserConfiguration(Browser browser, Lifecycle browserBehavior) {
        this.browser = browser;
        this.lifecycle = browserBehavior;
        driverOptions = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVersion() {
        return version;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public Browser getBrowser() {
        return browser;
    }
}