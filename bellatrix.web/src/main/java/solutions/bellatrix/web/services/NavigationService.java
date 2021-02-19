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

package solutions.bellatrix.web.services;

import com.google.common.base.Strings;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.configuration.WebSettings;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class NavigationService extends WebService {
    // TODO: UrlNotNavigatedEvent

    public void to(String url) {
        getWrappedDriver().navigate().to(url);
    }

    public void NavigateToLocalPage(String filePath)
    {
//        var assembly = Assembly.GetExecutingAssembly();
//        string path = Path.GetDirectoryName(assembly.Location);
//
//        string pageFilePath = Path.Combine(path ?? throw new InvalidOperationException(), filePath);
//
//        if (WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.Safari) || WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.Firefox) || WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.FirefoxHeadless))
//        {
//            pageFilePath = string.Concat("file:///", pageFilePath);
//        }

//        if (RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
//        {
//            pageFilePath = pageFilePath.Replace('\\', '/').Replace("file:////", "file://////").Replace(" ", "%20");
//        }
//
//        if (!WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.Safari))
//        {
//            Navigate(new Uri(pageFilePath, uriKind: UriKind.Absolute));
//        }
//        else
//        {
//            Navigate(pageFilePath);
//        }
    }

    public void waitForPartialUrl(String partialUrl) {
        try
        {
            long waitForPartialTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForPartialUrl();
            long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
            var webDriverWait = new WebDriverWait(getWrappedDriver(), waitForPartialTimeout, sleepInterval);
            webDriverWait.until(d -> getWrappedDriver().getCurrentUrl().contains(partialUrl));
        } catch (Exception ex)
        {
//            UrlNotNavigatedEvent?.Invoke(this, new UrlNotNavigatedEventArgs(ex));
            throw ex;
        }
    }

    public List<String> GetQueryParameter(String parameterName) throws MalformedURLException {
        return splitQuery(getWrappedDriver().getCurrentUrl()).get(parameterName);
    }

    private Map<String, List<String>> splitQuery(String url) throws MalformedURLException {
        if (Strings.isNullOrEmpty(new URL(url).getQuery())) {
            return Collections.emptyMap();
        }
        return Arrays.stream(new URL(url).getQuery().split("&"))
                .map(it -> {
                    try {
                        return splitQueryParameter(it);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
    }

    private AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) throws UnsupportedEncodingException {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(
                URLDecoder.decode(key, "UTF-8"),
                URLDecoder.decode(value, "UTF-8")
        );
    }
}
