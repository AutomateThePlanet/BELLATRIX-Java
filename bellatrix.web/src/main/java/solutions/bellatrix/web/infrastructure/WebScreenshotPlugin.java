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

package solutions.bellatrix.web.infrastructure;

import lombok.SneakyThrows;
import plugins.screenshots.ScreenshotPlugin;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.web.configuration.WebSettings;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class WebScreenshotPlugin extends ScreenshotPlugin {
    public WebScreenshotPlugin() {
        super(ConfigurationService.get(WebSettings.class).getScreenshotsOnFailEnabled());
    }

    @Override
    @SneakyThrows
    protected void takeScreenshot(String screenshotSaveDir, String filename) {
        var screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(DriverService.getWrappedDriver());
        var destFile = new File(Paths.get(screenshotSaveDir, filename).toString());
        Log.info("Saving screenshot with path: " + destFile);
        ImageIO.write(screenshot.getImage(), "png", destFile);
    }

    @Override
    protected String getOutputFolder() {
        String saveLocation = ConfigurationService.get(WebSettings.class).getScreenshotsSaveLocation();
        saveLocation = PathNormalizer.normalizePath(saveLocation);

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
