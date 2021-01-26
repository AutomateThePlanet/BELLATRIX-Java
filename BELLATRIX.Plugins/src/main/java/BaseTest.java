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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    private ITestResult result;

    public String getTestName() {
        return getTestResult().getTestName();
    }

    public void setTestResult(ITestResult result) {
        this.result = result;
    }

    public ITestResult getTestResult() {
        return result;
    }

    // TODO: extend for before, after class, etc.
    @BeforeMethod
    public void beforeMethod(ITestResult result) throws NoSuchMethodException, ClassNotFoundException {
        setTestResult(result);
        var testClass = this.getClass();
        var methodInfo = testClass.getMethod(getTestResult().getMethod().getMethodName());
        PluginExecutionEngine.preTestInit(getTestResult(), methodInfo);
        testInit();
        PluginExecutionEngine.postTestInit(getTestResult(), methodInfo);
    }

    @AfterMethod
    public void afterMethod() throws NoSuchMethodException {
        var testClass = this.getClass();
        var methodInfo = testClass.getMethod(getTestResult().getMethod().getMethodName());
        PluginExecutionEngine.preTestCleanup(getTestResult(), methodInfo);
        testCleanup();
        PluginExecutionEngine.postTestCleanup(getTestResult(), methodInfo);
    }

    protected void testInit()
    {
    }

    protected void testCleanup()
    {
    }
}
