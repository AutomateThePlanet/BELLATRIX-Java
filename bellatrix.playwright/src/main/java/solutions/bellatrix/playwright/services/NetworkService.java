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

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.WebError;
import org.junit.jupiter.api.Assertions;
import org.testng.Assert;
import solutions.bellatrix.playwright.infrastructure.httptraffic.Requests;
import solutions.bellatrix.playwright.infrastructure.httptraffic.Responses;
import solutions.bellatrix.playwright.infrastructure.httptraffic.Traffic;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class NetworkService extends WebService {
    private static BrowserContext context() {
        return wrappedBrowser().getCurrentContext();
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
        var responses = Traffic.getContextSpecificResponses(context());
        var errorCodesPresent = responses.stream().anyMatch(response ->
                response.status() > 400 && response.status() < 599);

        Assert.assertFalse(errorCodesPresent);
    }

    public static void assertRequestMade(String url) {
        var requests = Traffic.getContextSpecificRequests(context());
        var areRequestsMade = requests.stream().anyMatch(request -> request.url().contains(url));

        Assert.assertTrue(areRequestsMade);
    }

    public static void assertNoLargeImagesRequested() {
        Consumer<Response> handler = (Response response) -> {
            var body = new String(response.body());

            Assertions.assertFalse(body.contains("image") && response.body().length > 40000);
        };
        addResponseHandler(handler);
    }

    public static Request getLastRequest() {
        var requests = Traffic.getContextSpecificRequests(context());
        return requests.get(requests.size() - 1);
    }

    public static Response getLastResponse() {
        var responses = Traffic.getContextSpecificResponses(context());
        return responses.get(responses.size() - 1);
    }

    public static Request getRequestByIndex(int index) {
        return Traffic.getContextSpecificRequests(context()).get(index);
    }

    public static Response getResponseByIndex(int index) {
        return Traffic.getContextSpecificResponses(context()).get(index);
    }

    public static Request getRequestByUrl(String url, String httpMethod) {
        return Traffic.getContextSpecificRequests(context()).stream()
                .filter(request -> request.url().equals(url) && request.method().equalsIgnoreCase(httpMethod))
                .findFirst()
                .orElse(null);
    }

    public static Response getResponseByUrl(String url, String httpMethod) {
        return Traffic.getContextSpecificResponses(context()).stream()
                .filter(response -> response.request().url().equals(url) && response.request().method().equalsIgnoreCase(httpMethod))
                .findFirst()
                .orElse(null);
    }

    /**
     * Use {@link Traffic}, it provides the same functionality.
     */
    @Deprecated
    public static void newHar(String name) {
        // Deprecated
    }

    /**
     * Use {@link Traffic}, it provides the same functionality.
     */
    @Deprecated
    public static void newHar() {
        // Deprecated
    }

    public List<Requests> getRequestContainers() {
        return Traffic.getRequestContainers();
    }

    public List<Responses> getResponseContainers() {
        return Traffic.getResponseContainers();
    }

    public List<Request> getContextSpecificRequests(BrowserContext context) {
        return Traffic.getContextSpecificRequests(context);
    }

    public List<Response> getContextSpecificResponses(BrowserContext context) {
        return Traffic.getContextSpecificResponses(context);
    }

    // ToDo route() and unroute() (from playwright)
    // ToDo setExtraHTTPHeaders() (from playwright)
}
