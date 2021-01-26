/*
 * Copyright 2021 Automate The Planet Ltd.
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

import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PluginExecutionEngine {
    private final List<Plugin> plugins;

    public PluginExecutionEngine() {
        plugins = new ArrayList<>();
    }

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public void removePlugin(Plugin plugin) {
        plugins.remove(plugin);
    }

    public void preTestInit(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.preTestInit(result, memberInfo);
        }
    }

    public void postTestInit(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.postTestInit(result, memberInfo);
        }
    }

    public void preTestCleanup(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.preTestCleanup(result, memberInfo);
        }
    }

    public void postTestCleanup(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.postTestCleanup(result, memberInfo);
        }
    }

    public void testInstantiated(Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.testInstantiated(memberInfo);
        }
    }
}
