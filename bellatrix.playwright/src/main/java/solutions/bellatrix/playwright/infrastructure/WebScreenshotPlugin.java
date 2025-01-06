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

package solutions.bellatrix.playwright.infrastructure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import plugins.screenshots.ScreenshotPlugin;
import plugins.screenshots.ScreenshotPluginEventArgs;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.playwright.utilities.Settings;

import java.io.File;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class WebScreenshotPlugin extends ScreenshotPlugin {
    public WebScreenshotPlugin() {
        super(Settings.web().getScreenshotsOnFailEnabled());
    }

    @Override
    public byte[] takeScreenshot() {
        return PlaywrightService.wrappedBrowser().getCurrentPage()
                .screenshot(new Page.ScreenshotOptions()
                        .setType(ScreenshotType.PNG)
                        .setFullPage(false));
    }

    @Override
    public String takeScreenshot(String name) {
        var screenshotSaveDir = getOutputFolder();
        var filename = getUniqueFileName(name);

        var path = Paths.get(screenshotSaveDir, filename);
        var screenshot = PlaywrightService.wrappedBrowser().getCurrentPage()
                .screenshot(new Page.ScreenshotOptions()
                        .setPath(path)
                        .setType(ScreenshotType.PNG)
                );

        var image = Base64.getEncoder().encodeToString(screenshot);

        SCREENSHOT_GENERATED.broadcast(new ScreenshotPluginEventArgs(path.toString(), filename, image));
        return image;
    }

    @Override
    public String takeScreenshot(String screenshotSaveDir, String filename) {
        var path = Paths.get(screenshotSaveDir, filename);
        var screenshot = PlaywrightService.wrappedBrowser().getCurrentPage()
                .screenshot(new Page.ScreenshotOptions()
                        .setPath(path)
                        .setType(ScreenshotType.PNG)
                );

        var image = Base64.getEncoder().encodeToString(screenshot);

        SCREENSHOT_GENERATED.broadcast(new ScreenshotPluginEventArgs(path.toString(), filename, image));
        return image;
    }

    @Override
    protected String getOutputFolder() {
        String saveLocation = Settings.web().getScreenshotsSaveLocation();
        saveLocation = PathNormalizer.normalizePath(saveLocation);

        var directory = new File(saveLocation);
        if (directory.mkdirs()) {
            return saveLocation;
        }

        return saveLocation;
    }

    @Override
    protected String getUniqueFileName(String testName) {
        return testName.concat(UUID.randomUUID().toString());
    }
}
