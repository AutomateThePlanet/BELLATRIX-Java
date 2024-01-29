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

package solutions.bellatrix.web.infrastructure;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.http.HttpStatus;
import org.asynchttpclient.uri.Uri;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.opentest4j.AssertionFailedError;
import org.testng.Assert;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.configuration.WebSettings;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProxyServer {
    private static final ThreadLocal<BrowserMobProxyServer> PROXY_SERVER = ThreadLocal.withInitial(BrowserMobProxyServer::new);
    private static final ThreadLocal<Integer> PORT = new ThreadLocal<>();
    private static final List<Integer> successHttpStatusesList = Arrays.asList(
            HttpStatus.SC_OK,
            HttpStatus.SC_CREATED,
            HttpStatus.SC_ACCEPTED,
            HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,
            HttpStatus.SC_NO_CONTENT,
            HttpStatus.SC_RESET_CONTENT,
            HttpStatus.SC_UNPROCESSABLE_ENTITY,
            HttpStatus.SC_PARTIAL_CONTENT,
            HttpStatus.SC_MULTI_STATUS);

    @SneakyThrows
    public static int init() {
        int port = findFreePort();
        PROXY_SERVER.get().start(port);
        PROXY_SERVER.get().enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        PORT.set(port);
        return port;
    }

    public static BrowserMobProxyServer get() {
        return PROXY_SERVER.get();
    }

    public static void newHar(String name) {
        PROXY_SERVER.get().newHar(name);
    }

    public static void newHar() {
        PROXY_SERVER.get().newHar();
    }

    public static void close() {
        BrowserMobProxyServer proxyServer = PROXY_SERVER.get();
        if (proxyServer != null) {
            proxyServer.stop();
            PROXY_SERVER.remove();
            PORT.remove();
        }
    }

    public static List<HarEntry> getCapturedEntries() {
        return PROXY_SERVER.get().getHar().getLog().getEntries();
    }

    public static void assertNoErrorCodes() {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areThereErrorCodes = harEntries.stream().anyMatch(r
                -> r.getResponse().getStatus() > 400
                && r.getResponse().getStatus() < 599);

        Assert.assertFalse(areThereErrorCodes);
    }

    public static void assertRequestMade(String url) {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areRequestsMade = harEntries.stream().anyMatch(r -> r.getRequest().getUrl().contains(url));
        String simiarRequestsString = getSimilarRequestsString(url, harEntries);
        Assert.assertTrue(areRequestsMade, String.format("The expected url '%s' was not loaded! Similar requests: %s", url, simiarRequestsString));
    }

    public static void assertRequestNotMade(String url, HttpMethod httpMethod) {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areRequestsMade = harEntries.stream().anyMatch(r -> r.getRequest().getUrl().contains(url) && r.getRequest().getMethod().equals(httpMethod.toString()));

        Assert.assertFalse(areRequestsMade);
    }

    public static void clearHistory() {
        var oldHarCount = PROXY_SERVER.get().getHar().getLog().getEntries().stream().count();

        PROXY_SERVER.get().newHar();
        Log.info(String.format("The proxy history with %s entries is cleared!", oldHarCount));
    }

    public static void waitForRequest(WebDriver driver, String requestPartialUrl, HttpMethod httpMethod, int additionalTimeoutInSeconds) {
        long timeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout + additionalTimeoutInSeconds), Duration.ofSeconds(sleepInterval));

        try {
            webDriverWait.until(d -> {
                var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
                var areRequestsMade = harEntries.stream().anyMatch(r -> r.getRequest().getUrl().contains(requestPartialUrl) && r.getRequest().getMethod().equals(httpMethod.toString()));

                return areRequestsMade;
            });
        }
        catch (TimeoutException exception){
            Log.error(String.format("The expected request with URL '%s' is not loaded!", requestPartialUrl));
        }
    }

    public static void waitForResponse(WebDriver driver, String requestPartialUrl, HttpMethod httpMethod, int additionalTimeoutInSeconds) {
        long timeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitForAjaxTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout + additionalTimeoutInSeconds), Duration.ofSeconds(sleepInterval));
        List<HarEntry> allHarEntries = new ArrayList<>();
        try {
            webDriverWait.until(d -> {
                var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
                var isResponseReceived = harEntries.stream().anyMatch(
                        r -> r.getRequest().getUrl().contains(requestPartialUrl)
                        && r.getRequest().getMethod().equals(httpMethod.toString())
                        && successHttpStatusesList.contains(r.getResponse().getStatus())
                        && (httpMethod.equals(HttpMethod.DELETE)? true : (r.getResponse().getContent().getText() != null && !r.getResponse().getContent().getText().isEmpty()))
                );
                allHarEntries.clear();
                allHarEntries.addAll(harEntries);
                return isResponseReceived;
            });
        }
        catch (TimeoutException exception){
            String allUrlsString = getSimilarRequestsString(requestPartialUrl, allHarEntries);

            throw new AssertionFailedError(String.format("The expected response with request URL '%s' with method %s is not loaded! \r\nSimilar requests: %s", requestPartialUrl, httpMethod, allUrlsString));
        }
    }

    private static String getSimilarRequestsString(String requestPartialUrl, List<HarEntry> allHarEntries) {
        try{
            ArrayList<String> allUrls = new ArrayList<>();
            allHarEntries.stream().forEach(e -> {
                Uri uri = Uri.create(requestPartialUrl);
                if(e.getRequest().getUrl().contains(uri.getHost()) || e.getRequest().getUrl().contains(requestPartialUrl)){
                    allUrls.add(String.format("[%s]%s[%s]", e.getRequest().getMethod(), e.getRequest().getUrl(), e.getResponse().getStatusText()));
                }
            });
            String allUrlsString = "";
            for (String url:
                 allUrls) {
                allUrlsString += url;
                allUrlsString += "\r\n";
            }
            return allUrlsString;
        }
        catch (Exception ex){
            ArrayList<String> allUrls = new ArrayList<>();
            allHarEntries.forEach(e -> allUrls.add(e.getRequest().getUrl()));
            return allUrls.toString();
        }
    }

    public static void assertNoLargeImagesRequested() {
        var harEntries = PROXY_SERVER.get().getHar().getLog().getEntries();
        var areThereLargeImages = harEntries.stream().anyMatch(r
                -> r.getResponse().getContent().getMimeType().startsWith("image")
                && r.getResponse().getContent().getSize() > 40000);

        Assert.assertFalse(areThereLargeImages);
    }

    private static int findFreePort() {
        int port = 0;
        // For ServerSocket port number 0 means that the port number is automatically allocated.
        try (ServerSocket socket = new ServerSocket(0)) {
            // Disable timeout and reuse address after closing the socket.
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
        } catch (IOException ignored) {
        }
        if (port > 0) {
            return port;
        }
        throw new RuntimeException("Could not find a free port");
    }

    public static <T> T getLastRequest(Class<T> requestModelClass) {
        return getCapturedEntries().stream()
                .map(HarEntry::getRequest)
                .filter(request -> request.getPostData() != null)
                .map(request -> new Gson().fromJson(request.getPostData().getText(), requestModelClass))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public static <T> T getLastResponse(Class<T> responseModelClass) {
        return getCapturedEntries().stream()
                .map(HarEntry::getResponse)
                .filter(response -> response.getContent() != null)
                .map(response -> new Gson().fromJson(getDataObject(response.getContent().getText()), responseModelClass))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public static <T> T getRequestByIndex(int index, Class<T> requestModelClass) {
        var entries = getCapturedEntries();
        var harEntry = entries.get(index);
        String json = harEntry.getRequest().getPostData().getText();
        return new Gson().fromJson(json, requestModelClass);
    }

    public static <T> T getResponseByIndex(int index, Class<T> responseModelClass) {
        var harEntry = getCapturedEntries().get(index);
        String json = harEntry.getResponse().getContent().getText();
        return new Gson().fromJson(getDataObject(json), responseModelClass);
    }

    public static <T> T getRequestByUrl(String url, String httpMethod, Class<T> requestModelClass) {
        var harEntries = getCapturedEntries();
        var harEntry = harEntries.stream()
                .filter(r -> r.getRequest().getUrl().contains(url) && r.getRequest().getMethod().equals(httpMethod))
                .findFirst()
                .orElse(null);
        if (harEntry == null) {
            return null;
        }
        String json = harEntry.getRequest().getPostData().getText();
        try {
            return new Gson().fromJson(json, requestModelClass);
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred while converting json to model. Json was: %s".formatted(json), e);
        }
    }

    public static <T> T getRequestByUrl(String url, String httpMethod, Type modelType) {
        var harEntries = getCapturedEntries();
        var harEntry = harEntries.stream()
                .filter(r -> r.getRequest().getUrl().contains(url) && r.getRequest().getMethod().equals(httpMethod))
                .findFirst()
                .orElse(null);
        if (harEntry == null) {
            return null;
        }
        String json = harEntry.getRequest().getPostData().getText();
        try {
            return new Gson().fromJson(json, modelType);
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred while converting json to model. Json was: %s".formatted(json), e);
        }
    }

    public static <T> T getResponseByUrl(String url, String httpMethod, Class<T> responseModelClass) {
        var harEntries = getCapturedEntries();
        var harEntry = harEntries.stream()
                .filter(r -> r.getRequest().getUrl().contains(url) && r.getRequest().getMethod().equals(httpMethod))
                .findFirst()
                .orElse(null);
        if (harEntry == null) {
            System.out.println("There is no match!");
            return null;
        }
        String json = harEntry.getResponse().getContent().getText();
        try {
            return new Gson().fromJson(getDataObject(json), responseModelClass);
        }
        catch (Exception ex){
            throw new AssertionFailedError("Cannot get JSON body from the string: " + json + ". Error was: " + ex.getMessage());
        }
    }

    public static <T> T getResponseByUrl(String url, String httpMethod, Type responseModelType) {
        var harEntries = getCapturedEntries();
        var harEntry = harEntries.stream()
                .filter(r -> r.getRequest().getUrl().contains(url) && r.getRequest().getMethod().equals(httpMethod))
                .findFirst()
                .orElse(null);
        if (harEntry == null) {
            System.out.println("There is no match!");
            return null;
        }
        String json = harEntry.getResponse().getContent().getText();
        return new Gson().fromJson(getDataObject(json), responseModelType);
    }

    public static void blockRequestByUrl(String url, HttpMethod httpMethod) {
        PROXY_SERVER.get().blacklistRequests(url,407, httpMethod.toString());
    }

    public static void blockRequestByUrl(String url) {
        PROXY_SERVER.get().blacklistRequests(url,407);
    }

    public static void clearblockRequestList() {
        PROXY_SERVER.get().clearBlacklist();
    }

    private static String getDataObject(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;
        JsonArray jsonArray = null;
        boolean isObject = false;

        try {
            jsonObject = (JsonObject) parser.parse(jsonString);
            isObject = true;
        }
        catch (NullPointerException nullEx){
            throw new RuntimeException("JSON data is null. " + nullEx.getMessage());
        }
        catch (Exception exception) {
            jsonArray = (JsonArray) parser.parse(jsonString);
        }

        JsonObject dataObject = null;
        JsonArray dataArray = null;
        if (isObject == true) {
            if (jsonObject.has("data")){
                if (jsonObject.get("data").isJsonObject()){
                    dataObject = jsonObject.getAsJsonObject("data");
                } else {
                    dataArray = jsonObject.getAsJsonArray("data");
                }
            } else {
                dataObject = jsonObject.getAsJsonObject();
            }
        } else {
            dataArray = jsonArray.getAsJsonArray();
        }

//        if (jsonObject.has("data")){
//            if (jsonObject.get("data").isJsonObject()){
//                dataObject = jsonObject.getAsJsonObject("data");
//            } else {
//                dataArray = jsonObject.getAsJsonArray("data");
//            }
//        } else {
//            dataArray = jsonArray.getAsJsonArray();
//        }

        if (dataObject == null){
//            return dataArray.toString();
            return jsonString;
        } else {
            return dataObject.toString();
        }
    }
}
