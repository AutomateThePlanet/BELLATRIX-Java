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

package solutions.bellatrix.android.infrastructure;

import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.plugins.TimeRecord;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.configuration.WebSettings;

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
        IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
        IS_APP_STARTED_CORRECTLY.set(false);
        CURRENT_APP_CONFIGURATION.set(null);
        PREVIOUS_APP_CONFIGURATION.set(null);
    }

    @Override
    public void preBeforeClass(Class type) {
        if (ConfigurationService.get(WebSettings.class).getExecutionType() == "regular") {
            CURRENT_APP_CONFIGURATION.set(getExecutionAppClassLevel(type));
            if (shouldReinstallApp()) {
                reinstallApp();
                // TODO: maybe we can simplify and remove this parameter.
                IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(true);
            } else {
                IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
            }
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

        String testFullName = String.format("%s.%s", memberInfo.getDeclaringClass().getName(), memberInfo.getName());
        CURRENT_APP_CONFIGURATION.get().setTestName(testFullName);

        if (!IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.get()) {
            if (shouldReinstallApp()) {
                reinstallApp();
            }
        }

        IS_APP_STARTED_DURING_PRE_BEFORE_CLASS.set(false);
    }

    @Override
    public void postAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo, Throwable failedTestException) {
        if (CURRENT_APP_CONFIGURATION.get().getLifecycle() == Lifecycle.REUSE_IF_STARTED) {
            return;
        }

        if (CURRENT_APP_CONFIGURATION.get().getLifecycle() == Lifecycle.RESTART_ON_FAIL && testResult == TestResult.SUCCESS) {
            try {
                DriverService.getWrappedAndroidDriver().executeScript("window.location");
            } catch (Exception ex) {
                shutdownApp();
            }

            return;
        }

        shutdownApp();
    }

    @Override
    public void beforeTestFailed(Exception ex) {
        if (CURRENT_APP_CONFIGURATION.get().getLifecycle() == Lifecycle.RESTART_ON_FAIL) {
            shutdownApp();
        }
    }

    private void shutdownApp() {
        DriverService.close();
        SingletonFactory.clear();
        PREVIOUS_APP_CONFIGURATION.set(null);
    }

    private void reinstallApp() {
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

    private boolean shouldReinstallApp() {
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
        String testFullName = String.format("%s.%s", memberInfo.getDeclaringClass().getName(), memberInfo.getName());
        result.setTestName(testFullName);

        return result;
    }

    private AppConfiguration getExecutionAppMethodLevel(Method memberInfo) {
        var executionAppAnnotation = (ExecutionApp)memberInfo.getDeclaredAnnotation(ExecutionApp.class);
        if (executionAppAnnotation == null) {
            return null;
        } else if (executionAppAnnotation.isMobileWebTest()) {
            return new AppConfiguration(true);
        }

        return new AppConfiguration(executionAppAnnotation.lifecycle(), executionAppAnnotation.androidVersion(), executionAppAnnotation.deviceName(), executionAppAnnotation.appPath(), executionAppAnnotation.appPackage(), executionAppAnnotation.appActivity());
    }

    private AppConfiguration getExecutionAppClassLevel(Class<?> type) {
        var executionAppAnnotation = (ExecutionApp)type.getDeclaredAnnotation(ExecutionApp.class);
        if (executionAppAnnotation == null) {
            var defaultAppPath = ConfigurationService.get(AndroidSettings.class).getDefaultAppPath();
            defaultAppPath = PathNormalizer.normalizePath(defaultAppPath);
            var defaultLifecycle = Lifecycle.fromText(ConfigurationService.get(AndroidSettings.class).getDefaultLifeCycle());
            var defaultAppPackage = ConfigurationService.get(AndroidSettings.class).getDefaultAppPackage();
            var defaultAppActivity = ConfigurationService.get(AndroidSettings.class).getDefaultAppActivity();
            var defaultAndroidVersion = ConfigurationService.get(AndroidSettings.class).getDefaultAndroidVersion();
            var defaultDeviceName = ConfigurationService.get(AndroidSettings.class).getDefaultDeviceName();

            return new AppConfiguration(defaultLifecycle, defaultAndroidVersion, defaultDeviceName, defaultAppPath, defaultAppPackage, defaultAppActivity);
        } else if (executionAppAnnotation.isMobileWebTest()) {
            return new AppConfiguration(true);
        }

        return new AppConfiguration(executionAppAnnotation.lifecycle(), executionAppAnnotation.androidVersion(), executionAppAnnotation.deviceName(), PathNormalizer.normalizePath(executionAppAnnotation.appPath()), executionAppAnnotation.appPackage(), executionAppAnnotation.appActivity());
    }
}
