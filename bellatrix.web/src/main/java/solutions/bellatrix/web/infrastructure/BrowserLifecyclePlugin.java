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
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.core.utilities.SingletonFactory;
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
        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS = ThreadLocal.withInitial(() -> false);
        IS_BROWSER_STARTED_CORRECTLY = ThreadLocal.withInitial(() -> false);
    }

    @Override
    public void preBeforeClass(Class type) {
        if (Objects.equals(ConfigurationService.get(WebSettings.class).getExecutionType(), "regular")) {
            CURRENT_BROWSER_CONFIGURATION.set(getExecutionBrowserClassLevel(type));
            if (shouldRestartBrowser()) {
                shutdownBrowser();
                startBrowser();
                // TODO: maybe we can simplify and remove this parameter.
                IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(true);
            } else {
                IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
            }
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
                startBrowser();
            }
        }

        IS_BROWSER_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
    }

    @Override
    public void beforeTestFailed(Exception ex) throws Exception {
        throw ex;
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {

        if (CURRENT_BROWSER_CONFIGURATION.get().getLifecycle() == Lifecycle.REUSE_IF_STARTED) {
           return;
        }

        if (CURRENT_BROWSER_CONFIGURATION.get().getLifecycle() ==
                Lifecycle.RESTART_ON_FAIL && testResult != TestResult.FAILURE ) {
            return;
        }

        shutdownBrowser();

        SingletonFactory.clear();
    }

    private void shutdownBrowser() {
        DriverService.close();
        PREVIOUS_BROWSER_CONFIGURATION.remove();
    }

    private void startBrowser() {
//        shutdownBrowser();
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
        if (!memberInfo.isAnnotationPresent(ExecutionBrowser.class)) return null;

        var executionBrowserAnnotation = (ExecutionBrowser)memberInfo.getDeclaredAnnotation(ExecutionBrowser.class);
        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.deviceName(), executionBrowserAnnotation.lifecycle());
    }

    private BrowserConfiguration getExecutionBrowserClassLevel(Class<?> clazz) {
        var browser = Browser.fromText(SecretsResolver.getSecret(ConfigurationService.get(WebSettings.class).getDefaultBrowser()));
        var lifecycle = Lifecycle.fromText(ConfigurationService.get(WebSettings.class).getDefaultLifeCycle());
        var width = ConfigurationService.get(WebSettings.class).getDefaultBrowserWidth();
        var height = ConfigurationService.get(WebSettings.class).getDefaultBrowserHeight();

        if (clazz.isAnnotationPresent(ExecutionBrowser.class)) {
            var executionBrowserAnnotation = clazz.getDeclaredAnnotation(ExecutionBrowser.class);

            browser = executionBrowserAnnotation.browser() != Browser.NOT_SET && executionBrowserAnnotation.browser() != browser ? executionBrowserAnnotation.browser() : browser;
            lifecycle = executionBrowserAnnotation.lifecycle() != lifecycle ? executionBrowserAnnotation.lifecycle() : lifecycle;
            width = executionBrowserAnnotation.width() != 0 ? executionBrowserAnnotation.width() : width;
            height = executionBrowserAnnotation.height() != 0 ? executionBrowserAnnotation.height() : height;

            if (executionBrowserAnnotation.browser() == Browser.CHROME_MOBILE) {
                return new BrowserConfiguration(executionBrowserAnnotation.deviceName(), lifecycle, clazz.getName());
            }
        }

        return new BrowserConfiguration(browser, lifecycle, width, height);
    }
}
