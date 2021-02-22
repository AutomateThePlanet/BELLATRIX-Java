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

package solutions.bellatrix.desktop.infrastructure;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.configuration.GridSettings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DriverService {
    private static ThreadLocal<Boolean> disposed;
    private static ThreadLocal<AppConfiguration> appConfiguration;
    private static ThreadLocal<HashMap<String, String>> customDriverOptions;
    private static ThreadLocal<WindowsDriver<WindowsElement>> wrappedDriver;

    static {
        disposed = new ThreadLocal<>();
        customDriverOptions = new ThreadLocal<>();
        customDriverOptions.set(new HashMap<>());
        appConfiguration = new ThreadLocal<>();
        wrappedDriver = new ThreadLocal<>();
        disposed.set(false);
    }

    public static HashMap<String, String> getCustomDriverOptions() {
        return customDriverOptions.get();
    }

    public static void addDriverOptions(String key, String value) {
        customDriverOptions.get().put(key, value);
    }

    public static WindowsDriver<WindowsElement> getWrappedDriver() {
        return wrappedDriver.get();
    }

    public static AppConfiguration getAppConfiguration() {
        return appConfiguration.get();
    }

    public static WindowsDriver<WindowsElement> start(AppConfiguration configuration) {
        appConfiguration.set(configuration);
        disposed.set(false);
        WindowsDriver<WindowsElement> driver = null;
        var webSettings = ConfigurationService.get(DesktopSettings.class);
        var executionType = webSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode();
        } else {
            var gridSettings = webSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings != null : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get());
        }

        driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get(DesktopSettings.class).getTimeoutSettings().getPageLoadTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(ConfigurationService.get(DesktopSettings.class).getTimeoutSettings().getScriptTimeout(), TimeUnit.SECONDS);
        driver.manage().window().maximize();
        changeWindowSize(driver);
        wrappedDriver.set(driver);

        return driver;
    }

    private static WindowsDriver<WindowsElement> initializeDriverGridMode(GridSettings gridSettings) {
        var caps = new DesiredCapabilities();
        caps.setCapability("platform", Platform.WIN10);
        caps.setCapability("version", "latest");

        WindowsDriver<WindowsElement> driver = null;
        try {
            driver = new WindowsDriver<WindowsElement>(new URL(gridSettings.getUrl()), caps);
        } catch (MalformedURLException e) {
            DebugInformation.printStackTrace(e);
        }

        return driver;
    }

    private static WindowsDriver<WindowsElement> initializeDriverRegularMode() {
        var caps = new DesiredCapabilities();
        caps.setCapability("app", getAppConfiguration().getAppPath());
        caps.setCapability("deviceName", "WindowsPC");
        caps.setCapability("platformName", "Windows");
        caps.setCapability("appWorkingDir", new File(getAppConfiguration().getAppPath()).getParent());
        addDriverOptions(caps);
        var driver = new WindowsDriver<WindowsElement>(caps);

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

    private static void changeWindowSize(WebDriver wrappedDriver) {
        try {
            if (getAppConfiguration().getHeight() != 0 && getAppConfiguration().getWidth() != 0) {
                wrappedDriver.manage().window().setSize(new Dimension(getAppConfiguration().getHeight(), getAppConfiguration().getWidth()));
            }
        } catch (Exception ignored) {}
    }

    public static void close() {
        if (disposed.get()) {
            return;
        }

        if (wrappedDriver.get() != null) {
            wrappedDriver.get().close();
            customDriverOptions.get().clear();
        }

        disposed.set(true);
    }
}
