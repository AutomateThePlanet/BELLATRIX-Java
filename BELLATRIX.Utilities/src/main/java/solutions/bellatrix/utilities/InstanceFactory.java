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

package solutions.bellatrix.utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;

@UtilityClass
public final class InstanceFactory {
    @SneakyThrows
    public static <T> T create(Class<T> classOf) {
        T obj = (T)classOf.getConstructors()[0].newInstance();
        return obj;
    }

    @SneakyThrows
    public static <T> T create(Class<T> classOf, Object... args) {
        T obj = (T)classOf.getConstructors()[0].newInstance(args);
        return obj;
    }

    public static <T> T createByTypeParameter(Class parameterClass, int index) {
        try {
            var elementsClass = (Class)((ParameterizedType)parameterClass.getGenericSuperclass()).getActualTypeArguments()[index];
            return (T)elementsClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
