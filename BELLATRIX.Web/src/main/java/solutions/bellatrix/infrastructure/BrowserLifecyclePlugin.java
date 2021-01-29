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

package solutions.bellatrix.infrastructure;

import org.testng.ITestResult;
import solutions.bellatrix.plugins.Plugin;

import java.lang.reflect.Method;

public class BrowserLifecyclePlugin extends Plugin {
    private static ThreadLocal<BrowserConfiguration> currentBrowserConfiguration;
    private static ThreadLocal<BrowserConfiguration> previousBrowserConfiguration;
    private static ThreadLocal<Boolean> isBrowserStartedDuringPreBeforeClass;
    private static ThreadLocal<Boolean> isBrowserStartedCorrectly;

    static {
        currentBrowserConfiguration = new ThreadLocal<>();
        previousBrowserConfiguration = new ThreadLocal<>();
        isBrowserStartedDuringPreBeforeClass = new ThreadLocal<>();
        isBrowserStartedCorrectly = new ThreadLocal<>();
    }

    @Override
    public void preBeforeClass(Class type) {
        currentBrowserConfiguration.set(getExecutionBrowserClassLevel(type));
        if (shouldRestartBrowser())
        {
            restartBrowser();
            // TODO: maybe we can simplify and remove this parameter.
            isBrowserStartedDuringPreBeforeClass.set(true);
        } else {
            isBrowserStartedDuringPreBeforeClass.set(false);
        }

        super.preBeforeClass(type);
    }

    @Override
    public void postAfterClass(Class type) {
        shutdownBrowser();
        isBrowserStartedDuringPreBeforeClass.set(false);
        super.preAfterClass(type);
    }

    @Override
    public void preBeforeTest(ITestResult testResult, Method memberInfo) {
        currentBrowserConfiguration.set(getBrowserConfiguration(memberInfo));

        if (!isBrowserStartedDuringPreBeforeClass.get()) {
            if (shouldRestartBrowser()) {
                restartBrowser();
            }
        }

        isBrowserStartedDuringPreBeforeClass.set(false);
    }

    @Override
    public void postAfterTest(ITestResult testResult, Method memberInfo) {
        if (currentBrowserConfiguration.get().getLifecycle() ==
                Lifecycle.RESTART_ON_FAIL && testResult.getStatus() == ITestResult.FAILURE) {
            shutdownBrowser();
            isBrowserStartedDuringPreBeforeClass.set(false);
        }
    }

    private void shutdownBrowser() {
        DriverService.close();
        previousBrowserConfiguration.set(null);
    }

    private void restartBrowser() {
        shutdownBrowser();
        try {
            DriverService.start(currentBrowserConfiguration.get());
            isBrowserStartedCorrectly.set(true);
        } catch (Exception ex) {
            isBrowserStartedCorrectly.set(false);
        }

        previousBrowserConfiguration.set(currentBrowserConfiguration.get());
    }

    private Boolean shouldRestartBrowser() {
        // TODO: IsBrowserStartedCorrectly getter?
        var previousConfiguration = previousBrowserConfiguration.get();
        var currentConfiguration = currentBrowserConfiguration.get();
        if (previousConfiguration == null)
        {
            return true;
        } else if (!isBrowserStartedCorrectly.get()) {
            return true;
        } else if (!previousConfiguration.equals(currentConfiguration)) {
            return true;
        } else if (currentConfiguration.getLifecycle() == Lifecycle.RESTART_EVERY_TIME) {
            return true;
        } else {
            return false;
        }
    }

    private BrowserConfiguration getBrowserConfiguration(Method memberInfo) {
        BrowserConfiguration result = null;
        var classBrowserType = getExecutionBrowserClassLevel(memberInfo.getDeclaringClass());
        var methodBrowserType = getExecutionBrowserMethodLevel(memberInfo);
        if (methodBrowserType != null)
        {
            result = methodBrowserType;
        }
        else if (classBrowserType != null)
        {
            result = classBrowserType;
        }

        return result;
    }

    private BrowserConfiguration getExecutionBrowserMethodLevel(Method memberInfo) {
        var executionBrowserAnnotation = (ExecutionBrowser)memberInfo.getDeclaredAnnotation(ExecutionBrowser.class);
        if (executionBrowserAnnotation == null) {
            return null;
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.lifecycle(), executionBrowserAnnotation.executionType());
    }

    private BrowserConfiguration getExecutionBrowserClassLevel(Class<?> type) {
        var executionBrowserAnnotation = (ExecutionBrowser)type.getDeclaredAnnotation(ExecutionBrowser.class);
        if (executionBrowserAnnotation == null) {
            // set default browser configuration if not set previously.
            return new BrowserConfiguration(Browser.CHROME, Lifecycle.REUSE_IF_STARTED, ExecutionType.REGULAR);
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.lifecycle(), executionBrowserAnnotation.executionType());
    }
}
