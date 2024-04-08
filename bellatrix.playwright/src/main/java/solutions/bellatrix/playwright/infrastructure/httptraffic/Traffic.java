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

package solutions.bellatrix.playwright.infrastructure.httptraffic;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Container for all Requests and Responses captured during testing.
 */
@UtilityClass
public class Traffic {
    private static final ThreadLocal<List<Requests>> REQUESTS;
    private static final ThreadLocal<List<Responses>> RESPONSES;

    static {
        REQUESTS = new ThreadLocal<>();
        REQUESTS.set(new ArrayList<>());
        RESPONSES = new ThreadLocal<>();
        RESPONSES.set(new ArrayList<>());
    }

    public List<Requests> getRequestContainers() {
        return REQUESTS.get();
    }

    public List<Responses> getResponseContainers() {
        return RESPONSES.get();
    }

    public List<Request> getContextSpecificRequests(BrowserContext context) {
        return Objects.requireNonNull(REQUESTS.get().stream().filter(x -> x.getContext().equals(context)).findFirst().orElse(null)).getRequests();
    }

    public List<Response> getContextSpecificResponses(BrowserContext context) {
        return Objects.requireNonNull(RESPONSES.get().stream().filter(x -> x.getContext().equals(context)).findFirst().orElse(null)).getResponses();
    }
}
