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

package solutions.bellatrix.core.plugins;

import java.lang.reflect.Method;
import java.util.*;

public final class PluginExecutionEngine {
    private final static LinkedHashSet<Plugin> PLUGINS;

    static {
        PLUGINS = new LinkedHashSet<>();
    }

    public static void addPlugin(Plugin plugin) {
        PLUGINS.add(plugin);
    }

    public static void removePlugin(Plugin plugin) {
        PLUGINS.remove(plugin);
    }

    public static void preBeforeClass(Class type) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.preBeforeClass(type);
        }
    }

    public static void postBeforeClass(Class type) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.postBeforeClass(type);
        }
    }

    public static void beforeClassFailed(Exception e) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.beforeClassFailed(e);
        }
    }

    public static void preBeforeTest(TestResult result, Method memberInfo) throws Exception {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.preBeforeTest(result, memberInfo);
        }
    }

    public static void postBeforeTest(TestResult result, Method memberInfo) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.postBeforeTest(result, memberInfo);
        }
    }

    public static void beforeTestFailed(Exception e) throws Exception {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.beforeTestFailed(e);
        }
    }

    public static void preAfterTest(TestResult result, Method memberInfo) throws Exception {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.preAfterTest(result, memberInfo);
        }
    }

    public static void postAfterTest(TestResult result, Method memberInfo) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.postAfterTest(result, memberInfo);
        }
    }

    public static void afterTestFailed(Exception e) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.afterTestFailed(e);
        }
    }

    public static void preAfterClass(Class type) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.preAfterClass(type);
        }
    }

    public static void postAfterClass(Class type) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.postAfterClass(type);
        }
    }

    public static void afterClassFailed(Exception e) {
        for (var currentObserver : PLUGINS) {
            if (currentObserver != null)
                currentObserver.afterClassFailed(e);
        }
    }
}
