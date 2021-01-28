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

package solutions.bellatrix.plugins;import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class PluginExecutionEngine {
    private final static List<Plugin> plugins;

    static {
        plugins = new ArrayList<>();
    }

    public static void addPlugin(Plugin plugin) {
        if (!plugins.stream().anyMatch(p -> p.getClass().getName() == plugin.getClass().getName())) {
            plugins.add(plugin);
        }
    }

    public static void removePlugin(Plugin plugin) {
        plugins.remove(plugin);
    }

    public static void preBeforeClass(Class type) {
        for (var currentObserver: plugins) {
            currentObserver.preBeforeClass(type);
        }
    }

    public static void postBeforeClass(Class type) {
        for (var currentObserver: plugins) {
            currentObserver.postBeforeClass(type);
        }
    }

    public static void beforeClassFailed(Exception e) {
        for (var currentObserver: plugins) {
            currentObserver.beforeClassFailed(e);
        }
    }

    public static void preBeforeTest(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.preBeforeTest(result, memberInfo);
        }
    }

    public static void postBeforeTest(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.postBeforeTest(result, memberInfo);
        }
    }

    public static void beforeTestFailed(Exception e) {
        for (var currentObserver: plugins) {
            currentObserver.beforeTestFailed(e);
        }
    }

    public static void preAfterTest(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.preAfterTest(result, memberInfo);
        }
    }

    public static void postAfterTest(ITestResult result, Method memberInfo) {
        for (var currentObserver: plugins) {
            currentObserver.postAfterTest(result, memberInfo);
        }
    }

    public static void afterTestFailed(Exception e) {
        for (var currentObserver: plugins) {
            currentObserver.afterTestFailed(e);
        }
    }

    public static void preAfterClass(Class type) {
        for (var currentObserver: plugins) {
            currentObserver.preAfterClass(type);
        }
    }

    public static void postAfterClass(Class type) {
        for (var currentObserver: plugins) {
            currentObserver.postAfterClass(type);
        }
    }

    public static void afterClassFailed(Exception e) {
        for (var currentObserver: plugins) {
            currentObserver.afterClassFailed(e);
        }
    }

//    public static void testInstantiated(Method memberInfo) {
//        for (var currentObserver: plugins) {
//            currentObserver.testInstantiated(memberInfo);
//        }
//    }
}
