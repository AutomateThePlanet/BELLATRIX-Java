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

package solutions.bellatrix.api.infrastructure.junit;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.api.configuration.ApiSettings;
import solutions.bellatrix.api.services.App;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.junit.BaseTest;

public class ApiTest extends BaseTest {
    protected RequestSpecification requestSpecification;

    public App app() {
        return new App();
    }

    @Override
    protected void beforeEach() throws Exception {
        var logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        var config = RestAssuredConfig.config().logConfig(logConfig);

        // Anton(31.05.22): Probably we don't need to set here base URI and Path since we will have different ones for different APIs
        var requestSpecBuilder = new RequestSpecBuilder()
//                .setBaseUri(getBaseUri())
//                .setBasePath(getBasePath())
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .setConfig(config);

        initializeDefaultHeaders(requestSpecBuilder);
        requestSpecification = requestSpecBuilder.build();
    }

    private void initializeDefaultHeaders(RequestSpecBuilder requestSpecBuilder) {
        var settings = ConfigurationService.get(ApiSettings.class);
        for (var entry : settings.getHeaders()) {
            for (var c : entry.entrySet()) {
                requestSpecBuilder.addHeader(c.getKey(), c.getValue());
            }
        }
    }

    @Override
    protected void afterEach() {
        RestAssured.reset();
    }

    protected RequestSpecification givenRequest() {
        return RestAssured.given().spec(requestSpecification).log().all();
    }

    protected String getBaseUri() {
        return ConfigurationService.get(ApiSettings.class).getBaseUri();
    }

    protected String getBasePath() {
        return ConfigurationService.get(ApiSettings.class).getBasePath();
    }
}