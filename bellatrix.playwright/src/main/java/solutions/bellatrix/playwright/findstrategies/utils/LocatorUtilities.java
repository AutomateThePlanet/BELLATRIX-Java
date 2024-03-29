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

package solutions.bellatrix.playwright.findstrategies.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LocatorUtilities {

    public static <T> Method getGetMethod(Class<T> type, By by, Object... args) {
        List<Class<?>> parameterTypesList = new ArrayList<>();

        for (Object arg : args) {
            if (arg != null) {
                parameterTypesList.add(arg.getClass());
            }
        }

        Class<?>[] parameterTypes = new Class[parameterTypesList.size()];
        for (int i = 0 ; i < parameterTypesList.size(); i++) {
            parameterTypes[i] = parameterTypesList.get(i);
        }

        try {
            switch (by) {
                case ALT_TEXT -> {
                    return type.getDeclaredMethod("getByAltText", parameterTypes);
                }
                case LABEL -> {
                    return type.getDeclaredMethod("getByLabel", parameterTypes);
                }
                case PLACEHOLDER -> {
                    return type.getDeclaredMethod("getByPlaceholder", parameterTypes);
                }
                case ROLE -> {
                    return type.getDeclaredMethod("getByRole", parameterTypes);
                }
                case TEST_ID -> {
                    return type.getDeclaredMethod("getByTestId", parameterTypes);
                }
                case TEXT -> {
                    return type.getDeclaredMethod("getByText", parameterTypes);
                }
                case TITLE -> {
                    return type.getDeclaredMethod("getByTitle", parameterTypes);
                }
                case LOCATOR -> {
                    return type.getDeclaredMethod("locator", parameterTypes);
                }
                default -> throw new IllegalStateException("Unexpected value: " + by);
            }
        } catch (NoSuchMethodException|SecurityException|IllegalStateException ex) {
            throw new RuntimeException(ex);
        }
    }
}
