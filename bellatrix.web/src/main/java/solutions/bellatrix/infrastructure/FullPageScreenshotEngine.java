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

package solutions.bellatrix.infrastructure;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.configuration.ConfigurationService;
import solutions.bellatrix.configuration.WebSettings;
import solutions.bellatrix.services.JavaScriptService;
import solutions.bellatrix.utilities.ResourcesReader;
import solutions.bellatrix.utilities.TempFileWriter;
import java.io.File;

public class FullPageScreenshotEngine {
    private static final String GENERATE_SCREENSHOT_JS = "function genScreenshot () {var canvasImgContentDecoded; html2canvas(document.body).then(function(canvas) { window.canvasImgContentDecoded = canvas.toDataURL(\"image/png\"); });}genScreenshot();";

    public static File takeScreenshot() {
        var html2CanvasContent = ResourcesReader.getFileAsString(FullPageScreenshotEngine.class, "html2canvas.js");
        var javaScriptService = new JavaScriptService();
        var timeoutInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        var sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), timeoutInterval, sleepInterval);
        webDriverWait.ignoring(IllegalArgumentException.class);
        javaScriptService.execute(html2CanvasContent);
        javaScriptService.execute(GENERATE_SCREENSHOT_JS);
        webDriverWait.until(
                wd ->
                {
                        var response = (String)javaScriptService.execute("return (typeof canvasImgContentDecoded === 'undefined' || canvasImgContentDecoded === null)");
                        if (StringUtils.isEmpty(response))
                            return false;
                        return Boolean.parseBoolean(response);
                });
        webDriverWait.until(wd -> !StringUtils.isEmpty((String)javaScriptService.execute("return canvasImgContentDecoded;")));
        var pngContent = (String)javaScriptService.execute("return canvasImgContentDecoded;");
        pngContent = pngContent.replace("data:image/png;base64,", "");

        return TempFileWriter.writeStringToTempFile(pngContent);
    }
}
