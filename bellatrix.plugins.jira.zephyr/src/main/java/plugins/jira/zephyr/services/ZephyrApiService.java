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
package plugins.jira.zephyr.services;

import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import net.minidev.json.JSONObject;
import org.apache.commons.text.StringEscapeUtils;
import plugins.jira.zephyr.config.ZephyrSettings;
import plugins.jira.zephyr.data.ZephyrTestCycle;
import plugins.jira.zephyr.data.ZephyrTestCycleResponse;
import plugins.jira.zephyr.data.ZephyrTestCycleStatus;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class ZephyrApiService {
    private ZephyrSettings settings() {
        return ConfigurationService.get(ZephyrSettings.class);
    }

    public ZephyrTestCycleResponse createTestCycle(ZephyrTestCycle testCycle) {
        var body = Map.of(
                "projectKey", testCycle.getProjectKey(),
                "name", testCycle.getName(),
                "statusName", testCycle.getStatusName()
        );

        return given()
                .body(body, ObjectMapperType.GSON)
                .when()
                .post("/testcycles")
                .then()
                .extract().response().body().as(ZephyrTestCycleResponse.class, ObjectMapperType.GSON);
    }

    public Response executeTestCase(plugins.jira.zephyr.data.ZephyrTestCase testCase) {
        var body = new java.util.HashMap<String, java.io.Serializable>(Map.of(
                "projectKey", testCase.projectId(),
                "testCycleKey", testCase.testCycleId(),
                "testCaseKey", testCase.testCaseId(),
                "statusName", testCase.status(),
                "executionTime", (int)testCase.duration()
        ));

        if (testCase.error() != null) {
            body.put("comment", formatError(testCase.error()));
        }

        return given()
                .body(body, ObjectMapperType.GSON)
                .when()
                .post("/testexecutions")
                .then()
                .extract().response();
    }

    public Response changeTestCycleStatus(ZephyrTestCycle cycleData, ZephyrTestCycleResponse cycleCreationResponse) {
        var isDefaultValueAvailableInConfig = settings().getCycleFinalStatus() != null && !settings().getCycleFinalStatus().isBlank();

        var body = Map.of(
                "id", cycleCreationResponse.getId(),
                "key", cycleCreationResponse.getKey(),
                "name", cycleData.getName(),
                "project", Map.of("id", ZephyrApiService.getProjectId(cycleCreationResponse.getKey())),
                "status", Map.of("id", ZephyrApiService.getStatusId(isDefaultValueAvailableInConfig ?
                        settings().getCycleFinalStatus() : ZephyrTestCycleStatus.DONE.getValue(), cycleData.getProjectKey())),
                "plannedEndDate", cycleData.getPlannedEndDate()
        );

        return given()
                .body(body, ObjectMapperType.GSON)
                .put("/testcycles/" + settings().getDefaultProjectKey())
                .then()
                .extract().response();
    }

    private static String getProjectId(String testCycleIdOrKey) {
        return String.valueOf(given()
                .get("/testcycles/" + testCycleIdOrKey)
                .then()
                .extract().jsonPath().<Integer>get("project.id"));
    }

    private static String getStatusId(String statusName, String projectKey) {
        return getStatuses(projectKey).stream()
                .filter(status -> String.valueOf(status.get("name")).equalsIgnoreCase(statusName))
                .map(status -> String.valueOf(status.get("id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Could not find a status in project %s with the provided status name: %s", projectKey, statusName)
                ));
    }

    private static List<Map<String, Object>> getStatuses(String projectKey) {
        JsonPath jsonPath = given()
                .queryParam("maxResults", "100")
                .queryParam("projectKey", projectKey)
                .queryParam("statusType", "TEST_CYCLE")
                .get("/statuses")
                .then()
                .extract()
                .jsonPath();

        return jsonPath.getList("values");
    }

    private static String formatError(Throwable error) {


        var errorInfo = String.format("<strong>Failure details:</strong>\n\nError message:\n%s\n\nStack Trace:<pre>%s</pre>",
                StringEscapeUtils.escapeHtml4(error.getMessage()),
                String.join("\n", Arrays.stream(error.getStackTrace()).map(StackTraceElement::toString).toList())
        );

        return errorInfo.replace("\n", "<br>");
    }

}
