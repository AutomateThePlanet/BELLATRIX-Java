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

import lombok.Getter;

@Getter
public enum DeviceName {
    NOT_SET(),
    IPHONE_13_PRO_MOBILE("iPhone 13 Pro", 375, 667, true, 2, true, true, "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12A366 Safari/600.1.4"),
    IPHONE_12_PRO_MOBILE("iPhone 12 Pro", 390, 844, true, 2, true, true, "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12A366 Safari/600.1.4"),
    IPHONE_SE_MOBILE("iPhone SE", 750, 1334, true, 2, true, true, "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12A366 Safari/600.1.4"),
    IPHONE_X_MOBILE("iPhone X", 375, 667, true, 3, true, true, "Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Version/10.0 Mobile/14D27 Safari/602.1"),
    NEXUS_7_TABLET("Nexus 7", 600, 960, true, 2, true, true, "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 7 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.136 Safari/537.36"),
    IPAD("Apple iPad", 768, 1024, true, 2, true, true, "Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3");

    private final String name;
    private final int width;
    private final int height;
    private final boolean isMobile;
    private final int scaleFactor;
    private final boolean supportsConsoleLogs;
    private final boolean supportsFullPage;
    private final String userAgent;

    DeviceName(String name, int width, int height, boolean isMobile, int scaleFactor, boolean supportsConsoleLogs, boolean supportsFullPage) {
        this(name, width, height, isMobile, scaleFactor, supportsConsoleLogs, supportsFullPage, null);
    }

    DeviceName() {
        this(null, 0, 0, false, 0, false, false, null);
    }

    DeviceName(String name, int width, int height, boolean isMobile, int scaleFactor, boolean supportsConsoleLogs, boolean supportsFullPage, String userAgent) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.isMobile = isMobile;
        this.scaleFactor = scaleFactor;
        this.supportsConsoleLogs = supportsConsoleLogs;
        this.supportsFullPage = supportsFullPage;
        this.userAgent = userAgent;
    }
}
