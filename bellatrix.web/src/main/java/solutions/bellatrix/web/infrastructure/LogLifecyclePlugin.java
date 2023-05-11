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

package solutions.bellatrix.web.infrastructure;

import plugins.video.VideoPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.web.configuration.WebSettings;

import java.io.File;
import java.lang.reflect.Method;
import java.util.UUID;

public class LogLifecyclePlugin extends Plugin {
    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        Log.info("");
        Log.info("==================================================================================");
        Log.info("Start Test " + memberInfo.getDeclaringClass().getName(), " " + memberInfo.getName());
        Log.info("");
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        Log.info("");
        Log.info("==================================================================================");
        Log.info("");
    }
}
