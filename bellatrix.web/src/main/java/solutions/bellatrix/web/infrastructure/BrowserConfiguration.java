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

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Platform;

import java.util.HashMap;
import java.util.Objects;

public class BrowserConfiguration {
    @Setter @Getter private Browser browser;
    @Setter @Getter private DeviceName deviceName;
    @Setter @Getter private Lifecycle lifecycle;
    @Setter @Getter private int height;
    @Setter @Getter private int width;
    @Setter @Getter private int version;
    @Setter @Getter private Platform platform;
    @Setter @Getter private String testName;
    final HashMap<String, Object> driverOptions;

    public HashMap<String, Object> getDriverOptions() {
        return driverOptions;
    }

    public BrowserConfiguration(Browser browser, Lifecycle browserBehavior) {
        this.browser = browser;
        this.lifecycle = browserBehavior;
        driverOptions = new HashMap<>();
    }

    public BrowserConfiguration(Browser browser, DeviceName deviceName, Lifecycle browserBehavior) {
        this.deviceName = deviceName;
        this.browser = browser;
        this.lifecycle = browserBehavior;
        driverOptions = new HashMap<>();
    }

    public BrowserConfiguration(Browser browser, Lifecycle browserBehavior, Integer browserWidth, Integer browserHeight) {
        this.browser = browser;
        this.lifecycle = browserBehavior;
        this.width = browserWidth;
        this.height = browserHeight;
        driverOptions = new HashMap<>();
    }

    public BrowserConfiguration(Browser browser, Lifecycle browserBehavior, String testName) {
        this.browser = browser;
        this.lifecycle = browserBehavior;
        this.testName = testName;
        driverOptions = new HashMap<>();
    }

    public BrowserConfiguration(DeviceName deviceName, Lifecycle browserBehavior, String testName) {
        this.browser = Browser.CHROME_MOBILE;
        this.lifecycle = browserBehavior;
        this.testName = testName;
        this.deviceName = deviceName;
        driverOptions = new HashMap<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BrowserConfiguration that = (BrowserConfiguration) obj;

        if (!Objects.equals(this.getBrowser(), that.getBrowser())) return false;
        if (!Objects.equals(this.deviceName, that.deviceName)) return false;
        if (!Objects.equals(this.getLifecycle(), that.getLifecycle())) return false;
        if (this.getHeight() != that.getHeight()) return false;
        if (this.getWidth() != that.getWidth()) return false;
        if (this.getVersion() != that.getVersion()) return false;
        if (!Objects.equals(this.getPlatform(), that.getPlatform())) return false;
        return Objects.equals(this.getDriverOptions(), that.getDriverOptions());
    }
}