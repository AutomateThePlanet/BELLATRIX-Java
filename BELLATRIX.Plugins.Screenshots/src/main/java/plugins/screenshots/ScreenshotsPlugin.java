/*
 * Copyright 2021 Automate The Planet Ltd.
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

import org.testng.ITestResult;
import solutions.bellatrix.plugins.EventListener;
import solutions.bellatrix.plugins.Plugin;

import java.lang.reflect.Method;
import java.nio.file.Paths;

public abstract class ScreenshotsPlugin extends Plugin {
    public static final EventListener<ScreenshotPluginEventArgs> SCREENSHOT_GENERATED = new EventListener<>();
    private Boolean isEnabled;

    public ScreenshotsPlugin(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    protected abstract void takeScreenshot(String screenshotSaveDir, String filename);
    protected abstract String getOutputFolder();
    protected abstract String getUniqueFileName(String testName);

    @Override
    public void preAfterTest(ITestResult testResult, Method memberInfo) {
        if (isEnabled) {
           try {
               var screenshotSaveDir = getOutputFolder();
               var screenshotFileName = getUniqueFileName(memberInfo.getName());
               takeScreenshot(screenshotSaveDir, screenshotFileName);
               SCREENSHOT_GENERATED.broadcast(new ScreenshotPluginEventArgs(Paths.get(screenshotSaveDir, screenshotFileName).toString()));
           } catch (Exception e) {
               // ignore since it is failing often because of bugs in Remote driver for Chrome
               e.printStackTrace();
           }
        }
    }
}
