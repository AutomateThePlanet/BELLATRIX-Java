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

package solutions.bellatrix.web.pages;

import solutions.bellatrix.core.infrastructure.PageObjectModel;
import solutions.bellatrix.web.services.App;
import solutions.bellatrix.web.services.BrowserService;
import solutions.bellatrix.web.services.ComponentCreateService;
import solutions.bellatrix.web.services.JavaScriptService;

public abstract class WebSection<MapT extends PageMap, AssertionsT extends PageAsserts<MapT>> implements PageObjectModel<MapT, AssertionsT> {
    public BrowserService browser() {
        return app().browser();
    }

    public ComponentCreateService create() {
        return app().create();
    }

    public JavaScriptService javaScript() {
        return app().script();
    }

    public App app() {
        return new App();
    }
}
