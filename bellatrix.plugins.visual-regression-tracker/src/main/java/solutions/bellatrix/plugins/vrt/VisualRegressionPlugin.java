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

package solutions.bellatrix.plugins.vrt;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;

import java.lang.reflect.Method;

public class VisualRegressionPlugin extends Plugin {
    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        VisualRegressionService.stop();
    }

    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        VisualRegression attribute = getVisualRegressionConfiguration(memberInfo);

        if (attribute != null) {
            VisualRegressionService.initService(attribute, ConfigurationService.get(VisualRegressionSettings.class));
            VisualRegressionService.start();
        }
    }

    private VisualRegression getVisualRegressionConfiguration(Method memberInfo) {
        var classAttribute = (VisualRegression)memberInfo.getDeclaringClass().getDeclaredAnnotation(VisualRegression.class);
        var methodAttribute = (VisualRegression)memberInfo.getDeclaredAnnotation(VisualRegression.class);
        if (methodAttribute != null) {
            return  methodAttribute;
        }

        return classAttribute;
    }
}
