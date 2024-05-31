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
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import plugins.jira.zephyr.ZephyrPlugin;
import plugins.jira.zephyr.config.ZephyrSettings;
import plugins.jira.zephyr.data.ZephyrTestCycleResponse;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class ZephyrApiService {
    private ZephyrSettings settings() {
        return ConfigurationService.get(ZephyrSettings.class);
    }

    public ZephyrTestCycleResponse createTestCycle(ZephyrPlugin.ZephyrLocalData data) {
        var body = Map.of(
                "projectKey", data.getTestCycle().getProjectKey(),
                "name", data.getTestCycle().getName(),
                "statusName", data.getTestCycle().getStatusName()
        );

        return given()
                .body(body, ObjectMapperType.GSON)
                .when()
                .post("/testcycles")
                .then()
                .extract().response().body().as(ZephyrTestCycleResponse.class, ObjectMapperType.GSON);
    }

    public Response executeTestCase(plugins.jira.zephyr.data.ZephyrTestCase testCase) {
        var body = Map.of(
                "projectKey", testCase.projectId(),
                "testCycleKey", testCase.testCycleId(),
                "testCaseKey", testCase.testCaseId(),
                "statusName", testCase.status(),
                "executionTime", (int)testCase.duration()
        );

        return given()
                .body(body, ObjectMapperType.GSON)
                .when()
                .post("/testexecutions")
                .then()
                .extract().response();
    }

    public Response markTestCycleDone(ZephyrPlugin.ZephyrLocalData data) {
        var body = Map.of(
                "id", data.getTestCycleResponse().getId(),
                "key", data.getTestCycleResponse().getKey(),
                "name", data.getTestCycle().getName(),
                "project", Map.of("id", ZephyrApiService.getProjectId(data.getTestCycleResponse().getKey())),
                "status", Map.of("id", ZephyrApiService.getStatusId(settings().getCycleFinalStatus().isBlank() ? settings().getCycleFinalStatus() : "Done", data.getTestCycle().getProjectKey())),
                "plannedEndDate", data.getTestCycle().getPlannedEndDate()
        );

        return given()
                .body(body, ObjectMapperType.GSON)
                .put("/testcycles/" + settings().getDefaultProjectKey())
                .then()
                .extract().response();
    }

    private static String getProjectId(String testCycleIdOrKey) {
        return given()
                .get("/testcycles/" + testCycleIdOrKey)
                .then()
                .extract().as(TestCycle.class, ObjectMapperType.GSON).project.id;
    }

    private static String getStatusId(String statusName, String projectKey) {
        try {
            return getStatuses(projectKey).stream()
                    .filter(status -> status.name.equalsIgnoreCase(statusName))
                    .map(status -> status.id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Could not find a status in project %s with the provided status name: %s", projectKey, statusName)
                    ));
        } catch (IllegalArgumentException ex) {
            DebugInformation.debugInfo(ex.getMessage());
            return "";
        }
    }

    private static List<Status> getStatuses(String projectKey) {
        return given()
                .queryParam("maxResults", "100")
                .queryParam("projectKey", projectKey)
                .queryParam("statusType", "TEST_CYCLE")
                .get("/statuses")
                .then()
                .extract().as(Statuses.class, ObjectMapperType.GSON).values;
    }

    private static final class Status {
        private String id;
        private String name;
    }

    private static final class Statuses {
        private ArrayList<Status> values;
    }

    private static final class Project {
        private String id;
    }

    private static final class TestCycle {
        private Project project;
    }
}
