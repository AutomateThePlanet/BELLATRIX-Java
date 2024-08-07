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

package solutions.bellatrix.core.plugins;

import java.io.IOException;
import java.lang.reflect.Method;

public abstract class Plugin {
    public Plugin() {
        PluginExecutionEngine.addPlugin(this);
    }

    public void preBeforeClass(Class type) {
    }

    public void postBeforeClass(Class type) {
    }

    public void beforeClassFailed(Exception e) {
    }

    public void preBeforeTest(TestResult testResult, Method memberInfo) throws Exception {
    }

    public void postBeforeTest(TestResult testResult, Method memberInfo) {
    }

    public void beforeTestFailed(Exception e) throws Exception {
    }

    /**
     * Deprecated. <br>
     * Use {@link #preAfterTest(TestResult, TimeRecord, Method)} as it offers more information about the test.
     */
    @Deprecated
    public void preAfterTest(TestResult testResult, Method memberInfo) throws IOException {
    }

    public void preAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo) throws IOException {
    }

    /**
     * Deprecated. <br>
     * Use {@link #postAfterTest(TestResult, TimeRecord, Method, Throwable)} as it offers more information about the test.
     */
    @Deprecated
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
    }

    public void postAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo, Throwable failedTestException) {
    }

    public void afterTestFailed(Exception e) {
    }

    public void preAfterClass(Class type) {
    }

    public void postAfterClass(Class type) {
    }

    public void afterClassFailed(Exception e) {
    }
}
