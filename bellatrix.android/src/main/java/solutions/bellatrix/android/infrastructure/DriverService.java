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

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import lombok.SneakyThrows;
import org.openqa.selenium.MutableCapabilities;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.android.configuration.GridSettings;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.TimestampBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

public class DriverService {
    private static final ThreadLocal<Boolean> DISPOSED;
    private static final ThreadLocal<AppConfiguration> APP_CONFIGURATION;
    private static final ThreadLocal<HashMap<String, Object>> CUSTOM_DRIVER_OPTIONS;
    private static final ThreadLocal<AndroidDriver> WRAPPED_ANDROID_DRIVER;
    private static boolean isBuildNameSet = false;
    private  static String buildName;

    static {
        DISPOSED = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS = new ThreadLocal<>();
        CUSTOM_DRIVER_OPTIONS.set(new HashMap<>());
        APP_CONFIGURATION = new ThreadLocal<>();
        WRAPPED_ANDROID_DRIVER = new ThreadLocal<>();
        DISPOSED.set(false);
    }

    public static HashMap<String, Object> getCustomDriverOptions() {
        return CUSTOM_DRIVER_OPTIONS.get();
    }

    public static void addDriverConfigOptions(String key, String value) {
        CUSTOM_DRIVER_OPTIONS.get().put(key, value);
    }

    public static AndroidDriver getWrappedAndroidDriver() {
        return WRAPPED_ANDROID_DRIVER.get();
    }

    public static AppConfiguration getAppConfiguration() {
        return APP_CONFIGURATION.get();
    }

    public static AndroidDriver start(AppConfiguration configuration) {
        APP_CONFIGURATION.set(configuration);
        DISPOSED.set(false);
        AndroidDriver driver;
        var androidSettings = ConfigurationService.get(AndroidSettings.class);
        var executionType = androidSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode(androidSettings.getServiceUrl());
        } else {
            var testName = getAppConfiguration().getTestName();
            var gridSettings = androidSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings.isPresent() : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get(), testName);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(androidSettings.getTimeoutSettings().getImplicitWaitTimeout()));
        WRAPPED_ANDROID_DRIVER.set(driver);
        solutions.bellatrix.web.infrastructure.DriverService.setWrappedDriver(driver);
        return driver;
    }

    private static AndroidDriver initializeDriverGridMode(GridSettings gridSettings, String testName) {
        var caps = new UiAutomator2Options();
        caps.setPlatformName(MobilePlatform.ANDROID);
        caps.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        caps.setPlatformVersion(getAppConfiguration().getAndroidVersion());
        caps.setDeviceName(getAppConfiguration().getDeviceName());

        if (getAppConfiguration().getIsMobileWebExecution()) {
            caps.withBrowserName(ConfigurationService.get(AndroidSettings.class).getDefaultBrowser());
        } else {
            caps.setApp(getAppConfiguration().getAppPath().replace("\\", "/"));
            caps.setAppPackage(getAppConfiguration().getAppPackage());
            caps.setAppActivity(getAppConfiguration().getAppActivity());
        }

        var options = new HashMap<String, Object>();
        addGridOptions(options, gridSettings);
        options.put("name", testName);
        caps.setCapability(gridSettings.getOptionsName(), options);

        if (ConfigurationService.get(AndroidSettings.class).getAllowImageFindStrategies())
            caps.setCapability("use-plugins", "images");

        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL(gridSettings.getUrl()), caps);
            solutions.bellatrix.web.infrastructure.DriverService.setWrappedDriver(driver);
        } catch (Exception e) {
            DebugInformation.printStackTrace(e);
        }

        return driver;
    }

    @SneakyThrows
    private static AndroidDriver initializeDriverRegularMode(String serviceUrl) {
        var caps = new UiAutomator2Options();
        caps.setPlatformName(MobilePlatform.ANDROID);
        caps.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        caps.setPlatformVersion(getAppConfiguration().getAndroidVersion());
        caps.setDeviceName(getAppConfiguration().getDeviceName());

        if (getAppConfiguration().getIsMobileWebExecution()) {
            caps.withBrowserName(ConfigurationService.get(AndroidSettings.class).getDefaultBrowser());
        } else {
            caps.setApp(getAppConfiguration().getAppPath().replace("\\", "/"));
            caps.setAppPackage(getAppConfiguration().getAppPackage());
            caps.setAppActivity(getAppConfiguration().getAppActivity());
        }

        if (ConfigurationService.get(AndroidSettings.class).getAllowImageFindStrategies())
            caps.setCapability("use-plugins", "images");

        addDriverConfigOptions(caps);
        addCustomDriverOptions(caps);
        var driver = new AndroidDriver(new URL(serviceUrl), caps);
        solutions.bellatrix.web.infrastructure.DriverService.setWrappedDriver(driver);
        return driver;
    }

    private static void addGridOptions(HashMap<String, Object> options, GridSettings gridSettings) {
        Log.info("Add Appium Options:");
        Log.info("");

        for (var entry : gridSettings.getArguments()) {
            for (var c : entry.entrySet()) {
                String key = c.getKey();
                Object value = c.getValue();

                if (key.toLowerCase().contains("build")) {
                    var buildName = getBuildName();
                    if (buildName == null) {
                        buildName = value.toString();
                    }
                    value = buildName;
                } else if (value instanceof String && ((String) value).startsWith("env_")) {
                    value = System.getProperty(((String) value).replace("env_", ""));
                }

                // Handle boolean values
                if ("true".equalsIgnoreCase(value.toString())) {
                    value = true;
                } else if ("false".equalsIgnoreCase(value.toString())) {
                    value = false;
                }

                options.put(key, value);
                Log.info(key + "= " + value);
            }

            if ("lambdatest".equalsIgnoreCase(gridSettings.getProviderName())) {
                options.put("lambdaMaskCommands", new String[]{"setValues", "setCookies", "getCookies"});
            }

            Log.info("");
        }
    }


    private static String getBuildName() {
        if (!isBuildNameSet) {
            buildName = System.getProperty("buildName");
        }

        if (buildName == null) {
            InputStream input = ConfigurationService.class.getResourceAsStream("/application.properties");
            var p = new Properties();
            try {
                p.load(input);
            } catch (IOException e) {
                return null;
            }

            if (!isBuildNameSet) {
                buildName = p.getProperty("buildName");
            }

            if (buildName.equals("{randomNumber}") && !isBuildNameSet) {
                buildName = TimestampBuilder.buildUniqueTextByPrefix("LE_");
                isBuildNameSet = true;
            }
        }

        return buildName;
    }

    private static <TOption extends MutableCapabilities> void addDriverConfigOptions(TOption chromeOptions) {
        for (var optionEntry : APP_CONFIGURATION.get().appiumOptions.entrySet()) {
            chromeOptions.setCapability(optionEntry.getKey(), optionEntry.getValue());
        }
    }

    private static <TOption extends MutableCapabilities> void addCustomDriverOptions(TOption mobileOptions) {
        getCustomDriverOptions().forEach(mobileOptions::setCapability);
    }

    public static void close() {
        if (DISPOSED.get()) {
            return;
        }

        if (WRAPPED_ANDROID_DRIVER.get() != null) {
            WRAPPED_ANDROID_DRIVER.get().quit();
            // CUSTOM_DRIVER_OPTIONS.get().clear();
        }

        DISPOSED.set(true);
    }
}
