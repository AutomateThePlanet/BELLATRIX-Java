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

package solutions.bellatrix.web.services;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.infrastructure.DriverService;
import solutions.bellatrix.web.infrastructure.ProxyServer;
import solutions.bellatrix.web.pages.WebPage;

public class App implements AutoCloseable {
    private boolean disposed = false;

    public NavigationService navigate() {
        return SingletonFactory.getInstance(NavigationService.class);
    }

    public BrowserService browser() {
        return SingletonFactory.getInstance(BrowserService.class);
    }

    public CookiesService cookies() {
        return SingletonFactory.getInstance(CookiesService.class);
    }

    public ProxyServer requests() {
        return SingletonFactory.getInstance(ProxyServer.class);
    }

    public DialogService dialogs() {
        return SingletonFactory.getInstance(DialogService.class);
    }

    public JavaScriptService script() {
        return SingletonFactory.getInstance(JavaScriptService.class);
    }

    public ComponentCreateService create() {
        return SingletonFactory.getInstance(ComponentCreateService.class);
    }

    public ComponentWaitService waitFor() {
        return SingletonFactory.getInstance(ComponentWaitService.class);
    }

    public void addDriverOptions(String key, String value) {
        DriverService.addDriverOptions(key, value);
    }

    public <TPage extends WebPage> TPage goTo(Class<TPage> pageOf, Object... args) {
        var page = SingletonFactory.getInstance(pageOf, args);
        assert page != null;
        page.open();

        return page;
    }

    public <TPage extends WebPage> TPage create(Class<TPage> pageOf, Object... args) {
        return SingletonFactory.getInstance(pageOf, args);
    }

    @Override
    public void close() {
        if (disposed) {
            return;
        }

        DriverService.close();

        disposed = true;
    }
}
