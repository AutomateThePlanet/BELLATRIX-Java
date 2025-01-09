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

import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.services.App;
import solutions.bellatrix.web.services.BrowserService;
import solutions.bellatrix.web.services.ComponentCreateService;
import solutions.bellatrix.web.services.JavaScriptService;
import solutions.bellatrix.web.services.NavigationService;

import java.lang.reflect.ParameterizedType;

public abstract class WebPage<MapT extends PageMap, AssertsT extends PageAsserts<MapT>> {
    public BrowserService browser() {
        return new BrowserService();
    }

    public JavaScriptService javaScript() {
        return new JavaScriptService();
    }

    public ComponentCreateService create() {
        return new ComponentCreateService();
    }

    public App app() {
        return new App();
    }

    public MapT map() {
        try {
            var elementsClass = (Class<MapT>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return elementsClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public AssertsT asserts() {
        try {
            var assertionsClass = (Class<AssertsT>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            return assertionsClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public NavigationService navigate() {
        return new NavigationService();
    }

    protected String getUrl() {
        return "";
    }

    public void open() {
        navigate().to(getUrl());
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
        browser().waitForAjax();
    }
}
