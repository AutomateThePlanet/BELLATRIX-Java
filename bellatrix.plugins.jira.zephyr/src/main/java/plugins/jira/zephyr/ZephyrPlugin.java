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
package plugins.jira.zephyr;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import lombok.Getter;
import lombok.Setter;
import plugins.jira.zephyr.annotations.ZephyrProject;
import plugins.jira.zephyr.annotations.ZephyrTestCase;
import plugins.jira.zephyr.data.ZephyrTestCycle;
import plugins.jira.zephyr.data.ZephyrTestCycleResponse;
import plugins.jira.zephyr.config.ZephyrSettings;
import plugins.jira.zephyr.events.ZephyrCyclePluginEventArgs;
import plugins.jira.zephyr.events.ZephyrExecutionPluginEventArgs;
import plugins.jira.zephyr.services.ZephyrApiService;
import plugins.jira.zephyr.utilities.DateTimeUtilities;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.plugins.TimeRecord;

import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

public class ZephyrPlugin extends Plugin {
    public static final EventListener<ZephyrCyclePluginEventArgs> ZEPHYR_CYCLE_CREATED = new EventListener<>();
    public static final EventListener<ZephyrExecutionPluginEventArgs> ZEPHYR_TEST_CASE_EXECUTION_FAILED = new EventListener<>();
    public static final EventListener<ZephyrExecutionPluginEventArgs> ZEPHYR_TEST_CASE_EXECUTED = new EventListener<>();
    public static final EventListener<ZephyrCyclePluginEventArgs> ZEPHYR_CYCLE_STATUS_UPDATE_FAILED = new EventListener<>();

    private ZephyrLocalData data;

    public ZephyrPlugin() {
        String authToken = settings().getToken();

        RestAssured.baseURI = settings().getApiUrl();
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + authToken)
                .addHeader("Content-Type", "application/json")
                .build();

        data = new ZephyrLocalData();
    }

    private ZephyrSettings settings() {
        return ConfigurationService.get(ZephyrSettings.class);
    }

    private boolean isEnabled() {
        return settings().isEnabled();
    }

    private String getExecutionId(Method memberInfo) {
        if (memberInfo.isAnnotationPresent(ZephyrTestCase.class)) {
            return memberInfo.getAnnotation(ZephyrTestCase.class).id();
        } else {
            return "";
        }
    }

    private String getProjectId(Method memberInfo) {
        if (memberInfo.getDeclaringClass().isAnnotationPresent(ZephyrProject.class)) {
            return memberInfo.getDeclaringClass().getAnnotation(ZephyrProject.class).id();
        } else {
            try {
                return settings().getDefaultProjectKey();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    private String getStatus(TestResult testResult) {
        if (testResult.equals(TestResult.SUCCESS)) {
            return "Pass";
        } else {
            return "Fail";
        }
    }

    @Override
    public void postBeforeClass(Class type) {
        if (!isEnabled()) return;

        var testCycleName = String.format("%s %s",
                DateTimeUtilities.getUtcNow(),
                settings().getTestCycleName().isBlank() ? settings().getTestCycleName() : "BELLATRIX RUN");

        var testCycle = new ZephyrTestCycle(settings().getDefaultProjectKey(), testCycleName, "In Progress");
        testCycle.setPlannedStartDate(DateTimeUtilities.getUtcNow());
        data.setTestCycle(testCycle);

        var testCycleResponse = ZephyrApiService.createTestCycle(data);
        data.setTestCycleResponse(testCycleResponse);

        ZEPHYR_CYCLE_CREATED.broadcast(new ZephyrCyclePluginEventArgs(testCycleResponse));
    }

    @Override
    public void postAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo, Throwable failedTestException) {
        if (!isEnabled()) return;

        var testCase = new plugins.jira.zephyr.data.ZephyrTestCase(getProjectId(memberInfo), data.getTestCycleResponse().getKey(), getExecutionId(memberInfo), getStatus(testResult), timeRecord.getDuration());

        if (testCase.testCaseId().isEmpty() || testCase.status().isEmpty() || getProjectId(memberInfo).isEmpty()) {
            ZEPHYR_TEST_CASE_EXECUTION_FAILED.broadcast(new ZephyrExecutionPluginEventArgs(testCase));
            return;
        }

        var response = ZephyrApiService.executeTestCase(testCase);

        if (response.statusCode() >= 400) {
            ZEPHYR_TEST_CASE_EXECUTION_FAILED.broadcast(new ZephyrExecutionPluginEventArgs(testCase));
        }

        ZEPHYR_TEST_CASE_EXECUTED.broadcast(new ZephyrExecutionPluginEventArgs(testCase));
    }

    @Override
    public void preAfterClass(Class type) {
        if (!isEnabled()) return;

        data.getTestCycle().setPlannedEndDate(DateTimeUtilities.getUtcNow());

        var response = ZephyrApiService.markTestCycleDone(data);

        if (response.statusCode() >= 400) {
            ZEPHYR_CYCLE_STATUS_UPDATE_FAILED.broadcast(new ZephyrCyclePluginEventArgs());
        }
    }

    @Getter @Setter
    public static class ZephyrLocalData {
        private ZephyrTestCycleResponse testCycleResponse;
        private ZephyrTestCycle testCycle;
    }
}
