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

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.util.concurrent.TimeUnit;

public class DriverService {
    private static ThreadLocal<Boolean> disposed;
    //    private static ProxyService proxyService;
    private static BrowserSettings browserSettings;
    private static ThreadLocal<BrowserConfiguration> browserConfiguration;
    private static ThreadLocal<WebDriver> wrappedDriver;

    static {
        disposed.set(false);
    }

    public static ThreadLocal<WebDriver> getWrappedDriver() {
        return wrappedDriver;
    }

    public static BrowserConfiguration getBrowserConfiguration() {
        return browserConfiguration.get();
    }

    public static BrowserSettings getBrowserSettings() {
        return browserSettings;
    }

    public static WebDriver start(BrowserConfiguration configuration) {
        browserConfiguration.set(configuration);
        WebDriver driver = null;
        // configure proxy
        switch (configuration.getExecutionType()) {
            case REGULAR -> {
                driver = initializeDriverRegularMode();
            }
            case GRID -> {
                driver = initializeDriverGridMode();
            }
            case SAUCE_LABS -> {
                driver = initializeDriverSauceLabsMode();
            }
            case BROWSER_STACK -> {
                driver = initializeDriverBrowserStackMode();
            }
            case CROSS_BROWSER_TESTING -> {
                driver = initializeDriverCrossBrowserTestingMode();
            }
        }

        changeWindowSize(driver);
        wrappedDriver.set(driver);

        return driver;
    }

    private static WebDriver initializeDriverRegularMode() {
        WebDriver driver = null;
        switch (browserConfiguration.get().getBrowser()) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();

                var chromeOptions = new ChromeOptions();
                initializeBrowserOptions(chromeOptions);
                chromeOptions.addArguments("--log-level=3");
                System.setProperty("webdriver.chrome.silentOutput", "true");

                driver = new ChromeDriver(chromeOptions);
                driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get(WebSettings.class).getChrome().getPageLoadTimeout(), TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(ConfigurationService.get(WebSettings.class).getChrome().getScriptTimeout(), TimeUnit.SECONDS);
            }
            case CHROME_HEADLESS -> {
                WebDriverManager.chromedriver().setup();
                var chromeHeadlessOptions = new ChromeOptions();
                initializeBrowserOptions(chromeHeadlessOptions);
                chromeHeadlessOptions.addArguments("--log-level=3");
                chromeHeadlessOptions.setHeadless(true);
                System.setProperty("webdriver.chrome.silentOutput", "true");

                driver = new ChromeDriver(chromeHeadlessOptions);
                driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get(WebSettings.class).getChrome().getPageLoadTimeout(), TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(ConfigurationService.get(WebSettings.class).getChrome().getScriptTimeout(), TimeUnit.SECONDS);
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                var firefoxOptions = new FirefoxOptions();
                initializeBrowserOptions(firefoxOptions);
                driver = new FirefoxDriver(firefoxOptions);

                driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get(WebSettings.class).getFirefox().getPageLoadTimeout(), TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(ConfigurationService.get(WebSettings.class).getFirefox().getScriptTimeout(), TimeUnit.SECONDS);
            }
            case FIREFOX_HEADLESS -> {
                WebDriverManager.firefoxdriver().setup();
                var firefoxHeadlessOptions = new FirefoxOptions();
                initializeBrowserOptions(firefoxHeadlessOptions);
                firefoxHeadlessOptions.setHeadless(true);
                driver = new FirefoxDriver(firefoxHeadlessOptions);

                driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get(WebSettings.class).getFirefox().getPageLoadTimeout(), TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(ConfigurationService.get(WebSettings.class).getFirefox().getScriptTimeout(), TimeUnit.SECONDS);
            }
            case EDGE, EDGE_HEADLESS -> {
                throw new InvalidArgumentException("BELLATRIX doesn't support Edge. It will be supported with the official release of WebDriver 4.0");
            }
            case OPERA -> {
                throw new InvalidArgumentException("BELLATRIX doesn't support Opera.");
            }
            case SAFARI -> {
               throw new InvalidArgumentException("BELLATRIX doesn't support Safari.");
            }
            case INTERNET_EXPLORER -> {
                throw new InvalidArgumentException("BELLATRIX doesn't support Internet Explorer.");
            }
        }

        return driver;
    }

    private static void initializeBrowserOptions(MutableCapabilities browserOptions) {
        for (var keyOption:browserConfiguration.get().getDriverOptions().keySet()) {
            browserOptions.setCapability(keyOption, browserConfiguration.get().getDriverOptions().get(keyOption));
        }
    }

    private static WebDriver initializeDriverCrossBrowserTestingMode() {
        WebDriver driver = null;
        return null;
    }

    private static WebDriver initializeDriverBrowserStackMode() {
        WebDriver driver = null;
        return null;
    }

    private static WebDriver initializeDriverSauceLabsMode() {
        WebDriver driver = null;
        return null;
    }

    private static WebDriver initializeDriverGridMode() {
        WebDriver driver = null;
        return null;
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
        }

        disposed.set(true);
    }
}
