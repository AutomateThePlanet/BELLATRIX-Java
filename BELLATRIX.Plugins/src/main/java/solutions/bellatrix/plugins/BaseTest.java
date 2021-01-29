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

package solutions.bellatrix.plugins;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    private ITestResult result;
    private final List<Plugin> plugins;

    public BaseTest() {
        this.plugins = new ArrayList<>();
    }

    public void addPlugin(Plugin plugin) {
        PluginExecutionEngine.addPlugin(plugin);
    }

    public String getTestName() {
        return getTestResult().getTestName();
    }

    public void setTestResult(ITestResult result) {
        this.result = result;
    }

    public ITestResult getTestResult() {
        return result;
    }

    @BeforeClass
    public void beforeClassCore() {
        try {
            configure();
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
            setTestResult(result);
            var testClass = this.getClass();
            var methodInfo = testClass.getMethod(getTestResult().getMethod().getMethodName());
            PluginExecutionEngine.preBeforeTest(getTestResult(), methodInfo);
            beforeMethod();
            PluginExecutionEngine.postBeforeTest(getTestResult(), methodInfo);
        } catch (Exception e) {
            PluginExecutionEngine.beforeTestFailed(e);
        }
    }

    @AfterMethod
    public void afterMethodCore() {
        try {
            var testClass = this.getClass();
            var methodInfo = testClass.getMethod(getTestResult().getMethod().getMethodName());
            PluginExecutionEngine.preAfterTest(getTestResult(), methodInfo);
            afterMethod();
            PluginExecutionEngine.postAfterTest(getTestResult(), methodInfo);
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
}
