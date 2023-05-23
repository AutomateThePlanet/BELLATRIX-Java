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

package solutions.bellatrix.desktop.infrastructure;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.desktop.configuration.DesktopSettings;

import java.lang.reflect.Method;
import java.util.Objects;

public class AppLifecyclePlugin extends Plugin {
    private static final ThreadLocal<AppConfiguration> CURRENT_APP_CONFIGURATION;
    private static final ThreadLocal<AppConfiguration> PREVIOUS_APP_CONFIGURATION;
    private static final ThreadLocal<Boolean> IS_APP_STARTED_DURING_PRE_BEFORE_CLASS;
    private static final ThreadLocal<Boolean> IS_APP_STARTED_CORRECTLY;

    static {
        CURRENT_APP_CONFIGURATION = new ThreadLocal<>();
        PREVIOUS_APP_CONFIGURATION = new ThreadLocal<>();
        IS_APP_STARTED_DURING_PRE_BEFORE_CLASS = new ThreadLocal<>();
        IS_APP_STARTED_CORRECTLY = new ThreadLocal<>();
    }

    @Override
    public void preBeforeClass(Class type) {
        CURRENT_APP_CONFIGURATION.set(getExecutionAppClassLevel(type));
        if (shouldRestartApp()) {
            restartApp();
            // TODO: maybe we can simplify and remove this parameter.
            IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(true);
        } else {
            IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        }

        super.preBeforeClass(type);
    }

    @Override
    public void postAfterClass(Class type) {
        shutdownApp();
        IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        super.preAfterClass(type);
    }

    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        CURRENT_APP_CONFIGURATION.set(getAppConfiguration(memberInfo));

        if (!IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.get()) {
            if (shouldRestartApp()) {
                restartApp();
            }
        }

        IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        if (CURRENT_APP_CONFIGURATION.get().getLifecycle() ==
                Lifecycle.RESTART_ON_FAIL && testResult == TestResult.FAILURE) {
            shutdownApp();
            IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        }
    }

    private void shutdownApp() {
        DriverService.close();
        PREVIOUS_APP_CONFIGURATION.set(null);
    }

    private void restartApp() {
        shutdownApp();
        try {
            DriverService.start(CURRENT_APP_CONFIGURATION.get());
            IS_APP_STARTED_CORRECTLY.set(true);
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            IS_APP_STARTED_CORRECTLY.set(false);
        }

        PREVIOUS_APP_CONFIGURATION.set(CURRENT_APP_CONFIGURATION.get());
    }

    private boolean shouldRestartApp() {
        // TODO: IsAppStartedCorrectly getter?
        var previousConfiguration = PREVIOUS_APP_CONFIGURATION.get();
        var currentConfiguration = CURRENT_APP_CONFIGURATION.get();
        if (previousConfiguration == null) {
            return true;
        } else if (!IS_APP_STARTED_CORRECTLY.get()) {
            return true;
        } else if (!previousConfiguration.equals(currentConfiguration)) {
            return true;
        } else if (currentConfiguration.getLifecycle() == Lifecycle.RESTART_EVERY_TIME) {
            return true;
        } else {
            return false;
        }
    }

    private AppConfiguration getAppConfiguration(Method memberInfo) {
        AppConfiguration result;
        var classAppType = getExecutionAppClassLevel(memberInfo.getDeclaringClass());
        var methodAppType = getExecutionAppMethodLevel(memberInfo);
        result = Objects.requireNonNullElse(methodAppType, classAppType);

        return result;
    }

    private AppConfiguration getExecutionAppMethodLevel(Method memberInfo) {
        var executionAppAnnotation = (ExecutionApp)memberInfo.getDeclaredAnnotation(ExecutionApp.class);
        if (executionAppAnnotation == null) {
            return null;
        }

        return new AppConfiguration(executionAppAnnotation.lifecycle(), executionAppAnnotation.appPath());
    }

    private AppConfiguration getExecutionAppClassLevel(Class<?> type) {
        var executionAppAnnotation = (ExecutionApp)type.getDeclaredAnnotation(ExecutionApp.class);
        if (executionAppAnnotation == null) {
            var defaultAppPath = ConfigurationService.get(DesktopSettings.class).getDefaultAppPath();
            defaultAppPath = PathNormalizer.normalizePath(defaultAppPath);
            var defaultLifecycle = Lifecycle.fromText(ConfigurationService.get(DesktopSettings.class).getDefaultLifeCycle());

            return new AppConfiguration(defaultLifecycle, defaultAppPath);
        }

        return new AppConfiguration(executionAppAnnotation.lifecycle(), executionAppAnnotation.appPath());
    }
}
