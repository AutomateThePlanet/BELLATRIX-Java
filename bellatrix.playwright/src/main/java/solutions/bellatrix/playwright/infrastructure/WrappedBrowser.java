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

package solutions.bellatrix.playwright.infrastructure;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.core.utilities.SingletonFactory;

@Data
public class WrappedBrowser {
    public WrappedBrowser(Playwright playwright) {
        this.playwright = playwright;
        SingletonFactory.register(this);
    }

    public WrappedBrowser(Playwright playwright, Browser browser, BrowserContext context, Page page) {
        this.playwright = playwright;
        this.browser = browser;
        currentContext = context;
        currentPage = page;
        SingletonFactory.register(this);
    }

    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE) private Playwright playwright;
    private Browser browser;
    private BrowserContext currentContext;
    private Page currentPage;
    private String gridSessionId;

    public void close() {
        playwright.close();
    }

    public void changeContext(BrowserContext context) {
        for (var page : currentContext.pages()) page.close();
        currentContext.close();

        setCurrentContext(context);
        setCurrentPage(context.newPage());
    }
}
