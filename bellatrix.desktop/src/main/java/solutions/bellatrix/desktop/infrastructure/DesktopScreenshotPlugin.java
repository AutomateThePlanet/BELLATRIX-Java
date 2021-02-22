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

package solutions.bellatrix.desktop.infrastructure;

import lombok.SneakyThrows;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.desktop.configuration.DesktopSettings;

import java.io.File;
import java.util.UUID;

public class DesktopScreenshotPlugin extends ScreenshotPlugin {
    public DesktopScreenshotPlugin(Boolean isEnabled) {
        super(isEnabled);
    }

    public static DesktopScreenshotPlugin of() {
        Boolean isEnabled = ConfigurationService.get(DesktopSettings.class).getScreenshotsOnFailEnabled();
        return new DesktopScreenshotPlugin(isEnabled);
    }

    @Override
    @SneakyThrows
    protected void takeScreenshot( String screenshotSaveDir, String filename) {
//        var screenshot = new AShot()
//                .shootingStrategy(DesktopSettings.viewportPasting(100))
//                .takeScreenshot(DriverService.getWrappedDriver());
//        var destFile = new File(Paths.get(screenshotSaveDir, filename).toString());
//        ImageIO.write(screenshot.getImage(), "png", destFile);
    }

    @Override
    protected String getOutputFolder() {
        String saveLocation = ConfigurationService.get(DesktopSettings.class).getScreenshotsSaveLocation();
        if (saveLocation.startsWith("user.home")) {
            var userHomeDir = System.getProperty("user.home");
            saveLocation = saveLocation.replace("user.home", userHomeDir);
        }

        var directory = new File(saveLocation);
        if (! directory.exists()){
            directory.mkdirs();
        }

        return saveLocation;
    }

    @Override
    protected String getUniqueFileName(String testName) {
        return testName.concat(UUID.randomUUID().toString()).concat(".png");
    }
}
