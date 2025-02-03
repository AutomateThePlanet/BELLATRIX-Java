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

package solutions.bellatrix.desktop.infrastructure;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.options.WindowsOptions;
import lombok.SneakyThrows;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.configuration.GridSettings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

public class DriverService {
    private static final ThreadLocal<Boolean> DISPOSED;
    private static final ThreadLocal<AppConfiguration> APP_CONFIGURATION;
    private static final ThreadLocal<HashMap<String, Object>> CUSTOM_DRIVER_OPTIONS;
    private static final ThreadLocal<WindowsDriver> WRAPPED_DRIVER;

    static {
        DISPOSED = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS.set(new HashMap<>());
        APP_CONFIGURATION = new ThreadLocal<>();
        WRAPPED_DRIVER = new ThreadLocal<>();
        DISPOSED.set(false);
    }

    public static HashMap<String, Object> getCustomDriverOptions() {
        return CUSTOM_DRIVER_OPTIONS.get();
    }

    public static void addDriverOptions(String key, String value) {
        CUSTOM_DRIVER_OPTIONS.get().put(key, value);
    }

    public static WindowsDriver getWrappedDriver() {
        return WRAPPED_DRIVER.get();
    }

    public static AppConfiguration getAppConfiguration() {
        return APP_CONFIGURATION.get();
    }

    public static WindowsDriver start(AppConfiguration configuration) {
        APP_CONFIGURATION.set(configuration);
        DISPOSED.set(false);
        WindowsDriver driver;
        var desktopSettings = ConfigurationService.get(DesktopSettings.class);
        var executionType = desktopSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode(desktopSettings.getServiceUrl());
        } else {
            var gridSettings = desktopSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings.isPresent() : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get());
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(desktopSettings.getTimeoutSettings().getImplicitWaitTimeout()));
        driver.manage().window().maximize();
        changeWindowSize(driver);
        WRAPPED_DRIVER.set(driver);

        return driver;
    }

    private static WindowsDriver initializeDriverGridMode(GridSettings gridSettings) {
        var caps = new WindowsOptions();
        caps.setApp(getAppConfiguration().getAppPath().replace("\\", "/"));
        caps.setAppWorkingDir(new File(getAppConfiguration().getAppPath()).getParent());

        if (Objects.requireNonNullElse(ConfigurationService.get(DesktopSettings.class).getAllowImageFindStrategies(), false))
            caps.setCapability("use-plugins", "images");

        WindowsDriver driver = null;
        try {
            driver = new WindowsDriver(new URL(gridSettings.getUrl()), caps);
        } catch (MalformedURLException e) {
            DebugInformation.printStackTrace(e);
        }

        return driver;
    }

    @SneakyThrows
    private static WindowsDriver initializeDriverRegularMode(String serviceUrl) {
        var caps = new WindowsOptions();
        caps.setApp(getAppConfiguration().getAppPath().replace("\\", "/"));
        caps.setAppWorkingDir(new File(getAppConfiguration().getAppPath()).getParent());
        addDriverOptions(caps);
        var driver = new WindowsDriver(new URL(serviceUrl), caps);

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

    private static void changeWindowSize(WebDriver wrappedDriver) {
        try {
            if (getAppConfiguration().getHeight() != 0 && getAppConfiguration().getWidth() != 0) {
                wrappedDriver.manage().window().setSize(new Dimension(getAppConfiguration().getHeight(), getAppConfiguration().getWidth()));
            }
        } catch (Exception ignored) {}
    }

    public static void close() {
        if (DISPOSED.get()) {
            return;
        }

        if (WRAPPED_DRIVER.get() != null) {
            WRAPPED_DRIVER.get().close();
            CUSTOM_DRIVER_OPTIONS.get().clear();
        }

        DISPOSED.set(true);
    }
}
