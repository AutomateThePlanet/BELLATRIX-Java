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

package solutions.bellatrix.desktop.infrastructure;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.desktop.configuration.DesktopSettings;

import java.lang.reflect.Method;

public class AppLifecyclePlugin extends Plugin {
    private static ThreadLocal<AppConfiguration> currentAppConfiguration;
    private static ThreadLocal<AppConfiguration> previousAppConfiguration;
    private static ThreadLocal<Boolean> isAppStartedDuringPreBeforeClass;
    private static ThreadLocal<Boolean> isAppStartedCorrectly;

    static {
        currentAppConfiguration = new ThreadLocal<>();
        previousAppConfiguration = new ThreadLocal<>();
        isAppStartedDuringPreBeforeClass = new ThreadLocal<>();
        isAppStartedCorrectly = new ThreadLocal<>();
    }

    public static AppLifecyclePlugin of() {
        return new AppLifecyclePlugin();
    }

    @Override
    public void preBeforeClass(Class type) {
        currentAppConfiguration.set(getExecutionAppClassLevel(type));
        if (shouldRestartApp()) {
            restartApp();
            // TODO: maybe we can simplify and remove this parameter.
            isAppStartedDuringPreBeforeClass.set(true);
        } else {
            isAppStartedDuringPreBeforeClass.set(false);
        }

        super.preBeforeClass(type);
    }

    @Override
    public void postAfterClass(Class type) {
        shutdownApp();
        isAppStartedDuringPreBeforeClass.set(false);
        super.preAfterClass(type);
    }

    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        currentAppConfiguration.set(getAppConfiguration(memberInfo));

        if (!isAppStartedDuringPreBeforeClass.get()) {
            if (shouldRestartApp()) {
                restartApp();
            }
        }

        isAppStartedDuringPreBeforeClass.set(false);
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo) {
        if (currentAppConfiguration.get().getLifecycle() ==
                Lifecycle.RESTART_ON_FAIL && testResult == TestResult.FAILURE) {
            shutdownApp();
            isAppStartedDuringPreBeforeClass.set(false);
        }
    }

    private void shutdownApp() {
        DriverService.close();
        previousAppConfiguration.set(null);
    }

    private void restartApp() {
        shutdownApp();
        try {
            DriverService.start(currentAppConfiguration.get());
            isAppStartedCorrectly.set(true);
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            isAppStartedCorrectly.set(false);
        }

        previousAppConfiguration.set(currentAppConfiguration.get());
    }

    private Boolean shouldRestartApp() {
        // TODO: IsAppStartedCorrectly getter?
        var previousConfiguration = previousAppConfiguration.get();
        var currentConfiguration = currentAppConfiguration.get();
        if (previousConfiguration == null) {
            return true;
        } else if (!isAppStartedCorrectly.get()) {
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
        AppConfiguration result = null;
        var classAppType = getExecutionAppClassLevel(memberInfo.getDeclaringClass());
        var methodAppType = getExecutionAppMethodLevel(memberInfo);
        if (methodAppType != null) {
            result = methodAppType;
        } else if (classAppType != null) {
            result = classAppType;
        }

        return result;
    }

    private AppConfiguration getExecutionAppMethodLevel(Method memberInfo) {
        var executionAppAnnotation = (ExecutionApp) memberInfo.getDeclaredAnnotation(ExecutionApp.class);
        if (executionAppAnnotation == null) {
            return null;
        }

        return new AppConfiguration(executionAppAnnotation.lifecycle(), executionAppAnnotation.appPath());
    }

    private AppConfiguration getExecutionAppClassLevel(Class<?> type) {
        var executionAppAnnotation = (ExecutionApp) type.getDeclaredAnnotation(ExecutionApp.class);
        if (executionAppAnnotation == null) {
            var defaultAppPath = ConfigurationService.get(DesktopSettings.class).getDefaultAppPath();
            var defaultLifecycle = Lifecycle.fromText(ConfigurationService.get(DesktopSettings.class).getDefaultLifeCycle());

            return new AppConfiguration(defaultLifecycle, defaultAppPath);
        }

        return new AppConfiguration(executionAppAnnotation.lifecycle(), executionAppAnnotation.appPath());
    }
}
