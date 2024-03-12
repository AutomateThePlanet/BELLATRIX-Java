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

package services;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class CookiesService extends WebService {
    private BrowserContext context() {
        return wrappedBrowser().currentContext();
    }

    public void addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie);

        context().addCookies(cookies);
    }

    public void addCookie(Cookie cookie) {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie);

        context().addCookies(cookies);
    }

    public void deleteAllCookies() {
        context().clearCookies();
    }

    public void deleteCookie(String name) {
        var cookieToRemove = context().cookies().stream()
                .filter(x -> x.name.equals(name))
                .findFirst().orElse(null);

        context().cookies().remove(cookieToRemove);
    }

    public List<Cookie> cookies() {
        return context().cookies();
    }

    public Cookie getCookie(String name) {
        return context().cookies().stream()
                .filter(x -> x.name.equals(name))
                .findFirst().orElse(null);
    }
}
