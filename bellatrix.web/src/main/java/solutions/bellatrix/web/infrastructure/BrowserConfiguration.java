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

package solutions.bellatrix.web.infrastructure;

import org.openqa.selenium.Platform;

import java.util.HashMap;

public class BrowserConfiguration {
    private final Browser browser;
    private final Lifecycle lifecycle;
    private int height;
    private int width;
    private int version;
    private Platform platform;
    final HashMap<String, String> driverOptions;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BrowserConfiguration))
            return false;
        BrowserConfiguration that = (BrowserConfiguration)obj;
        if (!(this.getBrowser() == null ? that.getBrowser() == null : this.getBrowser().equals(that.getBrowser())))
            return false;
        if (!(this.getLifecycle() == null ? that.getLifecycle() == null : this.getLifecycle().equals(that.getLifecycle())))
            return false;
        if (this.getHeight() != that.getHeight())
            return false;
        if (this.getWidth() != that.getWidth())
            return false;
        if (this.getVersion() != that.getVersion())
            return false;
        if (!(this.getPlatform() == null ? that.getPlatform() == null : this.getPlatform().equals(that.getPlatform())))
            return false;
        if (!(this.getDriverOptions() == null ? that.getDriverOptions() == null : this.getDriverOptions().equals(that.getDriverOptions())))
            return false;
        return true;
    }
}