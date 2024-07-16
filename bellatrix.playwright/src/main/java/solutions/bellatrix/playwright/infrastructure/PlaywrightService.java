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

import com.google.gson.Gson;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.ShutdownManager;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.core.utilities.TimestampBuilder;
import solutions.bellatrix.playwright.configuration.GridSettings;
import solutions.bellatrix.playwright.configuration.WebSettings;
import solutions.bellatrix.playwright.infrastructure.httptraffic.Requests;
import solutions.bellatrix.playwright.infrastructure.httptraffic.Responses;
import solutions.bellatrix.playwright.infrastructure.httptraffic.Traffic;
import solutions.bellatrix.playwright.utilities.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("ALL")
public class PlaywrightService {
    private static ThreadLocal<PlaywrightManager> PLAYWRIGHT_MANAGER = new ThreadLocal<>();

    static {
        var driverManager = new PlaywrightManager();
        SingletonFactory.register(driverManager);
        ShutdownManager.register(driverManager::close);
        PLAYWRIGHT_MANAGER.set(driverManager);
    }

    public static WrappedBrowser start(BrowserConfiguration configuration) {
        return PLAYWRIGHT_MANAGER.get().start(configuration);
    }

    public static void restartBrowserContext() {
        PLAYWRIGHT_MANAGER.get().restartBrowserContext();
    }
    public static void close() {
        PLAYWRIGHT_MANAGER.get().close();
    }

    public static WrappedBrowser wrappedBrowser() {
        return PLAYWRIGHT_MANAGER.get().wrappedBrowser;
    }

    public static void wrappedBrowser(WrappedBrowser driver) {
        PLAYWRIGHT_MANAGER.get().wrappedBrowser = driver;
    }

    public static BrowserConfiguration browserConfiguration() {
        return PLAYWRIGHT_MANAGER.get().browserConfiguration;
    }

    private static class PlaywrightManager {
        private PlaywrightManager() {
        }

        private boolean disposed = true;
        @Getter private BrowserConfiguration browserConfiguration;
        private Playwright playwright;
        private WrappedBrowser wrappedBrowser;
        private boolean isBuildNameSet = false;
        private String buildName;

        public WrappedBrowser start(BrowserConfiguration configuration) {
            if (disposed) {
                browserConfiguration(configuration);
                playwright = Playwright.create();
                disposed = false;

                var executionType = Settings.web().getExecutionType();

                if (executionType.equals("regular")) {
                    return initializeBrowserWrapperRegularMode();
                } else {
                    var gridSettings = ConfigurationService.get(WebSettings.class).getGridSettings().stream().filter(g -> g.getProviderName().equals(executionType.toLowerCase())).findFirst().orElse(null);
                    assert gridSettings != null : String.format("The specified execution type '%s' is not declared in the configuration", executionType);

                    return initializeBrowserWrapperGridMode(gridSettings);
                }
            }
            else return wrappedBrowser();
        }

        public void close() {
            if (disposed) {
                return;
            }

            if (playwright != null) {
                DebugInformation.debugInfo("SHUTTING DOWN PLAYWRIGHT");

                if (wrappedBrowser() != null) {
                    wrappedBrowser().close();
                }

                playwright = null;

                if (wrappedBrowser().getGridSessionId() != null) {
                    RestAssured.baseURI = ConfigurationService.get(WebSettings.class).getGridSettings().stream().filter(g -> g.getProviderName().equals(Settings.web().getExecutionType().toLowerCase())).findFirst().orElse(null).getUrl();

                    var response = RestAssured.given()
                            .delete(String.format("/session/%s", wrappedBrowser().getGridSessionId()));
                }
            }

            disposed = true;
        }

        public void restartBrowserContext() {
            DebugInformation.debugInfo("RESTARTING CONTEXT");
            wrappedBrowser().changeContext(intializeBrowserContext());
        }

        private WrappedBrowser initializeBrowserWrapperRegularMode() {
            wrappedBrowser(new WrappedBrowser(playwright));

            wrappedBrowser().setBrowser(initializeBrowserRegularMode());
            wrappedBrowser().setCurrentContext(intializeBrowserContext());
            wrappedBrowser().setCurrentPage(wrappedBrowser().getCurrentContext().newPage());

            return wrappedBrowser();
        }

        private WrappedBrowser initializeBrowserWrapperGridMode(GridSettings gridSettings) {
            wrappedBrowser(new WrappedBrowser(playwright));

            wrappedBrowser().setBrowser(initializeBrowserGridMode(gridSettings));
            wrappedBrowser().setCurrentContext(intializeBrowserContext());
            wrappedBrowser().setCurrentPage(wrappedBrowser().getCurrentContext().newPage());

            return wrappedBrowser();
        }

        private Browser initializeBrowserRegularMode() {
            BrowserTypes browserTypes = browserConfiguration().getBrowserTypes();

            switch (browserTypes) {
                case CHROMIUM -> {
                    var browserType = playwright.chromium();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(false);
                    launchOptions.setArgs(List.of("--log-level=3", "--remote-allow-origins=*"));
                    launchOptions.setTimeout(Settings.web().getArtificialDelayBeforeAction());
                    // System.setProperty("webdriver.chrome.silentOutput", "true"); ?

                    return browser(browserType.launch(launchOptions));
                }
                case CHROMIUM_HEADLESS -> {
                    var browserType = playwright.chromium();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(true);
                    launchOptions.setArgs(List.of("--log-level=3"));
                    // System.setProperty("webdriver.chrome.silentOutput", "true"); ?

                    return browser(browserType.launch(launchOptions));
                }
                case CHROME -> {
                    var browserType = playwright.chromium();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setChannel("chrome");
                    launchOptions.setHeadless(false);
                    launchOptions.setArgs(List.of("--log-level=3", "--remote-allow-origins=*"));
                    launchOptions.setTimeout(Settings.web().getArtificialDelayBeforeAction());
                    // System.setProperty("webdriver.chrome.silentOutput", "true"); ?

                    return browser(browserType.launch(launchOptions));
                }
                case CHROME_HEADLESS -> {
                    var browserType = playwright.chromium();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setChannel("chrome");
                    launchOptions.setHeadless(true);
                    launchOptions.setArgs(List.of("--log-level=3"));
                    // System.setProperty("webdriver.chrome.silentOutput", "true"); ?

                    return browser(browserType.launch(launchOptions));
                }
                case FIREFOX -> {
                    var browserType = playwright.firefox();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(false);
                    launchOptions.setTimeout(Settings.web().getArtificialDelayBeforeAction());

                    return browser(browserType.launch(launchOptions));
                }
                case FIREFOX_HEADLESS -> {
                    var browserType = playwright.firefox();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(true);

                    return browser(browserType.launch(launchOptions));
                }
                case EDGE -> {
                    var browserType = playwright.chromium();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setChannel("msedge");
                    launchOptions.setHeadless(false);
                    // launchOptions.setArgs(List.of("--log-level=3"));
                    // System.setProperty("webdriver.chrome.silentOutput", "true"); ?

                    return browser(browserType.launch(launchOptions));
                }
                case EDGE_HEADLESS -> {
                    var browserType = playwright.chromium();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setChannel("msedge");
                    launchOptions.setHeadless(true);
                    // launchOptions.setArgs(List.of("--log-level=3"));
                    // System.setProperty("webdriver.chrome.silentOutput", "true"); ?

                    return browser(browserType.launch(launchOptions));
                }
                case WEBKIT -> {
                    var browserType = playwright.webkit();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(false);
                    launchOptions.setTimeout(Settings.web().getArtificialDelayBeforeAction());

                    return browser(browserType.launch(launchOptions));
                }
                case WEBKIT_HEADLESS -> {
                    var browserType = playwright.webkit();
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(true);

                    return browser(browserType.launch(launchOptions));
                }
                default -> throw new IllegalArgumentException("Unsupported.");
            }
        }

        private BrowserContext intializeBrowserContext() {
            Browser.NewContextOptions options = getContextOptions();
            var context = browser().newContext(options);

            if (Settings.web().getShouldCaptureHttpTraffic()) {
                startRecordingHttpTraffic(context);
            }

            return context;
        }

        // ToDo Browser Context options
        private Browser.NewContextOptions getContextOptions() {
            Browser.NewContextOptions options = new Browser.NewContextOptions();

//        if (Settings.context().isShouldSetContextSettings()) {
//            options.setIgnoreHTTPSErrors(Settings.context().isIgnoreHTTPSErrors());
//            options.setAcceptDownloads(Settings.context().isAcceptDownloads());
//            options.setBypassCSP(Settings.context().isBypassCSP());
//            options.setBaseURL(Settings.web().getBaseUrl());
//            options.setGeolocation(Settings.context().getGeolocation());
//            options.setHttpCredentials(Settings.context().getHTTPCredentials());
//            options.setJavaScriptEnabled(Settings.context().isJsEnabled());
//            options.setLocale(Settings.context().getLocale());
//            options.setTimezoneId(Settings.context().getTimezoneId());
//            options.setUserAgent(Settings.context().getUserAgent());
//        }

            options.setIgnoreHTTPSErrors(true);

            // ToDo setProxy

            return options;
        }

        private void startRecordingHttpTraffic(BrowserContext context) {
            Traffic.getRequestContainers().add(new Requests(context));
            context.onRequest(x -> Traffic.getContextSpecificRequests(context).add(x));

            Traffic.getResponseContainers().add(new Responses(context));
            context.onResponse(x -> Traffic.getContextSpecificResponses(context).add(x));
        }

        private Browser initializeBrowserGridMode(GridSettings gridSettings) {
            var timeout = Settings.timeout().inMilliseconds().getConnectToRemoteGridTimeout();

            var gridUrl = gridSettings.getUrl();
            if (gridUrl.startsWith("env_")) {
                gridUrl = System.getProperty(gridSettings.getUrl()).replace("env_", "");
            }

            try {
                Gson gson = new Gson();
                String serializedSettings = URLEncoder.encode(gson.toJson(gridSettings.getArguments().get(0)), "UTF-8");

                if (gridSettings.getProviderName().equals("grid") || gridSettings.getProviderName().equals("selenoid")) {
                    var browserType = browserConfiguration.getBrowserTypes();
                    if (browserType == BrowserTypes.FIREFOX || browserType == BrowserTypes.FIREFOX_HEADLESS || browserType == BrowserTypes.WEBKIT || browserType == BrowserTypes.WEBKIT_HEADLESS) {
                        throw new NotImplementedException("Playwright supports running in Selenium Grid only Chromium browsers.");
                    }

                    RestAssured.baseURI = gridUrl;
                    Map<String, Object> capabilitiesMap = new HashMap<>();
                    capabilitiesMap.put("alwaysMatch", gridSettings.getArguments().get(0));

                    Map<String, Object> body = new HashMap<>();
                    body.put("capabilities", capabilitiesMap);

                    var serializedBody = gson.toJson(body);

                    var response = RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(serializedBody)
                            .post("/session");

                    wrappedBrowser().setGridSessionId(response.body().jsonPath().get("value.sessionId"));

                    var responseBody = response.body();
                    var responseJson = response.jsonPath();

                    var cdpUrl = new URI(response.jsonPath().get("value.capabilities['se:cdp']"));
                    cdpUrl = new URI(cdpUrl.getScheme(), cdpUrl.getUserInfo(), new URI(gridUrl).getHost(), new URI(gridUrl).getPort(), cdpUrl.getPath(), cdpUrl.getQuery(), cdpUrl.getFragment());

                    return playwright.chromium().connectOverCDP(cdpUrl.toString(), new BrowserType.ConnectOverCDPOptions().setTimeout(timeout));
                } else if (gridSettings.getProviderName().equals("lambdatest")) {
                    return playwright.chromium().connect(String.format("%s?capabilities=%s", gridUrl, serializedSettings), new BrowserType.ConnectOptions().setTimeout(timeout));
                } else if (gridSettings.getProviderName().equals("browserstack")) {
                    return playwright.chromium().connect(String.format("%s?caps=%s", gridUrl, serializedSettings), new BrowserType.ConnectOptions().setTimeout(timeout));
                } else {
                    throw new NotImplementedException("Unsupported grid provider. Supported are: selenium grid, selenoid, lambdatest, and browserstack.");
                }
            } catch (Exception e) {
                DebugInformation.printStackTrace(e);
                return null;
            }
        }

        private void changeWindowSize() {
            try {
                if (browserConfiguration().getHeight() != 0 && browserConfiguration().getWidth() != 0) {
                    wrappedBrowser().getCurrentPage().setViewportSize(browserConfiguration().getWidth(), browserConfiguration().getHeight());
                }
            } catch (Exception ignored) {}
        }

        private String getBuildName() {
            if (!isBuildNameSet) {
                buildName = System.getProperty("buildName");
            }

            if (buildName == null) {
                InputStream input = ConfigurationService.class.getResourceAsStream("/application.properties");
                var p = new Properties();
                try {
                    p.load(input);
                } catch (IOException e) {
                    return null;
                }

                if (!isBuildNameSet) {
                    buildName = p.getProperty("buildName");
                }

                if (buildName.equals("{randomNumber}") && !isBuildNameSet) {
                    buildName = TimestampBuilder.buildUniqueTextByPrefix("LE_");
                    isBuildNameSet = true;
                }
            }

            return buildName;
        }

        public BrowserConfiguration browserConfiguration() {
            return browserConfiguration;
        }

        public BrowserConfiguration browserConfiguration(BrowserConfiguration configuration) {
            browserConfiguration = configuration;
            return browserConfiguration();
        }

        public WrappedBrowser wrappedBrowser() {
            return wrappedBrowser;
        }

        public WrappedBrowser wrappedBrowser(WrappedBrowser wrappedBrowser) {
            this.wrappedBrowser = wrappedBrowser;
            return this.wrappedBrowser;
        }

        private Browser browser() {
            return wrappedBrowser().getBrowser();
        }

        private Browser browser(Browser browser) {
            wrappedBrowser().setBrowser(browser);
            return browser();
        }

        private BrowserContext context() {
            return wrappedBrowser().getCurrentContext();
        }

        private BrowserContext context(BrowserContext browserContext) {
            wrappedBrowser().setCurrentContext(browserContext);
            return context();
        }
    }
}
