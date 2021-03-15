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

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.android.configuration.GridSettings;
import solutions.bellatrix.android.configuration.IOSSettings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DriverService {
    private static ThreadLocal<Boolean> disposed;
    private static ThreadLocal<AppConfiguration> appConfiguration;
    private static ThreadLocal<HashMap<String, String>> customDriverOptions;
    private static ThreadLocal<IOSDriver<MobileElement>> wrappedIOSDriver;

    static {
        disposed = new ThreadLocal<>();
        customDriverOptions = new ThreadLocal<>();
        customDriverOptions.set(new HashMap<>());
        appConfiguration = new ThreadLocal<>();
        wrappedIOSDriver = new ThreadLocal<>();
        disposed.set(false);
    }

    public static HashMap<String, String> getCustomDriverOptions() {
        return customDriverOptions.get();
    }

    public static void addDriverOptions(String key, String value) {
        customDriverOptions.get().put(key, value);
    }

    public static IOSDriver<MobileElement> getWrappedIOSDriver() {
        return wrappedIOSDriver.get();
    }

    public static AppConfiguration getAppConfiguration() {
        return appConfiguration.get();
    }

    public static IOSDriver<MobileElement> start(AppConfiguration configuration) {
        appConfiguration.set(configuration);
        disposed.set(false);
        IOSDriver<MobileElement> driver = null;
        var IOSSettings = ConfigurationService.get(solutions.bellatrix.android.configuration.IOSSettings.class);
        var executionType = IOSSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode(IOSSettings.getServiceUrl());
        } else {
            var gridSettings = IOSSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings != null : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get());
        }

        driver.manage().timeouts().implicitlyWait(IOSSettings.getTimeoutSettings().getImplicitWaitTimeout(), TimeUnit.SECONDS);
        wrappedIOSDriver.set(driver);

        return driver;
    }

    private static IOSDriver<MobileElement> initializeDriverGridMode(GridSettings gridSettings) {
        var caps = new DesiredCapabilities();
        caps.setCapability("platform", Platform.WIN10);
        caps.setCapability("version", "latest");

        IOSDriver<MobileElement> driver = null;
        try {
            driver = new IOSDriver<MobileElement>(new URL(gridSettings.getUrl()), caps);
        } catch (MalformedURLException e) {
            DebugInformation.printStackTrace(e);
        }

        return driver;
    }

    @SneakyThrows
    private static IOSDriver<MobileElement> initializeDriverRegularMode(String serviceUrl) {
        var caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "IOS");
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, getAppConfiguration().getIosVersion());
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, getAppConfiguration().getDeviceName());

        if (getAppConfiguration().getIsMobileWebExecution()) {
            caps.setCapability(MobileCapabilityType.BROWSER_NAME, ConfigurationService.get(IOSSettings.class).getDefaultBrowser());
        } else {
            caps.setCapability(MobileCapabilityType.APP, getAppConfiguration().getAppPath());
        }

        addDriverOptions(caps);
        var driver = new IOSDriver<MobileElement>(new URL(serviceUrl), caps);

        return driver;
    }

    private static <TOption extends MutableCapabilities> void addGridOptions(TOption options, GridSettings gridSettings) {
        for (var entry:gridSettings.getArguments()) {
            for (var c:entry.entrySet()) {
                if (c.getKey().startsWith("env_")) {
                    var envValue = System.getProperty(c.getKey().replace("env_", "")) ;
                    options.setCapability(c.getKey(), envValue);
                } else {
                    options.setCapability(c.getKey(), c.getValue());
                }
            }
        }
    }

    private static <TOption extends MutableCapabilities> void addDriverOptions(TOption chromeOptions) {
        for (var optionKey:appConfiguration.get().appiumOptions.keySet()) {
            chromeOptions.setCapability(optionKey, appConfiguration.get().appiumOptions.get(optionKey));
        }
    }

    public static void close() {
        if (disposed.get()) {
            return;
        }

        if (wrappedIOSDriver.get() != null) {
            wrappedIOSDriver.get().close();
            customDriverOptions.get().clear();
        }

        disposed.set(true);
    }
}
