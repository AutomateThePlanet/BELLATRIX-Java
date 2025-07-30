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


import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import lombok.SneakyThrows;
import org.openqa.selenium.MutableCapabilities;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.ios.configuration.GridSettings;
import solutions.bellatrix.ios.configuration.IOSSettings;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

public class DriverService {
    private static final ThreadLocal<Boolean> DISPOSED;
    private static final ThreadLocal<AppConfiguration> APP_CONFIGURATION;
    private static final ThreadLocal<HashMap<String, Object>> CUSTOM_DRIVER_OPTIONS;
    private static final ThreadLocal<IOSDriver> WRAPPED_IOS_DRIVER;

    static {
        DISPOSED = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS.set(new HashMap<>());
        APP_CONFIGURATION = new ThreadLocal<>();
        WRAPPED_IOS_DRIVER = new ThreadLocal<>();
        DISPOSED.set(false);
    }

    public static HashMap<String, Object> getCustomDriverOptions() {
        return CUSTOM_DRIVER_OPTIONS.get();
    }

    public static void addDriverOptions(String key, String value) {
        CUSTOM_DRIVER_OPTIONS.get().put(key, value);
    }

    public static IOSDriver getWrappedIOSDriver() {
        return WRAPPED_IOS_DRIVER.get();
    }

    public static AppConfiguration getAppConfiguration() {
        return APP_CONFIGURATION.get();
    }

    public static IOSDriver start(AppConfiguration configuration) {
        APP_CONFIGURATION.set(configuration);
        DISPOSED.set(false);
        IOSDriver driver;
        var IOSSettings = ConfigurationService.get(solutions.bellatrix.ios.configuration.IOSSettings.class);
        var executionType = IOSSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode(IOSSettings.getServiceUrl());
        } else {
            var gridSettings = IOSSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings.isPresent() : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get());
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IOSSettings.getTimeoutSettings().getImplicitWaitTimeout()));
        WRAPPED_IOS_DRIVER.set(driver);
        solutions.bellatrix.web.infrastructure.DriverService.setWrappedDriver(driver);

        return driver;
    }

    private static IOSDriver initializeDriverGridMode(GridSettings gridSettings) {
        var caps = new XCUITestOptions();
        caps.setPlatformName(MobilePlatform.IOS);
        caps.setAutomationName(AutomationName.IOS_XCUI_TEST);
        caps.setPlatformVersion(getAppConfiguration().getIosVersion());
        caps.setDeviceName(getAppConfiguration().getDeviceName());
        caps.setUdid(ConfigurationService.get(IOSSettings.class).getDeviceIdentifier());
        caps.setWebviewConnectTimeout(Duration.ofSeconds(ConfigurationService.get(IOSSettings.class).getTimeoutSettings().getWebviewConnectTimeout()));

        try {
            var driver = new IOSDriver(new URL(gridSettings.getUrl()), caps);
            solutions.bellatrix.web.infrastructure.DriverService.setWrappedDriver(driver);
            return driver;
        } catch (MalformedURLException e) {
            DebugInformation.printStackTrace(e);
            return null;
        }
    }

    @SneakyThrows
    private static IOSDriver initializeDriverRegularMode(String serviceUrl) {
        var caps = new XCUITestOptions();
        caps.setPlatformName(MobilePlatform.IOS);
        caps.setAutomationName(AutomationName.IOS_XCUI_TEST);
        caps.setPlatformVersion(getAppConfiguration().getIosVersion());
        caps.setDeviceName(getAppConfiguration().getDeviceName());
        caps.setUdid(ConfigurationService.get(IOSSettings.class).getDeviceIdentifier());
        caps.setWebviewConnectTimeout(Duration.ofSeconds(ConfigurationService.get(IOSSettings.class).getTimeoutSettings().getWebviewConnectTimeout()));

        if (getAppConfiguration().getIsMobileWebExecution()) {
            caps.withBrowserName(ConfigurationService.get(IOSSettings.class).getDefaultBrowser());
        } else {
            caps.setApp(getAppConfiguration().getAppPath().replace("\\", "/"));
        }

        if (Objects.requireNonNullElse(ConfigurationService.get(IOSSettings.class).getAllowImageFindStrategies(), false))
            caps.setCapability("use-plugins", "images");

        addDriverOptions(caps);
        var driver = new IOSDriver(new URL(serviceUrl), caps);
        solutions.bellatrix.web.infrastructure.DriverService.setWrappedDriver(driver);
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

        if (WRAPPED_IOS_DRIVER.get() != null) {
            WRAPPED_IOS_DRIVER.get().quit();
        }

        DISPOSED.set(true);
    }
}
