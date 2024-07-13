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

import io.visual_regression_tracker.sdk_java.VisualRegressionTrackerConfig;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.SingletonFactory;

import java.util.Objects;

@UtilityClass
public class VisualRegressionTracker {
    private static final VisualRegressionTrackerSettings settings = ConfigurationService.get(VisualRegressionTrackerSettings.class);
    private static final VisualRegressionTrackerConfig config = new VisualRegressionTrackerConfig(
            settings.getApiUrl(),
            settings.getApiKey(),
            settings.getProject(),
            settings.getBranch(),
            settings.getCiBuildId(),
            settings.isEnableSoftAssert(),
            settings.getHttpTimeout()
    );
    private static io.visual_regression_tracker.sdk_java.VisualRegressionTracker tracker;

    static {
        tracker = new io.visual_regression_tracker.sdk_java.VisualRegressionTracker(config);
    }

    @SneakyThrows
    public static void takeSnapshot() {
        var caller = Thread.currentThread().getStackTrace()[1]; // gets the caller method of screenshot()
        var name = caller.getMethodName();

        tracker.track(name, Objects.requireNonNull(SingletonFactory.getInstance(ScreenshotPlugin.class)).takeScreenshot(name));
    }

    @SneakyThrows
    public static void takeSnapshot(String name) {
        tracker.track(name, Objects.requireNonNull(SingletonFactory.getInstance(ScreenshotPlugin.class)).takeScreenshot(name));
    }
}
