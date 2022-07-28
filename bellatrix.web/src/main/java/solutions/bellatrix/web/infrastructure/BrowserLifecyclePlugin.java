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

package solutions.bellatrix.web.infrastructure;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.web.configuration.WebSettings;

import java.lang.reflect.Method;
import java.util.Objects;

public class BrowserLifecyclePlugin extends Plugin {
    private static final ThreadLocal<BrowserConfiguration> CURRENT_BROWSER_CONFIGURATION;
    private static final ThreadLocal<BrowserConfiguration> PREVIOUS_BROWSER_CONFIGURATION;
    private static final ThreadLocal<Boolean> IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS;
    private static final ThreadLocal<Boolean> IS_BROWSER_STARTED_CORRECTLY;

    static {
        CURRENT_BROWSER_CONFIGURATION = new ThreadLocal<>();
        PREVIOUS_BROWSER_CONFIGURATION = new ThreadLocal<>();
        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS = new ThreadLocal<>();
        IS_BROWSER_STARTED_CORRECTLY = new ThreadLocal<>();
    }

    @Override
    public void preBeforeClass(Class type) {
        CURRENT_BROWSER_CONFIGURATION.set(getExecutionBrowserClassLevel(type));
        if (shouldRestartBrowser()) {
            restartBrowser();
            // TODO: maybe we can simplify and remove this parameter.
            IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(true);
        } else {
            IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        }

        super.preBeforeClass(type);
    }

    @Override
    public void postAfterClass(Class type) {
        shutdownBrowser();
        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        super.preAfterClass(type);
    }

    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        CURRENT_BROWSER_CONFIGURATION.set(getBrowserConfiguration(memberInfo));

        if (!IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.get()) {
            if (shouldRestartBrowser()) {
                restartBrowser();
            }
        }

        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo) {
        if (CURRENT_BROWSER_CONFIGURATION.get().getLifecycle() ==
                Lifecycle.RESTART_ON_FAIL && testResult == TestResult.FAILURE) {
            shutdownBrowser();
            IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        }
    }

    private void shutdownBrowser() {
        DriverService.close();
        PREVIOUS_BROWSER_CONFIGURATION.set(null);
    }

    private void restartBrowser() {
        shutdownBrowser();
        try {
            DriverService.start(CURRENT_BROWSER_CONFIGURATION.get());
            IS_BROWSER_STARTED_CORRECTLY.set(true);
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            IS_BROWSER_STARTED_CORRECTLY.set(false);
        }

        PREVIOUS_BROWSER_CONFIGURATION.set(CURRENT_BROWSER_CONFIGURATION.get());
    }

    private boolean shouldRestartBrowser() {
        // TODO: IsBrowserStartedCorrectly getter?
        var previousConfiguration = PREVIOUS_BROWSER_CONFIGURATION.get();
        var currentConfiguration = CURRENT_BROWSER_CONFIGURATION.get();
        if (previousConfiguration == null) {
            return true;
        } else if (!IS_BROWSER_STARTED_CORRECTLY.get()) {
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
        BrowserConfiguration result;
        var classBrowserType = getExecutionBrowserClassLevel(memberInfo.getDeclaringClass());
        var methodBrowserType = getExecutionBrowserMethodLevel(memberInfo);
        result = Objects.requireNonNullElse(methodBrowserType, classBrowserType);

        return result;
    }

    private BrowserConfiguration getExecutionBrowserMethodLevel(Method memberInfo) {
        var executionBrowserAnnotation = (ExecutionBrowser)memberInfo.getDeclaredAnnotation(ExecutionBrowser.class);
        if (executionBrowserAnnotation == null) {
            return null;
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.lifecycle());
    }

    private BrowserConfiguration getExecutionBrowserClassLevel(Class<?> type) {
        var executionBrowserAnnotation = (ExecutionBrowser)type.getDeclaredAnnotation(ExecutionBrowser.class);
        if (executionBrowserAnnotation == null) {
            var defaultBrowser = Browser.fromText(ConfigurationService.get(WebSettings.class).getDefaultBrowser());
            var defaultLifecycle = Lifecycle.fromText(ConfigurationService.get(WebSettings.class).getDefaultLifeCycle());
            return new BrowserConfiguration(defaultBrowser, defaultLifecycle);
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.lifecycle());
    }
}
