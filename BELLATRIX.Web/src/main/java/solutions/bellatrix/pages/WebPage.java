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

package solutions.bellatrix.pages;

import solutions.bellatrix.services.BrowserService;
import solutions.bellatrix.services.ComponentCreateService;
import solutions.bellatrix.services.NavigationService;

import java.lang.reflect.ParameterizedType;

public abstract class WebPage<ComponentsT extends PageComponents, AssertsT extends PageAsserts<ComponentsT>>  {
    public BrowserService browser() {
        return new BrowserService();
    }

    public ComponentCreateService create() {
        return new ComponentCreateService();
    }

    public ComponentsT elements() {
        try {
            var elementsClass = (Class<ComponentsT>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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

    protected abstract String getUrl();

    public void open() {
        navigate().to(getUrl());
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
    }
}
