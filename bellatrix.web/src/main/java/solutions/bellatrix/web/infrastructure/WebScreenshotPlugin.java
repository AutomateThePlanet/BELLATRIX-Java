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

import nu.pattern.OpenCV;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import plugins.screenshots.ScreenshotPlugin;
import plugins.screenshots.ScreenshotPluginEventArgs;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.PathNormalizer;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.services.BrowserService;
import solutions.bellatrix.web.services.WebService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class WebScreenshotPlugin extends ScreenshotPlugin {
    public WebScreenshotPlugin() {
        super(ConfigurationService.get(WebSettings.class).getScreenshotsOnFailEnabled());
    }

    @Override
    public byte[] takeScreenshot() {
        return ((TakesScreenshot)DriverService.getWrappedDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public String takeScreenshot(String name) {
        var screenshotSaveDir = getOutputFolder();
        var filename = getUniqueFileName(name);

        var screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(DriverService.getWrappedDriver());

        var path = Paths.get(screenshotSaveDir, filename).toString();
        var destFile = new File(path);
        Log.info("Saving screenshot with path: " + destFile);
        try {
            ImageIO.write(screenshot.getImage(), "png", destFile);
        } catch (IOException e) {
            Log.error(e.toString());
        }

        var base64image = bufferedImageToBase64(screenshot.getImage());

        SCREENSHOT_GENERATED.broadcast(new ScreenshotPluginEventArgs(path.toString(), filename, base64image));
        return base64image;
    }

    @Override
    public String takeScreenshot(String screenshotSaveDir, String filename) {
        var screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(DriverService.getWrappedDriver());

        var path = Paths.get(screenshotSaveDir, filename).toString();
        var destFile = new File(path);
        Log.info("Saving screenshot with path: " + destFile);
        try {
            ImageIO.write(screenshot.getImage(), "png", destFile);
        } catch (IOException e) {
            Log.error(e.toString());
        }

        var base64image = bufferedImageToBase64(screenshot.getImage());

        SCREENSHOT_GENERATED.broadcast(new ScreenshotPluginEventArgs(path.toString(), filename, base64image));
        return base64image;
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

    private static String bufferedImageToBase64(BufferedImage image) {
        String base64String = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            base64String = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64String;
    }
}
