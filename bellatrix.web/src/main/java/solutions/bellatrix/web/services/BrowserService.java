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

package solutions.bellatrix.web.services;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.web.components.Frame;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.infrastructure.BrowserConfiguration;

public class BrowserService extends WebService {
    private final JavascriptExecutor javascriptExecutor;

    public BrowserService() {
        super();
        javascriptExecutor = (JavascriptExecutor)getWrappedDriver();
    }

    public String getPageSource() {
        return getWrappedDriver().getPageSource();
    }

    public String getUrl() {
        return getWrappedDriver().getCurrentUrl();
    }

    public String getTitle() {
        return getWrappedDriver().getTitle();
    }

    public void back() {
        getWrappedDriver().navigate().back();
    }

    public void forward() {
        getWrappedDriver().navigate().forward();
    }

    public void refresh() {
        getWrappedDriver().navigate().refresh();
    }

    public void switchToDefault() {
        getWrappedDriver().switchTo().defaultContent();
    }

    public void switchToActive() {
        getWrappedDriver().switchTo().activeElement();
    }

    public void switchToFirstBrowserTab() {
        getWrappedDriver().switchTo().window(getWrappedDriver().getWindowHandles().stream().findFirst().orElse(""));
    }

    public void switchToLastTab() {
        getWrappedDriver().switchTo().window(getWrappedDriver().getWindowHandles().stream().reduce((first, second) -> second).orElse(""));
    }

    public void switchToTab(String tabName) {
        getWrappedDriver().switchTo().window(tabName);
    }

    public void switchToFrame(Frame frame) {
        getWrappedDriver().switchTo().frame(frame.findElement());
    }

    public void clearSessionStorage() {
        var browserConfig = InstanceFactory.create(BrowserConfiguration.class);
        switch (browserConfig.getBrowser()) {
            case CHROME:
            case CHROME_HEADLESS:
                var chromeDriver = (ChromeDriver)getWrappedDriver();
                chromeDriver.getSessionStorage().clear();
                break;
            case FIREFOX:
            case FIREFOX_HEADLESS:
                var firefoxDriver = (FirefoxDriver)getWrappedDriver();
                firefoxDriver.getSessionStorage().clear();
                break;
            case INTERNET_EXPLORER:
                var ieDriver = (InternetExplorerDriver)getWrappedDriver();
                ((JavascriptExecutor)ieDriver).executeScript("sessionStorage.clear()");
                break;
            case EDGE:
            case EDGE_HEADLESS:
                var edgeDriver = (EdgeDriver)getWrappedDriver();
                ((JavascriptExecutor)edgeDriver).executeScript("sessionStorage.clear()");
                break;
            case OPERA:
                var operaDriver = (OperaDriver)getWrappedDriver();
                operaDriver.getSessionStorage().clear();
                break;
            case SAFARI:
                var safariDriver = (SafariDriver)getWrappedDriver();
                ((JavascriptExecutor)safariDriver).executeScript("sessionStorage.clear()");
                break;
        }
    }

    public void clearLocalStorage() {
        var browserConfig = InstanceFactory.create(BrowserConfiguration.class);
        switch (browserConfig.getBrowser()) {
            case CHROME:
            case CHROME_HEADLESS:
                var chromeDriver = (ChromeDriver)getWrappedDriver();
                chromeDriver.getLocalStorage().clear();
                break;
            case FIREFOX:
            case FIREFOX_HEADLESS:
                var firefoxDriver = (FirefoxDriver)getWrappedDriver();
                firefoxDriver.getLocalStorage().clear();
                break;
            case INTERNET_EXPLORER:
                var ieDriver = (InternetExplorerDriver)getWrappedDriver();
                ((JavascriptExecutor)ieDriver).executeScript("localStorage.clear()");
                break;
            case EDGE:
            case EDGE_HEADLESS:
                var edgeDriver = (EdgeDriver)getWrappedDriver();
                ((JavascriptExecutor)edgeDriver).executeScript("localStorage.clear()");
                break;
            case OPERA:
                var operaDriver = (OperaDriver)getWrappedDriver();
                operaDriver.getLocalStorage().clear();
                break;
            case SAFARI:
                var safariDriver = (SafariDriver)getWrappedDriver();
                ((JavascriptExecutor)safariDriver).executeScript("localStorage.clear()");
                break;
        }
    }

    public void waitForAjax() {
        long ajaxTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), ajaxTimeout, sleepInterval);
        var javascriptExecutor = (JavascriptExecutor)getWrappedDriver();
        webDriverWait.until(d -> (boolean)javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0"));
    }

    public void waitForAjaxRequest(String requestPartialUrl, int additionalTimeoutInSeconds) {
        long ajaxTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), ajaxTimeout + additionalTimeoutInSeconds, sleepInterval);
        webDriverWait.until(d -> {
            String script = String.format("return performance.getEntriesByType('resource').filter(item => item.initiatorType == 'xmlhttprequest' && item.name.toLowerCase().includes('%s'))[0] !== undefined;", requestPartialUrl);
            String result = (String)javascriptExecutor.executeScript(script);
            if (result.equals("True")) {
                return true;
            }
            return false;
        });
    }

    public void waitForAjaxRequest(String requestPartialUrl) {
        waitForAjaxRequest(requestPartialUrl, 0);
    }

    public void waitUntilPageLoadsCompletely() {
        long waitUntilReadyTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitUntilReadyTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), waitUntilReadyTimeout, sleepInterval);
        webDriverWait.until(d -> javascriptExecutor.executeScript("return document.readyState").toString().equals("complete"));
    }

    public void waitForJavaScriptAnimations() {
        long waitForJavaScriptAnimationsTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForJavaScriptAnimationsTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), waitForJavaScriptAnimationsTimeout, sleepInterval);
        webDriverWait.until(d -> (boolean)javascriptExecutor.executeScript("return jQuery && jQuery(':animated').length === 0"));
    }

    public void waitForAngular() {
        long angularTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAngularTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), angularTimeout, sleepInterval);

        String isAngular5 = (String)javascriptExecutor.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
        if (StringUtils.isBlank(isAngular5)) {
            webDriverWait.until(d -> (boolean)javascriptExecutor.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1"));
        } else {
            boolean isAngularDefined = (boolean)javascriptExecutor.executeScript("return window.angular === undefined");
            if (!((boolean)isAngularDefined)) {
                boolean isAngularInjectorUnDefined = (boolean)javascriptExecutor.executeScript("return angular.element(document).injector() === undefined");
                if (!isAngularInjectorUnDefined) {
                    webDriverWait.until(d -> (boolean)javascriptExecutor.executeScript("return angular.element(document).injector().get('$http').pendingRequests.length === 0"));
                }
            }
        }
    }
}
