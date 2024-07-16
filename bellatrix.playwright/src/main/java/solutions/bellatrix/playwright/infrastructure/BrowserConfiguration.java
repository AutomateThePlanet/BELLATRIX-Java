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

package solutions.bellatrix.playwright.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class BrowserConfiguration {
    private Browsers browsers;
    private Lifecycle lifecycle;
    private int height;
    private int width;
    private int version;
    private String testName;
    private final HashMap<String, String> playwrightOptions;

    public BrowserConfiguration(Browsers browsers, Lifecycle browserBehavior) {
        this.browsers = browsers;
        this.lifecycle = browserBehavior;
        playwrightOptions = new HashMap<>();
    }

    public BrowserConfiguration(Browsers browsers, Lifecycle browserBehavior, String testName) {
        this.browsers = browsers;
        this.lifecycle = browserBehavior;
        this.testName = testName;
        playwrightOptions = new HashMap<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BrowserConfiguration that))
            return false;
        if (!(this.getBrowsers() == null ? that.getBrowsers() == null : this.getBrowsers().equals(that.getBrowsers())))
            return false;
        if (!(this.getLifecycle() == null ? that.getLifecycle() == null : this.getLifecycle().equals(that.getLifecycle())))
            return false;
        if (this.getVersion() != that.getVersion())
            return false;
        if (!(this.getPlaywrightOptions() == null ? that.getPlaywrightOptions() == null : this.getPlaywrightOptions().equals(that.getPlaywrightOptions())))
            return false;
        return true;
    }
}
