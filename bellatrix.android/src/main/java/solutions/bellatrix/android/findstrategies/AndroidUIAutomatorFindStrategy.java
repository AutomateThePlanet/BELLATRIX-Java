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

package solutions.bellatrix.android.findstrategies;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AndroidUIAutomatorFindStrategy extends FindStrategy {
    public AndroidUIAutomatorFindStrategy(String value) {
        super(value);
    }

    @Override
    public WebElement findElement(AndroidDriver driver) {
        return driver.findElement(AppiumBy.androidUIAutomator(getValue()));
    }

    @Override
    public List<WebElement> findAllElements(AndroidDriver driver) {
        return driver.findElements(AppiumBy.androidUIAutomator(getValue()));
    }

    @Override
    public WebElement findElement(WebElement element) {
        // AndroidUIAutomatorFindStrategy not supported for searching inside another element.
        return null;
    }

    @Override
    public List<WebElement> findAllElements(WebElement element) {
        // AndroidUIAutomatorFindStrategy not supported for searching inside another element.
        return null;
    }

    @Override
    public String toString() {
        return String.format("androidUIAutomator = %s", getValue());
    }
}
