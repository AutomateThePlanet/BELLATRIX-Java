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

public enum Browser {
    CHROME("chrome"),
    CHROME_HEADLESS("chrome"),
    FIREFOX("firefox"),
    FIREFOX_HEADLESS("firefox"),
    EDGE("edge"),
    EDGE_HEADLESS("edge"),
    OPERA("opera"),
    SAFARI("safari"),
    INTERNET_EXPLORER("ie");

    private String value;

    Browser(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
