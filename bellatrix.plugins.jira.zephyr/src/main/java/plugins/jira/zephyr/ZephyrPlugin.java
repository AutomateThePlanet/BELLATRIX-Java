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
import plugins.jira.zephyr.annotations.ZephyrCycleId;
import plugins.jira.zephyr.annotations.ZephyrProjectId;
import plugins.jira.zephyr.annotations.ZephyrTestCase;
import plugins.jira.zephyr.data.ZephyrTestCycle;
import plugins.jira.zephyr.config.ZephyrSettings;
import plugins.jira.zephyr.data.ZephyrTestCycleStatus;
import plugins.jira.zephyr.data.ZephyrTestExecutionStatus;
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
import java.util.Map;

public class ZephyrPlugin extends Plugin {
    public static final EventListener<ZephyrCyclePluginEventArgs> ZEPHYR_CYCLE_CREATED = new EventListener<>();
    public static final EventListener<ZephyrExecutionPluginEventArgs> ZEPHYR_TEST_CASE_EXECUTION_FAILED = new EventListener<>();
    public static final EventListener<ZephyrExecutionPluginEventArgs> ZEPHYR_TEST_CASE_EXECUTED = new EventListener<>();
    public static final EventListener<ZephyrCyclePluginEventArgs> ZEPHYR_CYCLE_STATUS_UPDATE_FAILED = new EventListener<>();

    private ZephyrTestCycle testCycle;
    
    public ZephyrPlugin() {
        String authToken = settings().getToken();

        RestAssured.baseURI = settings().getApiUrl();
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + authToken)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    private ZephyrSettings settings() {
        return ConfigurationService.get(ZephyrSettings.class);
    }

    private boolean isEnabled() {
        return settings().isEnabled();
    }

    private String getCycleId(Method memberInfo) {
        if (!settings().isExistingCycle()) return testCycle.getKey();

        if (memberInfo.isAnnotationPresent(ZephyrTestCase.class) && !memberInfo.getAnnotation(ZephyrTestCase.class).cycleId().isBlank()) {
            return memberInfo.getAnnotation(ZephyrTestCase.class).cycleId();
        } else if (memberInfo.getDeclaringClass().isAnnotationPresent(ZephyrTestCase.class)) {
            return memberInfo.getDeclaringClass().getAnnotation(ZephyrCycleId.class).value();
        } else {
            return "";
        }
    }

    private String getExecutionId(Method memberInfo) {
        if (memberInfo.isAnnotationPresent(ZephyrTestCase.class)) {
            return memberInfo.getAnnotation(ZephyrTestCase.class).id();
        } else {
            return "";
        }
    }

    private String getProjectId(Method memberInfo) {
        if (memberInfo.isAnnotationPresent(ZephyrTestCase.class) && !memberInfo.getAnnotation(ZephyrTestCase.class).projectId().isBlank()) {
            return memberInfo.getAnnotation(ZephyrTestCase.class).projectId();
        } else if (memberInfo.getDeclaringClass().isAnnotationPresent(ZephyrProjectId.class)) {
            return memberInfo.getDeclaringClass().getAnnotation(ZephyrProjectId.class).value();
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
            return ZephyrTestExecutionStatus.PASS.getValue();
        } else {
            return ZephyrTestExecutionStatus.FAIL.getValue();
        }
    }

    @Override
    public void postBeforeClass(Class type) {
        if (!isEnabled() || settings().isExistingCycle()) return;

        var isDefaultValueAvailableInConfig = settings().getTestCycleName() != null && !settings().getTestCycleName().isBlank();

        var testCycleName = String.format("%s %s",
                DateTimeUtilities.getUtcNow(), isDefaultValueAvailableInConfig ? settings().getTestCycleName() : "BELLATRIX RUN");

        testCycle = new ZephyrTestCycle(settings().getDefaultProjectKey(), testCycleName, ZephyrTestCycleStatus.IN_PROGRESS.getValue());
        testCycle.setPlannedStartDate(DateTimeUtilities.getUtcNow());

        ZephyrApiService.createTestCycle(testCycle);

        ZEPHYR_CYCLE_CREATED.broadcast(new ZephyrCyclePluginEventArgs(testCycle));
    }

    @Override
    public void postAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo, Throwable failedTestException) {
        if (!isEnabled()) return;

        var testCase = new plugins.jira.zephyr.data.ZephyrTestCase(getProjectId(memberInfo), getCycleId(memberInfo), getExecutionId(memberInfo), getStatus(testResult), timeRecord.getDuration(), failedTestException);

        if (testCase.testCaseId().isEmpty() || testCase.status().isEmpty() || getProjectId(memberInfo).isEmpty()) {
            ZEPHYR_TEST_CASE_EXECUTION_FAILED.broadcast(new ZephyrExecutionPluginEventArgs(testCase));
            return;
        }

        var response = ZephyrApiService.executeTestCase(testCase);

       var responseBody = response.body().prettyPrint();

        if (response.statusCode() >= 400) {
            ZEPHYR_TEST_CASE_EXECUTION_FAILED.broadcast(new ZephyrExecutionPluginEventArgs(testCase));
        }

        ZEPHYR_TEST_CASE_EXECUTED.broadcast(new ZephyrExecutionPluginEventArgs(testCase));
    }

    @Override
    public void preAfterClass(Class type) {
        if (!isEnabled() || settings().isExistingCycle()) return;

        testCycle.setPlannedEndDate(DateTimeUtilities.getUtcNow());

        var response = ZephyrApiService.changeTestCycleStatus(testCycle);

        if (response.statusCode() >= 400) {
            ZEPHYR_CYCLE_STATUS_UPDATE_FAILED.broadcast(new ZephyrCyclePluginEventArgs());
        }
    }
}
