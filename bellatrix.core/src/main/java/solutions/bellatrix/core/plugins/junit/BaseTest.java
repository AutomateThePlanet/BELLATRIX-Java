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

package solutions.bellatrix.core.plugins.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import solutions.bellatrix.core.plugins.PluginExecutionEngine;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.plugins.TimeRecord;
import solutions.bellatrix.core.plugins.UsesPlugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestResultWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TestDurationWatcher.class)
public class BaseTest extends UsesPlugins {
    static final ThreadLocal<TestResult> CURRENT_TEST_RESULT = new ThreadLocal<>();
    static final ThreadLocal<TimeRecord> CURRENT_TEST_TIME_RECORD = ThreadLocal.withInitial(TimeRecord::new);
    private static final ThreadLocal<Boolean> CONFIGURATION_EXECUTED = ThreadLocal.withInitial(() -> false);
    private static final List<String> ALREADY_EXECUTED_BEFORE_CLASSES = Collections.synchronizedList(new ArrayList<>());
    private TestInfo testInfo;

    @BeforeEach
    public void beforeMethodCore(TestInfo testInfo) throws Exception {
        try {
            assert testInfo.getTestClass().isPresent();
            this.testInfo = testInfo;
            var currentTestClassName = testInfo.getTestClass().get().getName();
            if (!ALREADY_EXECUTED_BEFORE_CLASSES.contains(currentTestClassName)) {
                beforeClassCore();
                ALREADY_EXECUTED_BEFORE_CLASSES.add(testInfo.getTestClass().get().getName());
            }

            var testClass = this.getClass();
            assert testInfo.getTestMethod().isPresent();
            var methodInfo = Arrays.stream(testClass.getMethods()).filter(m -> m.getName() == testInfo.getTestMethod().get().getName()).findFirst().get();
            PluginExecutionEngine.preBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo);
            beforeEach();
            PluginExecutionEngine.postBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo);
        } catch (Exception e) {
            e.printStackTrace();
            onBeforeEachFailure();
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
            e.printStackTrace();
            PluginExecutionEngine.beforeClassFailed(e);
        }
    }

    @AfterEach
    public void afterMethodCore(TestInfo testInfo) {
        try {
            var testClass = this.getClass();
            assert testInfo.getTestMethod().isPresent();
            var methodInfo = testClass.getMethod(testInfo.getTestMethod().get().getName(), testInfo.getTestMethod().get().getParameterTypes());
            PluginExecutionEngine.preAfterTest(CURRENT_TEST_RESULT.get(), methodInfo); // DEPRECATED, LEFT FOR COMPATIBILITY
            PluginExecutionEngine.preAfterTest(CURRENT_TEST_RESULT.get(), CURRENT_TEST_TIME_RECORD.get(), methodInfo);
            afterEach();
           // PluginExecutionEngine.postAfterTest(CURRENT_TEST_RESULT.get(), methodInfo);
        } catch (Exception e) {
            e.printStackTrace();
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @AfterAll
    public void afterClassCore(TestInfo testInfo) {
        try {
            var testClass = testInfo.getTestClass();
            if (testClass.isPresent()) {
                PluginExecutionEngine.preAfterClass(testClass.get());
                afterClass();
                PluginExecutionEngine.postAfterClass(testClass.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            PluginExecutionEngine.afterClassFailed(e);
        }
    }

    protected String getTestName() {
        return this.testInfo.getTestMethod().get().getName();
    }

    protected void configure() {
    }

    protected void beforeAll() throws Exception {
    }

    protected void beforeEach() throws Exception {
    }

    protected void onBeforeEachFailure() {
    }

    protected void afterEach() {
    }

    protected void afterClass() {
    }
}
