/*
 * Copyright 2025 Automate The Planet Ltd.
 * Author: Miriyam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.desktop.findstrategies;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.plugins.opencv.Base64Encodable;

import java.util.List;

public class ImageBase64FindStrategy extends FindStrategy {
    private final Base64Encodable encodedImage;
    public ImageBase64FindStrategy(Base64Encodable encodedImage) {
        super(encodedImage.getImageName());
        this.encodedImage = encodedImage;
    }

    @Override
    public WebElement findElement(WindowsDriver driver) {
        return driver.findElement(AppiumBy.image(encodedImage.getBase64Image()));
    }

    @Override
    public List<WebElement> findAllElements(WindowsDriver driver) {
        return driver.findElements(AppiumBy.image(encodedImage.getBase64Image()));
    }

    @Override
    public WebElement findElement(WebElement element) {
        return element.findElement(AppiumBy.image(encodedImage.getBase64Image()));
    }

    @Override
    public List<WebElement> findAllElements(WebElement element) {
        return element.findElements(AppiumBy.image(encodedImage.getBase64Image()));
    }

    @Override
    public String toString() {
        return String.format("image = %s", getValue());
    }
}
