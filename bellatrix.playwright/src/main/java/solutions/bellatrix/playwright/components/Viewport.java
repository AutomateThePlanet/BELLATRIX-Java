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

package solutions.bellatrix.playwright.components;

import layout.LayoutComponent;
import solutions.bellatrix.playwright.services.JavaScriptService;

import java.awt.*;

public class Viewport implements LayoutComponent {
    private final Point location;
    private final Dimension size;

    Viewport() {
        var javaScriptService = new JavaScriptService();
        location = new Point(0, 0);
        var viewportWidth = Integer.parseInt(javaScriptService.execute("return Math.max(document.documentElement.clientWidth, window.innerWidth || 0);").toString());
        var viewportHeight = Integer.parseInt(javaScriptService.execute("return Math.max(document.documentElement.clientHeight, window.innerHeight || 0);").toString());
        size = new Dimension(viewportWidth, viewportHeight);
    }

    @Override
    public String getComponentName() {
        return "Viewport";
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public Dimension getSize() {
        return size;
    }
}
