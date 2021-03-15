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
import solutions.bellatrix.android.configuration.AndroidSettings;

import java.util.HashMap;

public class AppConfiguration {
    @Getter private String appPath;
    @Getter private Lifecycle lifecycle;
    @Getter private String deviceName;
    @Getter private String appPackage;
    @Getter private String appActivity;
    @Getter private String androidVersion;
    @Getter private Boolean isMobileWebExecution;
    @Getter HashMap<String, String> appiumOptions;

    public HashMap<String, String> getAppiumOptions() {
        return appiumOptions;
    }

    public AppConfiguration(boolean isMobileWebExecution) {
        this.isMobileWebExecution = isMobileWebExecution;
    }

    public AppConfiguration(Lifecycle lifecycle, String androidVersion, String deviceName, String appPath, String appPackage, String appActivity) {
        if (StringUtil.isBlank(androidVersion)) {
            this.androidVersion = ConfigurationService.get(AndroidSettings.class).getDefaultAndroidVersion();
        } else {
            this.androidVersion = androidVersion;
        }

        if (StringUtil.isBlank(deviceName)) {
            this.deviceName = ConfigurationService.get(AndroidSettings.class).getDefaultDeviceName();
        } else {
            this.deviceName = deviceName;
        }

        if (StringUtil.isBlank(appPath)) {
            this.appPath = ConfigurationService.get(AndroidSettings.class).getDefaultAppPath();
        } else {
            this.appPath = appPath;
        }

        this.lifecycle = lifecycle;

        if (StringUtil.isBlank(appPackage)) {
            this.appPackage = ConfigurationService.get(AndroidSettings.class).getDefaultAppPackage();
        } else {
            this.appPackage = appPackage;
        }

        if (StringUtil.isBlank(appActivity)) {
            this.appActivity = ConfigurationService.get(AndroidSettings.class).getDefaultAppActivity();
        } else {
            this.appActivity = appActivity;
        }

        appiumOptions = new HashMap<>();
    }
}