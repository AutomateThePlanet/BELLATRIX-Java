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

import java.util.Arrays;

public enum Browser {
    CHROME("chrome"),
    CHROME_HEADLESS("chrome_headless"),
    CHROME_MOBILE("chrome_mobile"),
    FIREFOX("firefox"),
    FIREFOX_HEADLESS("firefox_headless"),
    EDGE("edge"),
    // EDGE_HEADLESS("edge"), // Unsupported by Selenium 3, Selenium 4 has support
    OPERA("opera"),
    SAFARI("safari"),
    NOT_SET("not_set"),
    INTERNET_EXPLORER("ie");

    private final String value;

    Browser(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Browser fromText(String text) {
        return Arrays.stream(values())
                .filter(l -> l.value.equalsIgnoreCase(text))
                .findFirst().orElse(Browser.CHROME);
    }
}
