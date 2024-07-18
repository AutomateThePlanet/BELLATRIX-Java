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

package solutions.bellatrix.web.findstrategies;

import lombok.Getter;
import org.openqa.selenium.By;


/**
 * Strategy that uses CSS to locate elements, but saves the original XPath for debugging purposes. <br>
 */
@Getter
public class ShadowXPathFindStrategy extends FindStrategy {
    String originalXpath;
    public ShadowXPathFindStrategy(String xpath, String css) {
        super(css);
        originalXpath = xpath;
    }

    @Override
    public By convert() {
        return By.cssSelector(getValue());
    }

    @Override
    public String toString() {
        return String.format("xpath = %s ; css = %s", originalXpath, getValue());
    }
}
