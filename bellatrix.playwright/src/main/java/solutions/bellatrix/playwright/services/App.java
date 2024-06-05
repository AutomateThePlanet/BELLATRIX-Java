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

package solutions.bellatrix.playwright.services;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.infrastructure.PlaywrightService;
import solutions.bellatrix.playwright.pages.WebPage;
import solutions.bellatrix.playwright.pages.WebSection;

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

    public ComponentCreateService create() {
        return SingletonFactory.getInstance(ComponentCreateService.class);
    }

    public DialogService dialogs() {
        return SingletonFactory.getInstance(DialogService.class);
    }

    public JavaScriptService script() {
        return SingletonFactory.getInstance(JavaScriptService.class);
    }

    public NetworkService network() {
        return SingletonFactory.getInstance(NetworkService.class);
    }

    public <TPage extends WebPage> TPage goTo(Class<TPage> pageOf, Object... args) {
        var page = SingletonFactory.getInstance(pageOf, args);
        assert page != null;
        page.open();

        return page;
    }

    public <TPage extends WebPage> TPage createPage(Class<TPage> pageOf, Object... args) {
        return SingletonFactory.getInstance(pageOf, args);
    }

    public <TSection extends WebSection> TSection createSection(Class<TSection> sectionOf, Object... args) {
        return SingletonFactory.getInstance(sectionOf, args);
    }

    /**
     * @deprecated
     * This method is obsolete, use createPage or createSection instead.
     * <p> Use {@link App#createPage(Class, Object...)} instead.
     */
    public <TPage extends WebPage> TPage create(Class<TPage> pageOf, Object... args) {
        return SingletonFactory.getInstance(pageOf, args);
    }

    @Override
    public void close() {
        if (disposed) {
            return;
        }

        PlaywrightService.close();

        disposed = true;
    }
}
