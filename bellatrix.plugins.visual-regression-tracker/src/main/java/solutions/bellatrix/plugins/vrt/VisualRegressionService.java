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

import io.visual_regression_tracker.sdk_java.TestRunOptions;
import io.visual_regression_tracker.sdk_java.TestRunResult;
import io.visual_regression_tracker.sdk_java.VisualRegressionTracker;
import io.visual_regression_tracker.sdk_java.VisualRegressionTrackerConfig;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.SingletonFactory;

import java.io.IOException;

public class VisualRegressionService {
    private static final float DEFAULT_DIFF_TOLERANCE = ConfigurationService.get(VisualRegressionSettings.class).getDefaultDiffTolerance();

    private static final ThreadLocal<VisualRegressionTracker> VISUAL_REGRESSION_TRACKER_THREAD_LOCAL;
    private static final ThreadLocal<VisualRegression> VISUAL_REGRESSION_ATTRIBUTE;

    static {
        try {
            VISUAL_REGRESSION_TRACKER_THREAD_LOCAL = ThreadLocal.withInitial(() -> null);
            VISUAL_REGRESSION_ATTRIBUTE = ThreadLocal.withInitial(() -> null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize VisualRegressionService", e);
        }
    }

    public static VisualRegressionTracker tracker() {
        return VISUAL_REGRESSION_TRACKER_THREAD_LOCAL.get();
    }

    public static void initService(VisualRegression attribute, VisualRegressionSettings settings) {
        VISUAL_REGRESSION_ATTRIBUTE.set(attribute);

        var config = new VisualRegressionTrackerConfig(
                settings.getApiUrl(),
                settings.getApiKey(),
                !attribute.projectName().isBlank() ? attribute.projectName() : settings.getProject(),
                settings.getBranch(),
                settings.getCiBuildId(),
                settings.isEnableSoftAssert(),
                settings.getHttpTimeout()
        );

        VISUAL_REGRESSION_TRACKER_THREAD_LOCAL.set(new VisualRegressionTracker(config));
    }

    public static TestRunResult track(String name, Float diffTolerance, String screenshot) {
        try {
            return tracker().track(name, screenshot, buildRunOptions(diffTolerance));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static TestRunResult track(String name, Float diffTolerance) {
        return track(name, diffTolerance, takeSnapshot(name));
    }

    public static TestRunResult track(String name) {
        return track(name, DEFAULT_DIFF_TOLERANCE);
    }

    public static String takeSnapshot(String name) {
        var screenshotPlugin = SingletonFactory.getInstance(ScreenshotPlugin.class);
        if (screenshotPlugin == null) {
            throw new IllegalArgumentException("It seems that the screenshot plugin isn't registered by the 'ScreenshotPlugin.class' key inside SingletonFactory's map or isn't registered at all!");
        }

        return screenshotPlugin.takeScreenshot(name);
    }

    public static TestRunOptions buildRunOptions(Float diffTolerance) {
        var options =  TestRunOptions.builder()
                .device(System.getProperty("COMPUTERNAME"))
                .os(System.getProperty("os.name"))
                .viewport(String.format("%dx%d", VISUAL_REGRESSION_ATTRIBUTE.get().viewportSize().getWidth(), VISUAL_REGRESSION_ATTRIBUTE.get().viewportSize().getHeight()))
                .customTags(String.format("%s", VISUAL_REGRESSION_ATTRIBUTE.get().viewportSize().name()))
                .diffTollerancePercent(diffTolerance)
                .build();

        return options;
    }

    public static void start() {
        try {
            VISUAL_REGRESSION_TRACKER_THREAD_LOCAL.get().start();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stop() {
        try {
            VISUAL_REGRESSION_TRACKER_THREAD_LOCAL.get().stop();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
