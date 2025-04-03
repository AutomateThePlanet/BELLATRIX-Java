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

package plugins.screenshots;

import lombok.SneakyThrows;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.plugins.TimeRecord;

import java.lang.reflect.Method;
import java.nio.file.Paths;

public abstract class ScreenshotPlugin extends Plugin {
    public static final EventListener<ScreenshotPluginEventArgs> SCREENSHOT_GENERATED = new EventListener<>();
    private final boolean isEnabled;

    public ScreenshotPlugin(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public abstract byte[] takeScreenshot();
    public abstract String takeScreenshot(String fileName);
    public abstract String takeScreenshot(String screenshotSaveDir, String filename);
    protected abstract String getOutputFolder();
    protected abstract String getUniqueFileName(String testName);

    @Override
    @SneakyThrows
    public void preAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo) {
        if (!isEnabled || testResult == TestResult.SUCCESS)
            return;

        var screenshotSaveDir = getOutputFolder();
        var screenshotFileName = getUniqueFileName(memberInfo.getName());
        var image = takeScreenshot(screenshotSaveDir, screenshotFileName);
        SCREENSHOT_GENERATED.broadcast(new ScreenshotPluginEventArgs(Paths.get(screenshotSaveDir, screenshotFileName).toString(), screenshotFileName, image));
    }
}
