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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import solutions.bellatrix.core.plugins.PluginExecutionEngine;
import solutions.bellatrix.core.plugins.TestResult;

import java.util.Optional;

public class TestResultWatcher implements TestWatcher {
    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE);

        try {
            PluginExecutionEngine.postAfterTest(BaseTest.CURRENT_TEST_RESULT.get(), extensionContext.getTestMethod().get());
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS);

        try {
            PluginExecutionEngine.postAfterTest(BaseTest.CURRENT_TEST_RESULT.get(), extensionContext.getTestMethod().get());
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE);

        try {
            PluginExecutionEngine.postAfterTest(BaseTest.CURRENT_TEST_RESULT.get(), extensionContext.getTestMethod().get());
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS);

        try {
            PluginExecutionEngine.postAfterTest(BaseTest.CURRENT_TEST_RESULT.get(), extensionContext.getTestMethod().get());
        } catch (Exception e) {
            PluginExecutionEngine.afterTestFailed(e);
        }
    }
}