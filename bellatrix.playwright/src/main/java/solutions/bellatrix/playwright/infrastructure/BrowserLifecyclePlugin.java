/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.playwright.infrastructure;

import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.playwright.utilities.Settings;

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
        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS = ThreadLocal.withInitial(() -> false);
        IS_BROWSER_STARTED_CORRECTLY = ThreadLocal.withInitial(() -> false);
    }

    @Override
    public void preBeforeClass(Class type) {
        if (Settings.web().getExecutionType().equals("regular")) {
            currentConfiguration(getExecutionBrowserClassLevel(type));
            if (shouldRestartBrowser()) {
                shutdownBrowser();
                startBrowser();
                // TODO: maybe we can simplify and remove this parameter.
                isBrowserStartedDuringPreBeforeClass(true);
            } else {
                isBrowserStartedDuringPreBeforeClass(false);
            }
        } else {
            isBrowserStartedDuringPreBeforeClass(false);
        }

        super.preBeforeClass(type);
    }

    @Override
    public void postAfterClass(Class type) {
        shutdownBrowser();
        isBrowserStartedDuringPreBeforeClass(false);
        super.preAfterClass(type);
    }

    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        currentConfiguration(getBrowserConfiguration(memberInfo));

        if (!isBrowserStartedDuringPreBeforeClass()) {
            if (shouldRestartBrowser()) {
                startBrowser();
            }
        }

        isBrowserStartedDuringPreBeforeClass(false);
    }

    @Override
    public void beforeTestFailed(Exception ex) throws Exception {
        throw ex;
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {

        if (currentConfiguration().getLifecycle() == Lifecycle.REUSE_IF_STARTED) {
            PlaywrightService.restartBrowserContext();
            return;
        }

        if (currentConfiguration().getLifecycle() ==
                Lifecycle.RESTART_ON_FAIL && testResult != TestResult.FAILURE) {
            return;
        }

        shutdownBrowser();
    }

    private void shutdownBrowser() {
        PlaywrightService.close();
        PREVIOUS_BROWSER_CONFIGURATION.remove();
    }

    private void startBrowser() {
        // shutdownBrowser();
        try {
            PlaywrightService.start(currentConfiguration());
            isBrowserStartedCorrectly(true);
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            isBrowserStartedCorrectly(false);
        }

        previousConfiguration(currentConfiguration());
    }

    private boolean shouldRestartBrowser() {
        // TODO: IsBrowserStartedCorrectly getter?
        var previousConfiguration = previousConfiguration();
        var currentConfiguration = currentConfiguration();
        if (previousConfiguration == null) {
            return true;
        } else if (!isBrowserStartedCorrectly()) {
            return true;
        } else if (!previousConfiguration.equals(currentConfiguration)) {
            return true;
        } else if (currentConfiguration.getLifecycle() == Lifecycle.REUSE_IF_STARTED) {
            return false;
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
        String testFullName = String.format("%s.%s", memberInfo.getDeclaringClass().getName(), memberInfo.getName());
        result.setTestName(testFullName);

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
            var defaultBrowser = Browsers.fromText(Settings.web().getDefaultBrowser());
            var defaultLifecycle = Lifecycle.fromText(Settings.web().getDefaultLifeCycle());
            return new BrowserConfiguration(defaultBrowser, defaultLifecycle);
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.lifecycle());
    }

    private BrowserConfiguration currentConfiguration() {
        return CURRENT_BROWSER_CONFIGURATION.get();
    }

    private void currentConfiguration(BrowserConfiguration configuration) {
        CURRENT_BROWSER_CONFIGURATION.set(configuration);
    }

    private BrowserConfiguration previousConfiguration() {
        return PREVIOUS_BROWSER_CONFIGURATION.get();
    }

    private void previousConfiguration(BrowserConfiguration configuration) {
        PREVIOUS_BROWSER_CONFIGURATION.set(configuration);
    }

    private Boolean isBrowserStartedDuringPreBeforeClass() {
        return IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.get();
    }

    private void isBrowserStartedDuringPreBeforeClass(Boolean bool) {
        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(bool);
    }

    private Boolean isBrowserStartedCorrectly() {
        return IS_BROWSER_STARTED_CORRECTLY.get();
    }

    private void isBrowserStartedCorrectly(Boolean bool) {
        IS_BROWSER_STARTED_CORRECTLY.set(bool);
    }
}
