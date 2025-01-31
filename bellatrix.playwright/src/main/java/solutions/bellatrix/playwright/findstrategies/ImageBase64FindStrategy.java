/*
 * Copyright 2025 Automate The Planet Ltd.
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

package solutions.bellatrix.playwright.findstrategies;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.core.utilities.parsing.TypeParser;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.services.JavaScriptService;
import solutions.bellatrix.plugins.opencv.Base64Encodable;
import solutions.bellatrix.plugins.opencv.OpenCvService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageBase64FindStrategy extends FindStrategy {
    private final Base64Encodable encodedImage;

    public ImageBase64FindStrategy(Base64Encodable encodedImage) {
        this.encodedImage = encodedImage;
    }

    @Override
    public WebElement convert(Page page) {
        var location = OpenCvService.getLocation(encodedImage, false);

        var foundLocators = findElementsOn(location, page);

        this.webElement = new WebElement(foundLocators);

        return webElement;
    }

    @Override
    public WebElement convert(WebElement webElement) {
        var location = OpenCvService.getLocation(encodedImage, false);

        var foundLocators = findElementsOn(location, webElement.page());

        this.webElement = new WebElement(foundLocators);

        return webElement;
    }

    private static List<Locator> findElementsOn(org.opencv.core.Point coordinates, Page page) {
        var elementsXpaths = (List<String>)page.evaluate("(%s)(%s, %s);".formatted(js, TypeParser.parse(coordinates.x, Integer.class), TypeParser.parse(coordinates.y, Integer.class)));

        var locators = new ArrayList<Locator>();

        for (var i = 0; i < elementsXpaths.size(); i++) {
            if (!elementsXpaths.get(i).isBlank())
                locators.add(page.locator("/" + elementsXpaths.get(i)));
        }

        return locators;
    }

    private static String js = """
            function getElementsOn(x, y) {
                function getAbsoluteXpath(element) {
                    function indexElement(el) {
                        let index = 1;
                        
                        let previousSibling = el.previousElementSibling;
                        while (previousSibling) {
                            if (previousSibling.nodeName.toLowerCase() === el.nodeName.toLowerCase()) {
                                index++;
                            }
                            previousSibling = previousSibling.previousElementSibling;
                        }
                        
                        return "/" + el.tagName.toLowerCase() + "[" + index + "]";
                    }
                        
                    let xpath = [];
                        
                    let currentElement = element;
                    while (currentElement) {
                        if (currentElement.tagName.toLowerCase() === 'html' || currentElement.tagName.toLowerCase() === 'body' || currentElement.tagName.startsWith() === '#' || currentElement.tagName.toLowerCase() === "temporary-div") {
                            break;
                        }
                        
                        xpath.unshift(indexElement(currentElement));
                        
                        currentElement = currentElement.parentElement;
                    }
                    return xpath.join("");
                }
             
              const elements = document.elementsFromPoint(x, y);
              const elementsXpaths = [];
             
              for (let i = 0; i < elements.length; i++) {
                elementsXpaths.push(getAbsoluteXpath(elements[i]));
              }
             
              return elementsXpaths;
            }
            """;
}
