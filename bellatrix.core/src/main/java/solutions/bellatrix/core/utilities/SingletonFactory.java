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

package solutions.bellatrix.core.utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

// Based on http://neutrofoton.github.io/blog/2013/08/29/generic-singleton-pattern-in-java/
// Can be used inside App design pattern.
@SuppressWarnings("unchecked")
public class SingletonFactory {
    private static final SingletonFactory SINGLETON_FACTORY = new SingletonFactory();

    private final Map<Class<?>, Object> mapHolder = new HashMap<>();

    private SingletonFactory() {
    }

    @SneakyThrows
    public static <T> T getInstance(Class<T> classOf, Object... initargs) {
        try {
            if (!SINGLETON_FACTORY.mapHolder.containsKey(classOf)) {
                T obj = (T)classOf.getConstructors()[0].newInstance(initargs);
                SINGLETON_FACTORY.mapHolder.put(classOf, obj);
            }
            return (T)SINGLETON_FACTORY.mapHolder.get(classOf);
        } catch (Exception e) {
            Log.error("Failed to create instance of the object. Exception was: " + e);
            return null;
        }
    }

    public static <T> void register(T instance) {
        SINGLETON_FACTORY.mapHolder.put(instance.getClass(), instance);
    }

    public static <T> void register(Class<?> classKey, T instance) {
        SINGLETON_FACTORY.mapHolder.put(classKey, instance);
    }
}
