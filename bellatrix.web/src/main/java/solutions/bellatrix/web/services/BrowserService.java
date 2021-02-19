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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.configuration.WebSettings;

public class BrowserService extends WebService {
    private final JavascriptExecutor javascriptExecutor;

    public BrowserService() {
        super();
        javascriptExecutor = (JavascriptExecutor) getWrappedDriver();
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

    public void SwitchToFrame(WebDriver.TargetLocator frame) {
        if (frame.activeElement() != null) {
            getWrappedDriver().switchTo().frame(frame.activeElement());
        }
    }

//    public void ClearSessionStorage() {
//        var browserConfig = ServicesCollection.Current.Resolve < BrowserConfiguration > ();
//        switch (browserConfig.BrowserType) {
//            case BrowserType.NotSet:
//                break;
//            case BrowserType.Chrome:
//            case BrowserType.ChromeHeadless:
//                var chromeDriver = (ChromeDriver) WrappedDriver;
//                chromeDriver.WebStorage.SessionStorage.Clear();
//                break;
//            case BrowserType.Firefox:
//            case BrowserType.FirefoxHeadless:
//                var firefoxDriver = (FirefoxDriver) WrappedDriver;
//                firefoxDriver.WebStorage.SessionStorage.Clear();
//                break;
//            case BrowserType.InternetExplorer:
//                var ieDriver = (InternetExplorerDriver) WrappedDriver;
//                ieDriver.WebStorage.SessionStorage.Clear();
//                break;
//            case BrowserType.Edge:
//            case BrowserType.EdgeHeadless:
//                var edgeDriver = (EdgeDriver) WrappedDriver;
//                edgeDriver.WebStorage.SessionStorage.Clear();
//                break;
//            case BrowserType.Opera:
//                var operaDriver = (OperaDriver) WrappedDriver;
//                operaDriver.WebStorage.SessionStorage.Clear();
//                break;
//            case BrowserType.Safari:
//                var safariDriver = (SafariDriver) WrappedDriver;
//                safariDriver.WebStorage.SessionStorage.Clear();
//                break;
//        }
//    }
//
//    public void ClearLocalStorage() {
//        var browserConfig = ServicesCollection.Current.Resolve < BrowserConfiguration > ();
//        switch (browserConfig.BrowserType) {
//            case BrowserType.NotSet:
//                break;
//            case BrowserType.Chrome:
//            case BrowserType.ChromeHeadless:
//                var chromeDriver = (ChromeDriver) WrappedDriver;
//                chromeDriver.WebStorage.LocalStorage.Clear();
//                break;
//            case BrowserType.Firefox:
//            case BrowserType.FirefoxHeadless:
//                var firefoxDriver = (FirefoxDriver) WrappedDriver;
//                firefoxDriver.WebStorage.LocalStorage.Clear();
//                break;
//            case BrowserType.InternetExplorer:
//                var ieDriver = (InternetExplorerDriver) WrappedDriver;
//                ieDriver.WebStorage.LocalStorage.Clear();
//                break;
//            case BrowserType.Edge:
//            case BrowserType.EdgeHeadless:
//                var edgeDriver = (EdgeDriver) WrappedDriver;
//                edgeDriver.WebStorage.LocalStorage.Clear();
//                break;
//            case BrowserType.Opera:
//                var operaDriver = (OperaDriver) WrappedDriver;
//                operaDriver.WebStorage.LocalStorage.Clear();
//                break;
//            case BrowserType.Safari:
//                var safariDriver = (SafariDriver) WrappedDriver;
//                safariDriver.WebStorage.LocalStorage.Clear();
//                break;
//        }
//    }

    public void waitForAjax() {
        long ajaxTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), ajaxTimeout, sleepInterval);
        var javascriptExecutor = (JavascriptExecutor) getWrappedDriver();
        webDriverWait.until(d -> (Boolean) javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0"));
    }

    public void waitForAjaxRequest(String requestPartialUrl, int additionalTimeoutInSeconds) {
        long ajaxTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), ajaxTimeout + additionalTimeoutInSeconds, sleepInterval);
        webDriverWait.until(d -> {
            String script = String.format("return performance.getEntriesByType('resource').filter(item => item.initiatorType == 'xmlhttprequest' && item.name.toLowerCase().includes('%s'))[0] !== undefined;", requestPartialUrl);
            String result = (String) javascriptExecutor.executeScript(script);
            if (result == "True") {
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
        webDriverWait.until(d -> (Boolean) javascriptExecutor.executeScript("return jQuery && jQuery(':animated').length === 0"));
    }

    public void waitForAngular() {
        long angularTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAngularTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), angularTimeout, sleepInterval);

        String isAngular5 = (String) javascriptExecutor.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
        if (StringUtils.isBlank(isAngular5)) {
            webDriverWait.until(d -> (Boolean) javascriptExecutor.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1"));
        } else {
            Boolean isAngularDefined = (Boolean) javascriptExecutor.executeScript("return window.angular === undefined");
            if (!((Boolean) isAngularDefined)) {
                Boolean isAngularInjectorUnDefined = (Boolean) javascriptExecutor.executeScript("return angular.element(document).injector() === undefined");
                if (!isAngularInjectorUnDefined) {
                    webDriverWait.until(d -> (Boolean) javascriptExecutor.executeScript("return angular.element(document).injector().get('$http').pendingRequests.length === 0"));
                }
            }
        }
    }
}
