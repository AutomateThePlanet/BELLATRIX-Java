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

package solutions.bellatrix.playwright.pages;

import solutions.bellatrix.core.infrastructure.PageObjectModel;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.PageObjectModel;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.services.App;
import solutions.bellatrix.playwright.services.BrowserService;
import solutions.bellatrix.playwright.services.ComponentCreateService;
import solutions.bellatrix.playwright.services.NavigationService;

public abstract class WebPage<MapT extends PageMap, AssertionsT extends PageAsserts<MapT>> implements PageObjectModel<MapT, AssertionsT> {
    public ComponentCreateService create() {
        return SingletonFactory.getInstance(ComponentCreateService.class);
    }
    public BrowserService browser() {
        return SingletonFactory.getInstance(BrowserService.class);
    }

    public App app() {
        return SingletonFactory.getInstance(App.class);
    }

    public NavigationService navigate() {
        return SingletonFactory.getInstance(NavigationService.class);
    }

    protected String getUrl() {
        return "";
    }

    public void open() {
        navigate().to(getUrl());
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
    }
}
