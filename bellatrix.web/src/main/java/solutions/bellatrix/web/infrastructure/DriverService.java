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

package solutions.bellatrix.web.infrastructure;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.web.configuration.GridSettings;
import solutions.bellatrix.web.configuration.WebSettings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DriverService {
    private static ThreadLocal<Boolean> disposed;
    private static ThreadLocal<BrowserConfiguration> browserConfiguration;
    private static ThreadLocal<HashMap<String, String>> customDriverOptions;
    private static ThreadLocal<WebDriver> wrappedDriver;

    static {
        disposed = new ThreadLocal<>();
        customDriverOptions = new ThreadLocal<>();
        customDriverOptions.set(new HashMap<>());
        browserConfiguration = new ThreadLocal<>();
        wrappedDriver = new ThreadLocal<>();
        disposed.set(false);
    }

    public static HashMap<String, String> getCustomDriverOptions() {
        return customDriverOptions.get();
    }

    public static void addDriverOptions(String key, String value) {
        customDriverOptions.get().put(key, value);
    }

    public static WebDriver getWrappedDriver() {
        return wrappedDriver.get();
    }

    public static BrowserConfiguration getBrowserConfiguration() {
        return browserConfiguration.get();
    }

    public static WebDriver start(BrowserConfiguration configuration) {
        browserConfiguration.set(configuration);
        disposed.set(false);
        WebDriver driver = null;
        var webSettings = ConfigurationService.get(WebSettings.class);
        var executionType = webSettings.getExecutionType();
        if (executionType.equals("regular")) {
            driver = initializeDriverRegularMode();
        } else {
            var gridSettings = webSettings.getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst();
            assert gridSettings != null : String.format("The specified execution type '%s' is not declared in the configuration", executionType);
            driver = initializeDriverGridMode(gridSettings.get());
        }

        driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get(WebSettings.class).getTimeoutSettings().getPageLoadTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(ConfigurationService.get(WebSettings.class).getTimeoutSettings().getScriptTimeout(), TimeUnit.SECONDS);
        driver.manage().window().maximize();
        changeWindowSize(driver);
        wrappedDriver.set(driver);

        return driver;
    }

    private static WebDriver initializeDriverGridMode(GridSettings gridSettings) {
        var caps = new DesiredCapabilities();
        if (browserConfiguration.get().getPlatform() != Platform.ANY) {
            caps.setCapability("platform", browserConfiguration.get().getPlatform());
        }

        if (browserConfiguration.get().getVersion() != 0) {
            caps.setCapability("version", browserConfiguration.get().getVersion());
        } else {
            caps.setCapability("version", "latest");
        }

        switch (browserConfiguration.get().getBrowser()) {
            case CHROME_HEADLESS:
            case CHROME: {
                var chromeOptions = new ChromeOptions();
                addGridOptions(chromeOptions, gridSettings);
                caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            }
            case FIREFOX_HEADLESS:
            case FIREFOX: {
                var firefoxOptions = new FirefoxOptions();
                addGridOptions(firefoxOptions, gridSettings);
                caps.setCapability(ChromeOptions.CAPABILITY, firefoxOptions);
            }
            case EDGE, EDGE_HEADLESS: {
                var edgeOptions = new EdgeOptions();
                addGridOptions(edgeOptions, gridSettings);
                caps.setCapability(ChromeOptions.CAPABILITY, edgeOptions);
            }
            case OPERA: {
                var operaOptions = new OperaOptions();
                addGridOptions(operaOptions, gridSettings);
                caps.setCapability(ChromeOptions.CAPABILITY, operaOptions);
            }
            case SAFARI: {
                throw new InvalidArgumentException("BELLATRIX doesn't support Safari.");
            }
            case INTERNET_EXPLORER: {
                var ieOptions = new InternetExplorerOptions();
                addGridOptions(ieOptions, gridSettings);
                caps.setCapability(ChromeOptions.CAPABILITY, ieOptions);
            }
        }

        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(gridSettings.getUrl()), caps);
        } catch (MalformedURLException e) {
            DebugInformation.printStackTrace(e);
        }

        return driver;
    }

    private static WebDriver initializeDriverRegularMode() {
        WebDriver driver = null;
        boolean shouldCaptureHttpTraffic = ConfigurationService.get(WebSettings.class).getShouldCaptureHttpTraffic();
        int port = ProxyServer.init();
        String proxyUrl = "127.0.0.1:" + port;
        final var proxyConfig = new Proxy()
                .setHttpProxy(proxyUrl)
                .setSslProxy(proxyUrl)
                .setFtpProxy(proxyUrl);

        switch (browserConfiguration.get().getBrowser()) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                var chromeOptions = new ChromeOptions();
                addDriverOptions(chromeOptions);
                chromeOptions.addArguments("--log-level=3");
                chromeOptions.setAcceptInsecureCerts(true);
                System.setProperty("webdriver.chrome.silentOutput", "true");
                if (shouldCaptureHttpTraffic) chromeOptions.setProxy(proxyConfig);

                driver = new ChromeDriver(chromeOptions);
            }
            case CHROME_HEADLESS -> {
                WebDriverManager.chromedriver().setup();
                var chromeHeadlessOptions = new ChromeOptions();
                addDriverOptions(chromeHeadlessOptions);
                chromeHeadlessOptions.setAcceptInsecureCerts(true);
                chromeHeadlessOptions.addArguments("--log-level=3");
                chromeHeadlessOptions.setHeadless(true);
                System.setProperty("webdriver.chrome.silentOutput", "true");
                if (shouldCaptureHttpTraffic) chromeHeadlessOptions.setProxy(proxyConfig);

                driver = new ChromeDriver(chromeHeadlessOptions);
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                var firefoxOptions = new FirefoxOptions();
                addDriverOptions(firefoxOptions);
                firefoxOptions.setAcceptInsecureCerts(true);
                if (shouldCaptureHttpTraffic) firefoxOptions.setProxy(proxyConfig);
                driver = new FirefoxDriver(firefoxOptions);
            }
            case FIREFOX_HEADLESS -> {
                WebDriverManager.firefoxdriver().setup();
                var firefoxHeadlessOptions = new FirefoxOptions();
                addDriverOptions(firefoxHeadlessOptions);
                firefoxHeadlessOptions.setAcceptInsecureCerts(true);
                firefoxHeadlessOptions.setHeadless(true);
                if (shouldCaptureHttpTraffic) firefoxHeadlessOptions.setProxy(proxyConfig);
                driver = new FirefoxDriver(firefoxHeadlessOptions);
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                var edgeOptions = new EdgeOptions();
                addDriverOptions(edgeOptions);
                if (shouldCaptureHttpTraffic) edgeOptions.setProxy(proxyConfig);
                driver = new EdgeDriver(edgeOptions);
            }
            case EDGE_HEADLESS -> {
                WebDriverManager.edgedriver().setup();
                var edgeOptions = new EdgeOptions();
                addDriverOptions(edgeOptions);
                edgeOptions.setCapability("headless", true);
                if (shouldCaptureHttpTraffic) edgeOptions.setProxy(proxyConfig);
                driver = new EdgeDriver(edgeOptions);
            }
            case OPERA -> {
                WebDriverManager.operadriver().setup();
                var operaOptions = new OperaOptions();
                addDriverOptions(operaOptions);
                if (shouldCaptureHttpTraffic) operaOptions.setProxy(proxyConfig);
                driver = new OperaDriver(operaOptions);
            }
            case SAFARI -> {
               throw new InvalidArgumentException("BELLATRIX doesn't support Safari.");
            }
            case INTERNET_EXPLORER -> {
                WebDriverManager.iedriver().setup();
                var internetExplorerOptions = new InternetExplorerOptions();
                addDriverOptions(internetExplorerOptions);
                if (shouldCaptureHttpTraffic) internetExplorerOptions.setProxy(proxyConfig);
                driver = new InternetExplorerDriver(internetExplorerOptions);
            }
        }

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
        for (var optionKey:browserConfiguration.get().driverOptions.keySet()) {
            chromeOptions.setCapability(optionKey, browserConfiguration.get().driverOptions.get(optionKey));
        }
    }

    private static void changeWindowSize(WebDriver wrappedDriver) {
        try {
            if (getBrowserConfiguration().getHeight() != 0 && getBrowserConfiguration().getWidth() != 0) {
                wrappedDriver.manage().window().setSize(new Dimension(getBrowserConfiguration().getHeight(), getBrowserConfiguration().getWidth()));
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

        ProxyServer.close();

        disposed.set(true);
    }
}
