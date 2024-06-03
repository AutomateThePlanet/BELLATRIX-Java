/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
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

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestDurationWatcher implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        BaseTest.CURRENT_TEST_TIME_RECORD.get().setStartTime(System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        BaseTest.CURRENT_TEST_TIME_RECORD.get().setEndTime(System.currentTimeMillis());
    }
}
