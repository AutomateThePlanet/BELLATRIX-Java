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

package solutions.bellatrix.playwright.utilities;

import org.apache.http.client.utils.URIBuilder;
import solutions.bellatrix.playwright.configuration.UrlSettings;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

public class UrlDeterminer {
    private static UrlSettings urlSettings() {
        return Settings.url();
    }

    public static String getUrl(Function<UrlSettings, String> expression) {
        return expression.apply(urlSettings());
    }

    public static String getUrl(Function<UrlSettings, String> expression, String part) {
        var url = expression.apply(urlSettings());
        return contactUrls(url, part);
    }

    public static String getUrl(String url, String part) {
        return contactUrls(url, part);
    }

    private static String contactUrls(String url, String part) {
        try {
            var uriBuilder = new URIBuilder(url);
            URI uri = uriBuilder.setPath(uriBuilder.getPath() + part)
                    .build()
                    .normalize();
            return uri.toString();
        } catch (URISyntaxException ex) {
            return null;
        }
    }
}
