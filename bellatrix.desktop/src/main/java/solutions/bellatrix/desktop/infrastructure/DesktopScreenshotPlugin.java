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

package solutions.bellatrix.desktop.infrastructure;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.desktop.configuration.DesktopSettings;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class DesktopScreenshotPlugin extends ScreenshotPlugin {
    public DesktopScreenshotPlugin() {
        super(ConfigurationService.get(DesktopSettings.class).getScreenshotsOnFailEnabled());
    }

    @Override
    @SneakyThrows
    protected void takeScreenshot(String screenshotSaveDir, String filename) {
        File screenshot = ((TakesScreenshot)DriverService.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        var destFile = new File(Paths.get(screenshotSaveDir, filename) + ".png");
        FileUtils.copyFile(screenshot, destFile);
    }

    @Override
    protected String getOutputFolder() {
        String saveLocation = ConfigurationService.get(DesktopSettings.class).getScreenshotsSaveLocation();
        if (saveLocation.startsWith("user.home")) {
            var userHomeDir = System.getProperty("user.home");
            saveLocation = saveLocation.replace("user.home", userHomeDir);
        }

        var directory = new File(saveLocation);
        if (directory.mkdirs()) {
            return saveLocation;
        }

        return saveLocation;
    }

    @Override
    protected String getUniqueFileName(String testName) {
        return testName.concat(UUID.randomUUID().toString()).concat(".png");
    }
}
