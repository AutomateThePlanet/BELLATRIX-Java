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

package solutions.bellatrix.ios.infrastructure;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.ios.configuration.IOSSettings;

import java.util.HashMap;

public class AppConfiguration {
    @Getter private String appPath;
    @Getter private Lifecycle lifecycle;
    @Getter private String deviceName;
    @Getter private String iosVersion;
    @Getter private Boolean isMobileWebExecution;
    @Getter HashMap<String, Object> appiumOptions;

    public AppConfiguration(boolean isMobileWebExecution) {
        this.isMobileWebExecution = isMobileWebExecution;
    }

    public AppConfiguration(Lifecycle lifecycle, String iOSVersion, String deviceName, String appPath) {
        if (StringUtils.isBlank(iOSVersion)) {
            this.iosVersion = ConfigurationService.get(IOSSettings.class).getDefaultIOSVersion();
        } else {
            this.iosVersion = iOSVersion;
        }

        if (StringUtils.isBlank(deviceName)) {
            this.deviceName = ConfigurationService.get(IOSSettings.class).getDefaultDeviceName();
        } else {
            this.deviceName = deviceName;
        }

        if (StringUtils.isBlank(appPath)) {
            this.appPath = ConfigurationService.get(IOSSettings.class).getDefaultAppPath();
        } else {
            this.appPath = appPath;
        }

        this.lifecycle = lifecycle;
        this.isMobileWebExecution = false;

        appiumOptions = new HashMap<>();
    }
}