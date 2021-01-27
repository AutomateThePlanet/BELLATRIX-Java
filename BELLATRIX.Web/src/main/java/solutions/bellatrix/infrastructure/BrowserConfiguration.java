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

package solutions.bellatrix.infrastructure;import java.util.HashMap;
import java.util.Objects;

public class BrowserConfiguration {
    private Browser browser;
    private Lifecycle lifecycle;
    private ExecutionType executionType;
    private int height;
    private int width;
    private Boolean shouldCaptureHttpTraffic;
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

    public Boolean getShouldCaptureHttpTraffic() {
        return shouldCaptureHttpTraffic;
    }

    public int getHeight() {
        return height;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public Browser getBrowser() {
        return browser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (BrowserConfiguration) o;
        return height == that.height && width == that.width && browser == that.browser && lifecycle == that.lifecycle && executionType == that.executionType && shouldCaptureHttpTraffic.equals(that.shouldCaptureHttpTraffic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(browser, lifecycle, executionType, height, width, shouldCaptureHttpTraffic);
    }
}
