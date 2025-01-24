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

package solutions.bellatrix.android.services;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.openqa.selenium.ContextAware;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.android.infrastructure.DriverService;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.RuntimeInformation;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class AppService extends MobileService {
    public String getCurrentActivity() {
        return getWrappedAndroidDriver().currentActivity();
    }

    public void startActivity(
            String appPackage,
            String appActivity,
            boolean shouldWait,
            boolean stopApp
    ) {
        try {
            getWrappedAndroidDriver().hideKeyboard();
        } catch (Exception ignore) {}
        var activity = ImmutableMap.of(
                "package", appPackage,
                "component", appActivity,
                "wait", shouldWait,
                "stop", stopApp
        );

        getWrappedAndroidDriver().execute("mobile: startActivity", activity);
    }

    public void startActivity(
            String appPackage,
            String appActivity
    ) {
        var activity = ImmutableMap.of(
                "package", appPackage,
                "component", appActivity
        );

        getWrappedAndroidDriver().execute("mobile: startActivity", activity);
    }

    public void startActivityWithIntent(
            String appPackage,
            String appActivity,
            String intentAction,
            boolean shouldWait,
            String intentCategory,
            String intentFlags,
            List<List<String>> intentOptionalArgs,
            boolean stopApp
    ) {
        try {
            getWrappedAndroidDriver().hideKeyboard();
        } catch (Exception ignore) {}

        var activity = ImmutableMap.of(
                "package", appPackage,
                "component", appActivity,
                "action", intentAction,
                "wait", shouldWait,
                "categories", intentCategory,
                "flags", intentFlags,
                "extras", intentOptionalArgs,
                "stop", stopApp
        );

        getWrappedAndroidDriver().execute("mobile: startActivity", activity);
    }

    public String getContext() {
        return getWrappedAndroidDriver().getContext();
    }

    public void setContext(String name) {
        getWrappedAndroidDriver().context(name);
    }

    public void backgroundApp(int seconds) {
        getWrappedAndroidDriver().runAppInBackground(Duration.ofSeconds(seconds));
    }

    public void terminateApp(String appId) {
        getWrappedAndroidDriver().terminateApp(appId);
    }

    public void activateApp(String appId) {
        getWrappedAndroidDriver().activateApp(appId);
    }

    public List<String> getWebViews() {
        var contexts = ((ContextAware)getWrappedAndroidDriver()).getContextHandles();
        return contexts.stream().toList();
    }

    public void switchToDefault() {
        var contexts = ((ContextAware)getWrappedAndroidDriver()).getContextHandles();
        var firstContext = contexts.stream().findFirst().get();
        ((ContextAware)getWrappedAndroidDriver()).context(firstContext);
    }

    public void switchToWebView() {
        var contexts = ((ContextAware)getWrappedAndroidDriver()).getContextHandles();
        long count = contexts.stream().count();
        var lastContext = contexts.stream().skip(count - 1).findFirst().get();
        ((ContextAware)getWrappedAndroidDriver()).context(lastContext);
    }

    public void switchToWebView(String name) {
        var contexts = ((ContextAware)getWrappedAndroidDriver()).getContextHandles();
        long count = contexts.stream().count();
        var lastContext = contexts.stream().filter(c -> c.contains(name)).findFirst().get();
        ((ContextAware)getWrappedAndroidDriver()).context(lastContext);
    }

    public void switchToFirstWebView() {
        var contexts = ((ContextAware)getWrappedAndroidDriver()).getContextHandles();
        long count = contexts.stream().count();
        var firstContext = contexts.stream().findFirst().get();
        ((ContextAware)getWrappedAndroidDriver()).context(firstContext);
    }

    @SneakyThrows
    public void switchToWebViewUrlContains(String url) {
        switchToWebView(() -> getWrappedAndroidDriver().getCurrentUrl() != null && getWrappedAndroidDriver().getCurrentUrl().contains(url));
    }

    @SneakyThrows
    public void switchToWebViewTitleContains(String title) {
        switchToWebView(() -> getWrappedAndroidDriver().getTitle() != null && getWrappedAndroidDriver().getTitle().contains(title));
    }

    public void installApp(String appPath) {
        if (RuntimeInformation.IS_MAC) {
            appPath = appPath.replace('\\', '/');
        }

        getWrappedAndroidDriver().installApp(appPath);
    }

    public void removeApp(String appId) {
        getWrappedAndroidDriver().removeApp(appId);
    }

    public boolean isAppInstalled(String bundleId) {
        try {
            return getWrappedAndroidDriver().isAppInstalled(bundleId);
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    private void switchToWebView(BooleanSupplier filterConditionToSwitchWebView) {
        AtomicBoolean switchedToRightWebView = new AtomicBoolean(false);
        var timeoutInterval = ConfigurationService.get(AndroidSettings.class).getTimeoutSettings().getImplicitWaitTimeout();
        var sleepInterval = ConfigurationService.get(AndroidSettings.class).getTimeoutSettings().getSleepInterval();
        var webDriverWait = new WebDriverWait(DriverService.getWrappedAndroidDriver(), Duration.ofSeconds(timeoutInterval), Duration.ofSeconds(sleepInterval));
        webDriverWait.until(d -> {
            var contexts = ((ContextAware)getWrappedAndroidDriver()).getContextHandles();
            contexts.stream().forEach(c -> {
                try {
                    ((ContextAware)getWrappedAndroidDriver()).context(c);
                    if (filterConditionToSwitchWebView.getAsBoolean()) {
                        switchedToRightWebView.set(true);
                    }
                } catch (Exception e) {
                    // ignore
                }
            });

            if (switchedToRightWebView.get()) {
                return true;
            }

            getWrappedAndroidDriver().switchTo().defaultContent();
            return false;
        });
    }
}
