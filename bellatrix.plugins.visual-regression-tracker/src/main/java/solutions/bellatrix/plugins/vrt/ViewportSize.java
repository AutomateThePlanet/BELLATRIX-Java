/*
 * Copyright 2024 Automate The Planet Ltd.
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

package solutions.bellatrix.plugins.vrt;

import lombok.Getter;

@Getter
public enum ViewportSize {

    MOBILE_S(300, 873),
    MOBILE_M(320, 873),
    MOBILE_L(393, 873),
    TABLET(768, 873),
    DESKTOP_M(1280, 873),
    DESKTOP(1440, 873),
    DESKTOP_TALL(1440, 1500),
    IPHONE_X(375, 667),
    NEXUS_7_TABLET(600, 960),
    IPAD(768, 1024),
    IPHONE_SE(375, 667),
    IPHONE_13_PRO(375, 667),
    IPHONE_12_PRO(390, 844);

    private final int width;
    private final int height;

    ViewportSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String getName(){
        return this.name();
    }

    @Override
    public String toString() {
        return String.format("%sx%s", getWidth(), getHeight());
    }
}
