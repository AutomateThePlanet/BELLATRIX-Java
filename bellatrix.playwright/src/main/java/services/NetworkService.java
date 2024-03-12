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
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.WebError;
import infrastructure.httptraffic.Traffic;
import org.testng.Assert;

import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class NetworkService extends WebService {
    private static BrowserContext context() {
        return wrappedBrowser().currentContext();
    }

    public static void setOffline(boolean offline) {
        context().setOffline(offline);
    }

    public static void addResponseHandler(Consumer<Response> handler) {
        context().onResponse(handler);
    }

    public static void addRequestHandler(Consumer<Request> handler) {
        context().onRequest(handler);
    }

    public static void removeResponseHandler(Consumer<Response> handler) {
        context().offResponse(handler);
    }

    public static void removeRequestHandler(Consumer<Request> handler) {
        context().offRequest(handler);
    }

    public static void addRequestFailureHandler(Consumer<Request> handler) {
        context().onRequestFailed(handler);
    }

    public static void removeRequestFailureHandler(Consumer<Request> handler) {
        context().offRequestFailed(handler);
    }

    public static void addRequestCompletionHandler(Consumer<Request> handler) {
        context().onRequestFinished(handler);
    }

    public static void removeRequestCompletionHandler(Consumer<Request> handler) {
        context().offRequestFinished(handler);
    }

    public static void addWebErrorHandler(Consumer<WebError> handler) {
        context().onWebError(handler);
    }

    public static void removeWebErrorHandler(Consumer<WebError> handler) {
        context().offWebError(handler);
    }

    public static void assertNoErrorCodes() {
        var responses = Traffic.contextSpecificResponses(context());
        var errorCodesPresent = responses.stream().anyMatch(response ->
                response.status() > 400 && response.status() < 599);

        Assert.assertFalse(errorCodesPresent);
    }

    public static void assertRequestMade(String url) {
        var requests = Traffic.contextSpecificRequests(context());
        var areRequestsMade = requests.stream().anyMatch(request -> request.url().contains(url));

        Assert.assertTrue(areRequestsMade);
    }

    public static Request getLastRequest() {
        var requests = Traffic.contextSpecificRequests(context());
        return requests.get(requests.size() - 1);
    }

    public static Response getLastResponse() {
        var responses = Traffic.contextSpecificResponses(context());
        return responses.get(responses.size() - 1);
    }

    public static Request getRequestByIndex(int index) {
        return Traffic.contextSpecificRequests(context()).get(index);
    }

    public static Response getResponseByIndex(int index) {
        return Traffic.contextSpecificResponses(context()).get(index);
    }

    public static Request getRequestByUrl(String url, String httpMethod) {
        return Traffic.contextSpecificRequests(context()).stream()
                .filter(request -> request.url().equals(url) && request.method().equalsIgnoreCase(httpMethod))
                .findFirst()
                .orElse(null);
    }

    public static Response getResponseByUrl(String url, String httpMethod) {
        return Traffic.contextSpecificResponses(context()).stream()
                .filter(response -> response.request().url().equals(url) && response.request().method().equalsIgnoreCase(httpMethod))
                .findFirst()
                .orElse(null);
    }

    // ToDo public static void newHar(String name);
    // ToDo public static void newHar();
    // ToDo public static List<HarEntry> getCapturedEntries();
    // ToDo public static void assertNoLargeImagesRequested();
    // ToDo private static int findFreePort();

    // ToDo route() and unroute() (from playwright)
    // ToDo setExtraHTTPHeaders() (from playwright)
}
