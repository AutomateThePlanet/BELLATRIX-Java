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

package solutions.bellatrix.core.plugins.junit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.PluginExecutionEngine;
import solutions.bellatrix.core.plugins.TestResult;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestResultListener.class)
public class BaseTest {
    static final ThreadLocal<TestResult> CURRENT_TEST_RESULT = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> CONFIGURATION_EXECUTED = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> ALREADY_EXECUTED_BEFORE_CLASSES = new ThreadLocal<>();

    public BaseTest() {
        CONFIGURATION_EXECUTED.set(false);
        ALREADY_EXECUTED_BEFORE_CLASSES.set(new ArrayList<>());
    }

    public void addPlugin(Plugin plugin) {
        PluginExecutionEngine.addPlugin(plugin);
    }

    @BeforeEach
    public void beforeMethodCore(TestInfo testInfo) {
        try {
            assert testInfo.getTestClass().isPresent();
            if (!ALREADY_EXECUTED_BEFORE_CLASSES.get().contains(testInfo.getTestClass().get().getName())) {
                beforeClassCore();
                ALREADY_EXECUTED_BEFORE_CLASSES.get().add(testInfo.getTestClass().get().getName());
            }

            var testClass = this.getClass();
            assert testInfo.getTestMethod().isPresent();
            var methodInfo = testClass.getMethod(testInfo.getTestMethod().get().getName());
            PluginExecutionEngine.preBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo);
            beforeMethod();
            PluginExecutionEngine.postBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo);
        } catch (Exception e) {
            PluginExecutionEngine.beforeTestFailed(e);
        }
    }

    public void beforeClassCore() {
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

    @AfterEach
    public void afterMethodCore(TestInfo testInfo) {
        try {
            var testClass = this.getClass();
            assert testInfo.getTestMethod().isPresent();
            var methodInfo = testClass.getMethod(testInfo.getTestMethod().get().getName());
            PluginExecutionEngine.preAfterTest(CURRENT_TEST_RESULT.get(), methodInfo);
            afterMethod();
            PluginExecutionEngine.postAfterTest(CURRENT_TEST_RESULT.get(), methodInfo);
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @AfterAll
    public static void afterClassCore(TestInfo testInfo) {
        try {
            var testClass = testInfo.getTestClass();
            if (testClass.isPresent()) {
                PluginExecutionEngine.preAfterClass(testClass.get());
                PluginExecutionEngine.postAfterClass(testClass.get());
            }
        } catch (Exception e) {
            PluginExecutionEngine.afterClassFailed(e);
        }
    }

    protected void configure() {
    }

    protected void beforeAll() {
    }

    protected void beforeMethod() {
    }

    protected void afterMethod() {
    }
}
