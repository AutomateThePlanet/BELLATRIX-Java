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

package solutions.bellatrix.android.infrastructure;

import lombok.Getter;
import org.jsoup.internal.StringUtil;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.android.configuration.IOSSettings;

import java.util.HashMap;

public class AppConfiguration {
    @Getter private String appPath;
    @Getter private Lifecycle lifecycle;
    @Getter private String deviceName;
    @Getter private String iosVersion;
    @Getter private Boolean isMobileWebExecution;
    @Getter HashMap<String, String> appiumOptions;

    public HashMap<String, String> getAppiumOptions() {
        return appiumOptions;
    }

    public AppConfiguration(boolean isMobileWebExecution) {
        this.isMobileWebExecution = isMobileWebExecution;
    }

    public AppConfiguration(Lifecycle lifecycle, String IOSVersion, String deviceName, String appPath) {
        if (StringUtil.isBlank(IOSVersion)) {
            this.iosVersion = ConfigurationService.get(IOSSettings.class).getDefaultIOSVersion();
        } else {
            this.iosVersion = IOSVersion;
        }

        if (StringUtil.isBlank(deviceName)) {
            this.deviceName = ConfigurationService.get(IOSSettings.class).getDefaultDeviceName();
        } else {
            this.deviceName = deviceName;
        }

        if (StringUtil.isBlank(appPath)) {
            this.appPath = ConfigurationService.get(IOSSettings.class).getDefaultAppPath();
        } else {
            this.appPath = appPath;
        }

        this.lifecycle = lifecycle;

        appiumOptions = new HashMap<>();
    }
}