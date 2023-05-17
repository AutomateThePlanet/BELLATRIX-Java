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

package solutions.bellatrix.core.plugins.testng;

import org.testng.ITestResult;
import org.testng.annotations.*;
import solutions.bellatrix.core.plugins.PluginExecutionEngine;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.plugins.UsesPlugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Listeners(TestResultListener.class)
public class BaseTest extends UsesPlugins {
    static final ThreadLocal<TestResult> CURRENT_TEST_RESULT = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> CONFIGURATION_EXECUTED = new ThreadLocal<>();
    private static final List<String> ALREADY_EXECUTED_BEFORE_CLASSES = Collections.synchronizedList(new ArrayList<>());

    public BaseTest() {
//        try {
//            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//            theUnsafe.setAccessible(true);
//            Unsafe u = (Unsafe)theUnsafe.get(null);
//
//            Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
//            Field logger = cls.getDeclaredField("logger");
//            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
//        } catch (Exception ignored) {}
        CONFIGURATION_EXECUTED.set(false);
    }

    @BeforeClass
    public void beforeClass() {
        try {
            if (!CONFIGURATION_EXECUTED.get()) {
                configure();
                CONFIGURATION_EXECUTED.set(true);
            }
            var testClass = this.getClass();
            PluginExecutionEngine.preBeforeClass(testClass);
            beforeAll();
            PluginExecutionEngine.postBeforeClass(testClass);
        } catch (Exception e) {
            PluginExecutionEngine.beforeClassFailed(e);
        }
    }

    @BeforeMethod
    public void beforeMethodCore(ITestResult result) throws Exception {
        try {
//            var currentTestClassName = this.getClass().getName();
//            if (!ALREADY_EXECUTED_BEFORE_CLASSES.contains(currentTestClassName)) {
//                beforeClassCore();
//                ALREADY_EXECUTED_BEFORE_CLASSES.add(currentTestClassName);
//            }

            var methodInfo = this.getClass().getMethod(result.getMethod().getMethodName());
            PluginExecutionEngine.preBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo);
            beforeEach();
            PluginExecutionEngine.postBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo);
        } catch (Exception e) {
            e.printStackTrace();
            PluginExecutionEngine.beforeTestFailed(e);
        }
    }

    @AfterMethod
    public void afterMethodCore(ITestResult result) {
        try {
            var testClass = this.getClass();
            var methodInfo = testClass.getMethod(result.getMethod().getMethodName());
            PluginExecutionEngine.preAfterTest(CURRENT_TEST_RESULT.get(), methodInfo);
            afterEach();
            PluginExecutionEngine.postAfterTest(CURRENT_TEST_RESULT.get(), methodInfo, result.getThrowable());
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @AfterClass
    public void afterClassCore() {
        try {
            var testClass = this.getClass();
            PluginExecutionEngine.preAfterClass(testClass);
            afterAll();
            PluginExecutionEngine.postAfterClass(testClass);
        } catch (Exception e) {
            PluginExecutionEngine.afterClassFailed(e);
        }
    }

//    public void beforeClassCore() {
//        try {
//            if (!CONFIGURATION_EXECUTED.get()) {
//                configure();
//                CONFIGURATION_EXECUTED.set(true);
//            }
//            var testClass = this.getClass();
//            PluginExecutionEngine.preBeforeClass(testClass);
//            beforeAll();
//            PluginExecutionEngine.postBeforeClass(testClass);
//        } catch (Exception e) {
//            PluginExecutionEngine.beforeClassFailed(e);
//        }
//    }

    protected void configure() {
    }

    protected void beforeAll() throws Exception {
    }

    protected void afterAll() throws Exception {
    }

    protected void beforeEach() throws Exception {
    }

    protected void afterEach() {
    }
}
