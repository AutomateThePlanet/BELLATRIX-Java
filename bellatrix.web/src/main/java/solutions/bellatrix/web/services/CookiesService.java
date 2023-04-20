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

import org.openqa.selenium.Cookie;

import java.util.Set;

public class CookiesService extends WebService {
    public void addCookie(String cookieName, String cookieValue, String path) {
        getWrappedDriver().manage().addCookie(new Cookie(cookieName, cookieValue, path));
    }

    public void addCookie(String cookieName, String cookieValue) {
        addCookie(cookieName, cookieValue, "/");
    }

    public void addCookie(Cookie cookieToAdd) {
        getWrappedDriver().manage().addCookie(cookieToAdd);
    }

    public void deleteAllCookies() {
        getWrappedDriver().manage().deleteAllCookies();
    }

    public void deleteCookie(String cookieName) {
        getWrappedDriver().manage().deleteCookieNamed(cookieName);
    }

    public Set<Cookie> getAllCookies() {
        return getWrappedDriver().manage().getCookies();
    }

    public Cookie getCookie(String cookieName) {
        return getWrappedDriver().manage().getCookieNamed(cookieName);
    }
}
