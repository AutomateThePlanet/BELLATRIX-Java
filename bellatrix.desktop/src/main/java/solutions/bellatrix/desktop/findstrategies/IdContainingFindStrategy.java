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

package solutions.bellatrix.desktop.findstrategies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.windows.WindowsDriver;

import java.util.List;

public class IdContainingFindStrategy extends FindStrategy {
    private final String XPATH_CONTAINING_EXPRESSION = "//*[contains(@id, '%s')]";

    public IdContainingFindStrategy(String value)
    {
        super(value);
    }

    @Override
    public WebElement findElement(WindowsDriver<WebElement> driver) {
        return driver.findElementByXPath(String.format(XPATH_CONTAINING_EXPRESSION, getValue()));
    }

    @Override
    public List<WebElement> findAllElements(WindowsDriver<WebElement> driver) {
        return driver.findElementsByXPath(String.format(XPATH_CONTAINING_EXPRESSION, getValue()));
    }

    @Override
    public WebElement findElement(WebElement element) {
        return element.findElement(By.xpath(String.format(XPATH_CONTAINING_EXPRESSION, getValue())));
    }

    @Override
    public List<WebElement> findAllElements(WebElement element) {
        return element.findElements(By.xpath(String.format(XPATH_CONTAINING_EXPRESSION, getValue())));
    }

    @Override
    public String toString() {
        return String.format("id containing %s", getValue());
    }
}