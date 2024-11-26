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

import io.visual_regression_tracker.sdk_java.TestRunResult;
import io.visual_regression_tracker.sdk_java.TestRunStatus;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.PageObjectModel;

@UtilityClass
public class VisualRegressionAssertions {
    private static float defaultDiffTolerance() {
        return ConfigurationService.get(VisualRegressionSettings.class).getDefaultDiffTolerance();
    }

    public static void assertSameAsBaseline(String name, String... testCasesName) {
        TestRunResult result = VisualRegressionService.track(name + String.join(" ", testCasesName), defaultDiffTolerance());
        Assertions.assertSame(result.getTestRunResponse().getStatus(), TestRunStatus.OK,
                String.format("Visual comparison has detected a difference at: %s", result.getTestRunResponse().getUrl()));
    }

    public static void assertSameAsBaseline(String name, String screenshot, String... testCasesName) {
        TestRunResult result = VisualRegressionService.track(name + String.join(" ", testCasesName), defaultDiffTolerance(), screenshot);
        Assertions.assertSame(result.getTestRunResponse().getStatus(), TestRunStatus.OK,
                String.format("Visual comparison has detected a difference at: %s", result.getTestRunResponse().getUrl()));
    }

    public static void assertSameAsBaseline(PageObjectModel pageObjectModel, String... testCasesName) {
        String name = pageObjectModel.getClass().getSimpleName();
        assertSameAsBaseline(name, testCasesName);
    }

    public static void assertSameAsBaseline(PageObjectModel pageObjectModel) {
        assertSameAsBaseline(pageObjectModel, "");
    }

    public static void assertSameAsBaseline(TestRunResult result) {
        Assertions.assertSame(result.getTestRunResponse().getStatus(), TestRunStatus.OK,
                String.format("Visual comparison has detected a difference at: %s", result.getTestRunResponse().getUrl()));
    }
}
