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

package solutions.bellatrix.services;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.configuration.ConfigurationService;
import solutions.bellatrix.configuration.TimeoutSettings;

public class BrowserService extends WebService {
    private final JavascriptExecutor javascriptExecutor;

    protected BrowserService() {
        super();
        javascriptExecutor = (JavascriptExecutor) getWrappedDriver();
    }

    public void waitForAjax() {
        int ajaxTimeout = ConfigurationService.get(TimeoutSettings.class).getWaitForAjaxTimeout();
        int sleepInterval = ConfigurationService.get(TimeoutSettings.class).getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), ajaxTimeout, sleepInterval);
        var javascriptExecutor = (JavascriptExecutor) getWrappedDriver();
        webDriverWait.until(d -> (Boolean)javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0"));
    }

    public void waitForAjaxRequest(String requestPartialUrl, int additionalTimeoutInSeconds) {
        int ajaxTimeout = ConfigurationService.get(TimeoutSettings.class).getWaitForAjaxTimeout();
        int sleepInterval = ConfigurationService.get(TimeoutSettings.class).getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), ajaxTimeout + additionalTimeoutInSeconds, sleepInterval);
        var javascriptExecutor = (JavascriptExecutor) getWrappedDriver();
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

    public void waitForAngular() {
        int angularTimeout = ConfigurationService.get(TimeoutSettings.class).getWaitForAngularTimeout();
        int sleepInterval = ConfigurationService.get(TimeoutSettings.class).getSleepInterval();
        var webDriverWait = new WebDriverWait(getWrappedDriver(), angularTimeout, sleepInterval);

        String isAngular5 = (String)javascriptExecutor.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
        if (StringUtils.isBlank(isAngular5))
        {
            webDriverWait.until(d -> (Boolean)javascriptExecutor.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1"));
        }
        else
        {
            Boolean isAngularDefined = (Boolean)javascriptExecutor.executeScript("return window.angular === undefined");
            if (!((Boolean)isAngularDefined))
            {
                Boolean isAngularInjectorUnDefined = (Boolean)javascriptExecutor.executeScript("return angular.element(document).injector() === undefined");
                if (!isAngularInjectorUnDefined)
                {
                    webDriverWait.until(d -> (Boolean)javascriptExecutor.executeScript("return angular.element(document).injector().get('$http').pendingRequests.length === 0"));
                }
            }
        }
    }
}
