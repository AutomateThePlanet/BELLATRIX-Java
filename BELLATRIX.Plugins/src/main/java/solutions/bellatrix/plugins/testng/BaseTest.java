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

package solutions.bellatrix.plugins.testng;

import org.testng.ITestResult;
import org.testng.annotations.*;
import solutions.bellatrix.plugins.Plugin;
import solutions.bellatrix.plugins.PluginExecutionEngine;
import solutions.bellatrix.plugins.TestResult;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    private static final ThreadLocal<Boolean> CONFIGURATION_EXECUTED = new ThreadLocal<>();

    public BaseTest() {
        CONFIGURATION_EXECUTED.set(false);
    }

    public void addPlugin(Plugin plugin) {
        PluginExecutionEngine.addPlugin(plugin);
    }

    @BeforeClass
    public void beforeClassCore() {
        try {
            if (!CONFIGURATION_EXECUTED.get()) {
                configure();
                CONFIGURATION_EXECUTED.set(true);
            }

            var testClass = this.getClass();
            PluginExecutionEngine.preBeforeClass(testClass);
            beforeClass();
            PluginExecutionEngine.postBeforeClass(testClass);
        } catch (Exception e) {
            PluginExecutionEngine.beforeClassFailed(e);
        }
    }

    @BeforeMethod
    public void beforeMethodCore(ITestResult result) {
        try {
            var testClass = this.getClass();
            var methodInfo = testClass.getMethod(result.getMethod().getMethodName());
            PluginExecutionEngine.preBeforeTest(convertToTestResult(result), methodInfo);
            beforeMethod();
            PluginExecutionEngine.postBeforeTest(convertToTestResult(result), methodInfo);
        } catch (Exception e) {
            PluginExecutionEngine.beforeTestFailed(e);
        }
    }

    @AfterMethod
    public void afterMethodCore(ITestResult result) {
        try {
            var testClass = this.getClass();
            var methodInfo = testClass.getMethod(result.getMethod().getMethodName());
            PluginExecutionEngine.preAfterTest(convertToTestResult(result), methodInfo);
            afterMethod();
            PluginExecutionEngine.postAfterTest(convertToTestResult(result), methodInfo);
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @AfterClass
    public void afterClassCore() {
        try {
            var testClass = this.getClass();
            PluginExecutionEngine.preAfterClass(testClass);
            afterClass();
            PluginExecutionEngine.postAfterClass(testClass);
        } catch (Exception e) {
            PluginExecutionEngine.afterClassFailed(e);
        }
    }

    protected void configure()
    {
    }

    protected void beforeClass()
    {
    }

    protected void afterClass()
    {
    }

    protected void beforeMethod()
    {
    }

    protected void afterMethod()
    {
    }

    private TestResult convertToTestResult(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.SUCCESS ) {
            return TestResult.FAILURE;
        } else {
            return TestResult.SUCCESS;
        }
    }
}
