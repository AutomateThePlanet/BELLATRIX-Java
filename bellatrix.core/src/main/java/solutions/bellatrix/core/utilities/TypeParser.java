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

package solutions.bellatrix.core.utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class that contains instructions how to convert from one type to another.
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class TypeParser {
    private static final SpecificInstructions<String, ?> STRING_ALLOWED_OPERATIONS = new SpecificInstructions<>((Map<Class<Object>, Function<String, Object>>) (Map) Map.ofEntries(
            Map.entry(String.class, (Function<String, String>) (x) -> x),
            Map.entry(Integer.class, (Function<String, Integer>) Integer::parseInt),
            Map.entry(Double.class, (Function<String, Double>) Double::parseDouble),
            Map.entry(Float.class, (Function<String, Float>) Float::parseFloat),
            Map.entry(Byte.class, (Function<String, Byte>) Byte::parseByte),
            Map.entry(Long.class, (Function<String, Long>) Long::parseLong),
            Map.entry(Short.class, (Function<String, Short>) Short::parseShort),
            Map.entry(Boolean.class, (Function<String, Boolean>) Boolean::parseBoolean),
            Map.entry(Character.class, (Function<String, Character>) (x) -> x.charAt(0)),
            Map.entry(BigDecimal.class, (Function<String, BigDecimal>) BigDecimal::new),
            Map.entry(BigInteger.class, (Function<String, BigInteger>) BigInteger::new)
    ));

    /**
     * Instructions for converting from one type to another.<br>
     * You can specify it statically here, or you could add new instructions dynamically via {@link Map#put(Object, Object)}
     */
    public static InstructionsCollection<?, ?> ALLOWED_OPERATIONS = new InstructionsCollection<>(Map.ofEntries(
            Map.entry(String.class, STRING_ALLOWED_OPERATIONS)
    ));

    /**
     * Checks for enum fields containing the specified value and prioritizes which enum value to return by the field,
     * instead of the name of the enum value.
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T> T parseEnumValue(String value, Class<T> enumClass) {
        var fields = enumClass.getDeclaredFields();
        for (T enumConstant : enumClass.getEnumConstants()) {
            for (var field : fields) {
                field.setAccessible(true);
                Object fieldValue = field.get(enumConstant);
                if (fieldValue != null && fieldValue.toString().equals(value)) {
                    return enumConstant;
                }
            }
        }

        return (T) Enum.valueOf((Class<Enum>)enumClass, value);
    }

    // TODO: RENAME ME
    public class SpecificInstructions<TFrom, TTo> extends HashMap<Class<TTo>, Function<TFrom, TTo>> {
        public SpecificInstructions(Map<Class<TTo>, Function<TFrom, TTo>> map) {
            super(map);
        }

        public Function<TFrom, TTo> to(Class<?> clazz) {
            return this.get(clazz);
        }
    }

    // TODO: RENAME ME
    public class InstructionsCollection<TFrom, T extends SpecificInstructions> extends HashMap<Class<TFrom>, T> {
        public InstructionsCollection(Map<Class<TFrom>, T> map) {
            super(map);
        }

        public T from(Class<?> clazz) {
            return this.get(clazz);
        }
    }
}
