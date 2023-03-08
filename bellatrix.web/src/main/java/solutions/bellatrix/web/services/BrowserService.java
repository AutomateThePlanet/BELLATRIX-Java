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

package solutions.bellatrix.web.services;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Wait;
import solutions.bellatrix.web.components.Frame;
import solutions.bellatrix.web.configuration.WebSettings;

import java.time.Duration;
import java.util.Objects;

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
        var handles = getWrappedDriver().getWindowHandles();
        getWrappedDriver().switchTo().window(handles.stream().reduce((first, second) -> second).orElse(""));
    }

    public void switchToTab(Runnable condition) throws InterruptedException {
        Wait.retry(() -> {
                    var handles = getWrappedDriver().getWindowHandles();
                    Boolean shouldThrowException = true;
                    for (var currentHandle : handles) {
                        getWrappedDriver().switchTo().window(currentHandle);
                        try {
                            condition.run();
                            shouldThrowException = false;
                            break;
                        } catch (Exception ex) {
                            // ignore
                        }
                    }

                    if (shouldThrowException) {
                        throw new TimeoutException();
                    }
                },
                5,
                0,
                TimeoutException.class, NotFoundException.class, StaleElementReferenceException.class);
    }

    public void switchToTab(String tabName) {
        getWrappedDriver().switchTo().window(tabName);
    }

    public void switchToFrame(Frame frame) {
        getWrappedDriver().switchTo().frame(frame.findElement());
    }

    public void clearSessionStorage() {
        ((JavascriptExecutor)getWrappedDriver()).executeScript("sessionStorage.clear()");
    }

    public void removeItemFromLocalStorage(String item) {
        ((JavascriptExecutor)getWrappedDriver()).executeScript(String.format("window.localStorage.removeItem('%s');", item));
    }

    public boolean isItemPresentInLocalStorage(String item) {
        return !(((JavascriptExecutor)getWrappedDriver()).executeScript(String.format("return window.localStorage.getItem('%s');", item)) == null);
    }

    public String getItemFromLocalStorage(String key) {
        return (String) ((JavascriptExecutor)getWrappedDriver()).executeScript(String.format("return window.localStorage.getItem('%s');", key));
    }


    public void setItemInLocalStorage(String item, String value) {
        ((JavascriptExecutor)getWrappedDriver()).executeScript(String.format("window.localStorage.setItem('%s','%s');", item, value));
    }

    public void clearLocalStorage() {
        ((JavascriptExecutor)getWrappedDriver()).executeScript("localStorage.clear()");
    }

    public void waitForAjax() {
        long ajaxTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(ajaxTimeout), Duration.ofSeconds(sleepInterval));
        var javascriptExecutor = (JavascriptExecutor)getWrappedDriver();
        webDriverWait.until(d -> {
                    var numberOfAjaxConnections = javascriptExecutor.executeScript("return !isNaN(window.openHTTPs) ? window.openHTTPs : null");
                    if (Objects.nonNull(numberOfAjaxConnections)) {
                        int ajaxConnections = Integer.parseInt(numberOfAjaxConnections.toString());
                        return ajaxConnections == 0;
                    } else {
                        monkeyPatchXMLHttpRequest();
                    }

                    return false;
                }
        );
    }

    private void monkeyPatchXMLHttpRequest() {
        var numberOfAjaxConnections = javascriptExecutor.executeScript(("return !isNaN(window.openHTTPs) ? window.openHTTPs : null"));

        if (Objects.nonNull(numberOfAjaxConnections)) {
            var ajaxConnections = Integer.parseInt(numberOfAjaxConnections.toString());
        } else {
            var script = "  (function() {" +
                    "var oldOpen = XMLHttpRequest.prototype.open;" +
                    "window.openHTTPs = 0;" +
                    "XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {" +
                    "window.openHTTPs++;" +
                    "this.addEventListener('readystatechange', function() {" +
                    "if(this.readyState == 4) {" +
                    "window.openHTTPs--;" +
                    "}" +
                    "}, false);" +
                    "oldOpen.call(this, method, url, async, user, pass);" +
                    "}" +
                    "})();";

            javascriptExecutor.executeScript(script);
        }
    }

    public void waitForAjaxRequest(String requestPartialUrl, int additionalTimeoutInSeconds) {
        long ajaxTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(ajaxTimeout + additionalTimeoutInSeconds), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(d -> {
            String script = String.format("return performance.getEntriesByType('resource').filter(item => item.initiatorType == 'xmlhttprequest' && item.name.toLowerCase().includes('%s'))[0] !== undefined;", requestPartialUrl);
            boolean result = (boolean)javascriptExecutor.executeScript(script);
            return result;
        });
    }

    public void waitForAjaxRequest(String requestPartialUrl) {
        waitForAjaxRequest(requestPartialUrl, 0);
    }

    public void waitUntilPageLoadsCompletely() {
        long waitUntilReadyTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitUntilReadyTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(waitUntilReadyTimeout), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public void waitForReactPageLoadsCompletely() {
        long waitUntilReadyTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitUntilReadyTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(waitUntilReadyTimeout), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(d -> javascriptExecutor.executeScript("return document.querySelector('[data-reactroot]') !== null"));
        webDriverWait.until(d -> javascriptExecutor.executeScript("return window.performance.timing.loadEventEnd > 0"));
    }

    public void waitForJavaScriptAnimations() {
        long waitForJavaScriptAnimationsTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForJavaScriptAnimationsTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(waitForJavaScriptAnimationsTimeout), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(d -> (boolean)javascriptExecutor.executeScript("return jQuery && jQuery(':animated').length === 0"));
    }

    public void waitForPartialUrl(String partialUrl) {
        long waitForPartialUrlTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForPartialUrl();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(waitForPartialUrlTimeout), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(ExpectedConditions.urlContains(partialUrl));
    }

    public void waitNumberOfWindowsToBe(int numberOfWindows) {
        long waitNumberOfWindowsToBe = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForPartialUrl();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(waitNumberOfWindowsToBe), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
    }

    public void waitForAngular() {
        long angularTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAngularTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(angularTimeout), Duration.ofSeconds(sleepInterval));

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
