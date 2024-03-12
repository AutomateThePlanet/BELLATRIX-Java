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

package infrastructure;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import utilities.Settings;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.utilities.PathNormalizer;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class WebScreenshotPlugin extends ScreenshotPlugin {
    public WebScreenshotPlugin() {
        super(Settings.web().getScreenshotsOnFailEnabled());
    }

    @Override
    protected void takeScreenshot(String screenshotSaveDir, String filename) {
        // ToDo test taking a screenshot
        var screenshot = PlaywrightService.wrappedBrowser().currentPage()
                .screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(screenshotSaveDir, filename))
                        .setType(ScreenshotType.PNG)
                );
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
