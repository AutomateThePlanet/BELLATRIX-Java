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

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.SneakyThrows;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.android.configuration.GridSettings;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DriverService {
    private static final ThreadLocal<Boolean> DISPOSED;
    private static final ThreadLocal<AppConfiguration> APP_CONFIGURATION;
    private static final ThreadLocal<HashMap<String, String>> CUSTOM_DRIVER_OPTIONS;
    private static final ThreadLocal<AndroidDriver<MobileElement>> WRAPPED_ANDROID_DRIVER;

    static {
        DISPOSED = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS.set(new HashMap<>());
        APP_CONFIGURATION = new ThreadLocal<>();
        WRAPPED_ANDROID_DRIVER = new ThreadLocal<>();
        DISPOSED.set(false);
    }

    public static HashMap<String, String> getCustomDriverOptions() {
        return CUSTOM_DRIVER_OPTIONS.get();
    }

    public static void addDriverOptions(String key, String value) {
        CUSTOM_DRIVER_OPTIONS.get().put(key, value);
    }

    public static AndroidDriver<MobileElement> getWrappedAndroidDriver() {
        return WRAPPED_ANDROID_DRIVER.get();
    }

    public static AppConfiguration getAppConfiguration() {
        return APP_CONFIGURATION.get();
    }

    public static AndroidDriver<MobileElement> start(AppConfiguration configuration) {
        APP_CONFIGURATION.set(configuration);
        DISPOSED.set(false);
        AndroidDriver<MobileElement> driver;
        var androidSettings = ConfigurationService.get(AndroidSettings.class);
        var executionType = androidSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode(androidSettings.getServiceUrl());
        } else {
            var gridSettings = androidSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings.isPresent() : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get());
        }

        driver.manage().timeouts().implicitlyWait(androidSettings.getTimeoutSettings().getImplicitWaitTimeout(), TimeUnit.SECONDS);
        WRAPPED_ANDROID_DRIVER.set(driver);

        return driver;
    }

    private static AndroidDriver<MobileElement> initializeDriverGridMode(GridSettings gridSettings) {
        var caps = new DesiredCapabilities();
        caps.setCapability("platform", Platform.WIN10);
        caps.setCapability("version", "latest");

        AndroidDriver<MobileElement> driver = null;
        try {
            driver = new AndroidDriver<>(new URL(gridSettings.getUrl()), caps);
        } catch (MalformedURLException e) {
            DebugInformation.printStackTrace(e);
        }

        return driver;
    }

    @SneakyThrows
    private static AndroidDriver<MobileElement> initializeDriverRegularMode(String serviceUrl) {
        var caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, getAppConfiguration().getAndroidVersion());
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, getAppConfiguration().getDeviceName());

        if (getAppConfiguration().getIsMobileWebExecution()) {
            caps.setCapability(MobileCapabilityType.BROWSER_NAME, ConfigurationService.get(AndroidSettings.class).getDefaultBrowser());
        } else {
            caps.setCapability(MobileCapabilityType.APP, getAppConfiguration().getAppPath());
            caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, getAppConfiguration().getAppPackage());
            caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, getAppConfiguration().getAppActivity());
        }

        addDriverOptions(caps);
        var driver = new AndroidDriver<MobileElement>(new URL(serviceUrl), caps);

        return driver;
    }

    private static <TOption extends MutableCapabilities> void addGridOptions(TOption options, GridSettings gridSettings) {
        for (var entry : gridSettings.getArguments()) {
            for (var c : entry.entrySet()) {
                if (c.getKey().startsWith("env_")) {
                    var envValue = System.getProperty(c.getKey().replace("env_", ""));
                    options.setCapability(c.getKey(), envValue);
                } else {
                    options.setCapability(c.getKey(), c.getValue());
                }
            }
        }
    }

    private static <TOption extends MutableCapabilities> void addDriverOptions(TOption chromeOptions) {
        for (var optionKey : APP_CONFIGURATION.get().appiumOptions.keySet()) {
            chromeOptions.setCapability(optionKey, APP_CONFIGURATION.get().appiumOptions.get(optionKey));
        }
    }

    public static void close() {
        if (DISPOSED.get()) {
            return;
        }

        if (WRAPPED_ANDROID_DRIVER.get() != null) {
            WRAPPED_ANDROID_DRIVER.get().close();
            CUSTOM_DRIVER_OPTIONS.get().clear();
        }

        DISPOSED.set(true);
    }
}
