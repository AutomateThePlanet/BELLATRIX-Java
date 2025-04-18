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

package solutions.bellatrix.android.infrastructure;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.PathNormalizer;

import java.util.HashMap;

public class AppConfiguration {
    @Getter private String appPath;
    @Getter private Lifecycle lifecycle;
    @Getter private String deviceName;
    @Getter private String appPackage;
    @Getter private String appActivity;
    @Getter private String androidVersion;
    @Setter @Getter private String testName;
    @Getter private final Boolean isMobileWebExecution;
    @Getter HashMap<String, Object> appiumOptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppConfiguration that)) return false;
        return Objects.equal(appPath, that.appPath) && lifecycle == that.lifecycle && Objects.equal(deviceName, that.deviceName) && Objects.equal(appPackage, that.appPackage) && Objects.equal(appActivity, that.appActivity) && Objects.equal(androidVersion, that.androidVersion) && Objects.equal(isMobileWebExecution, that.isMobileWebExecution);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appPath, lifecycle, deviceName, appPackage, appActivity, androidVersion, isMobileWebExecution, appiumOptions);
    }

    public AppConfiguration(boolean isMobileWebExecution) {
        this.isMobileWebExecution = isMobileWebExecution;
    }

    public AppConfiguration(Lifecycle lifecycle, String androidVersion, String deviceName, String appPath, String appPackage, String appActivity) {
        if (StringUtils.isBlank(androidVersion)) {
            this.androidVersion = ConfigurationService.get(AndroidSettings.class).getDefaultAndroidVersion();
        } else {
            this.androidVersion = androidVersion;
        }

        if (StringUtils.isBlank(deviceName)) {
            this.deviceName = ConfigurationService.get(AndroidSettings.class).getDefaultDeviceName();
        } else {
            this.deviceName = deviceName;
        }

        if (StringUtils.isBlank(appPath)) {
            this.appPath = PathNormalizer.normalizePath(ConfigurationService.get(AndroidSettings.class).getDefaultAppPath());
        } else {
            this.appPath = PathNormalizer.normalizePath(appPath);
        }

        this.lifecycle = lifecycle;

        if (StringUtils.isBlank(appPackage)) {
            this.appPackage = ConfigurationService.get(AndroidSettings.class).getDefaultAppPackage();
        } else {
            this.appPackage = appPackage;
        }

        if (StringUtils.isBlank(appActivity)) {
            this.appActivity = ConfigurationService.get(AndroidSettings.class).getDefaultAppActivity();
        } else {
            this.appActivity = appActivity;
        }

        this.isMobileWebExecution = false;

        appiumOptions = new HashMap<>();
    }
}