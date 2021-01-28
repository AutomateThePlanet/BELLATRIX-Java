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

package solutions.bellatrix.services;

import solutions.bellatrix.infrastructure.DriverService;
import solutions.bellatrix.pages.NavigatableWebPage;
import solutions.bellatrix.pages.WebPage;
import solutions.bellatrix.utilities.SingletonFactory;

public class App implements AutoCloseable {
    private Boolean disposed = false;

    public NavigationService navigate() {
        return SingletonFactory.getInstance(NavigationService.class);
    }

    public BrowserService browser() {
        return SingletonFactory.getInstance(BrowserService.class);
    }

    public CookiesService cookies() {
        return SingletonFactory.getInstance(CookiesService.class);
    }

    public DialogService dialogs() {
        return SingletonFactory.getInstance(DialogService.class);
    }

    public JavaScriptService javaScript() {
        return SingletonFactory.getInstance(JavaScriptService.class);
    }

    public ComponentCreationService create() {
        return SingletonFactory.getInstance(ComponentCreationService.class);
    }

    public ComponentWaitService waitComponent() {
        return SingletonFactory.getInstance(ComponentWaitService.class);
    }

    public <TPage extends NavigatableWebPage> TPage pageGoTo(Class<TPage> pageOf, Object... args)
    {
        var page = SingletonFactory.getInstance(pageOf, args);
        page.open();

        return page;
    }

    public <TPage extends WebPage> TPage page(Class<TPage> pageOf, Object... args)
    {
        return SingletonFactory.getInstance(pageOf, args);
    }

    @Override
    public void close() {
        if (disposed)
        {
            return;
        }

        DriverService.close();

        disposed = true;
    }
}
