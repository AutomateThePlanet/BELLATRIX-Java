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

package solutions.bellatrix.mobile.infrastructure;

import java.util.HashMap;

public class AppConfiguration {
    private String appPath;
    private Lifecycle lifecycle;
    private int height;
    private int width;
    HashMap<String, String> appiumOptions;

    public HashMap<String, String> getAppiumOptions() {
        return appiumOptions;
    }

    public AppConfiguration(Lifecycle browserBehavior, String appPath) {
        this.appPath = appPath;
        this.lifecycle = browserBehavior;
        appiumOptions = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public String getAppPath() {
        return appPath;
    }
}