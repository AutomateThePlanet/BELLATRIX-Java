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

import org.testng.ITestResult;

import java.lang.reflect.Method;

public class BrowserLifecyclePlugin extends Plugin {
    private ThreadLocal<BrowserConfiguration> currentBrowserConfiguration;
    private ThreadLocal<BrowserConfiguration> previousBrowserConfiguration;
    private ThreadLocal<Boolean> isBrowserStartedDuringPreTestsArrange;

    @Override
    public void preBeforeTest(ITestResult testResult, Method memberInfo) {
        currentBrowserConfiguration.set(getBrowserConfiguration(memberInfo));

        Boolean shouldRestartBrowser = shouldRestartBrowser(currentBrowserConfiguration.get());

        if (shouldRestartBrowser)
        {
            restartBrowser();
        }

        previousBrowserConfiguration = currentBrowserConfiguration;
    }

    @Override
    public void postAfterTest(ITestResult testResult, Method memberInfo) {
        if (currentBrowserConfiguration.get().getBrowserBehavior() ==
                Lifecycle.RESTART_ON_FAIL && testResult.getStatus() == ITestResult.FAILURE)
        {
            restartBrowser();
        }
    }

    private void restartBrowser()
    {
        DriverService.close();
        DriverService.start(currentBrowserConfiguration.get());
    }

    private Boolean shouldRestartBrowser(BrowserConfiguration browserConfiguration)
    {
        if (previousBrowserConfiguration == null)
        {
            return true;
        }

        Boolean shouldRestartBrowser =
                browserConfiguration.getBrowserBehavior() == Lifecycle.RESTART_EVERY_TIME;

        return shouldRestartBrowser;
    }

    private BrowserConfiguration getBrowserConfiguration(Method memberInfo)
    {
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

    private BrowserConfiguration getExecutionBrowserMethodLevel(Method memberInfo)
    {
        var executionBrowserAnnotation = (ExecutionBrowser)memberInfo.getDeclaredAnnotation(ExecutionBrowser.class);
        if (executionBrowserAnnotation == null)
        {
            return null;
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.browserBehavior());
    }

    private BrowserConfiguration getExecutionBrowserClassLevel(Class<?> type)
    {
        var executionBrowserAnnotation = (ExecutionBrowser)type.getDeclaredAnnotation(ExecutionBrowser.class);
        if (executionBrowserAnnotation == null)
        {
            return null;
        }

        return new BrowserConfiguration(executionBrowserAnnotation.browser(), executionBrowserAnnotation.browserBehavior());
    }
}
