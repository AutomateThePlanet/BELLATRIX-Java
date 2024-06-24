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

package solutions.bellatrix.core.utilities.parsing;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Utility class that contains instructions how to convert from one type to another.
 */
@UtilityClass
@SuppressWarnings({"unchecked", "rawtypes"})
public class TypeParser {
    /**
     * Checks for enum fields containing the specified value and prioritizes which enum value to return by the field,
     * instead of the name of the enum value.
     */
    @SneakyThrows
    public static <T> T parseEnumValue(Object value, Class<T> enumClass) {
        var fields = enumClass.getDeclaredFields();

        for (T enumConstant : enumClass.getEnumConstants()) {
            for (var field : fields) {
                if (field.getClass() == value.getClass()) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(enumConstant);
                    if (fieldValue != null && fieldValue.equals(value)) {
                        return enumConstant;
                    }
                }
            }
        }
        if (value.getClass() == String.class) {
            return (T) Enum.valueOf((Class<Enum>)enumClass, (String)value);
        } else {
            // TODO: EventHandler<> for failing to parse enum.
            return null;
        }
    }

    /**
     * Checks if instructions for parsing the value to the given class exist and uses them. <br>
     * In case instructions aren't provided, you can provide them dynamically in your code through {@link #instruct(Class, Class, Function)}
     */
    public static <T> T parse(Object value, Class<T> clazz) {
        var originalClass = value.getClass();

        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null.");
        }

        Function<Object, T> operation = TypeParser.ALLOWED_OPERATIONS.get(originalClass).get(clazz);

        if (operation != null) {
            return operation.apply(value);
        } else if (clazz.isEnum()) {
            Function<Object, T> enumOperation = x -> TypeParser.parseEnumValue(x, clazz);
            TypeParser.ALLOWED_OPERATIONS.get(String.class).add(clazz, enumOperation);
            return enumOperation.apply(value);
        } else {
            throw new IllegalArgumentException("Unsupported class type: " + clazz.getName()
                    + "\nYou can add a custom type parser in " + TypeParser.class.getName());
        }
    }

    public static <T, V> void instruct(Class<T> fromClass, Class<V> toClass, Function<T, V> instructions) {
        if (ALLOWED_OPERATIONS.containsKey(fromClass)) {
            ALLOWED_OPERATIONS.get(fromClass).add(toClass, instructions);
        } else {
            Objects.requireNonNull(ALLOWED_OPERATIONS.put(fromClass, new ParsingInstructions<>())).add(toClass, instructions);
        }
    }

    public static <TFrom> void instruct(Class<TFrom> fromClass, Map.Entry<Class<?>, Function<?, ?>>... entries) {
        var map = ParsingInstructions.ofEntries(entries);

        if (ALLOWED_OPERATIONS.containsKey(fromClass)) {
            ALLOWED_OPERATIONS.get(fromClass).putAll(map);
        } else {
            Objects.requireNonNull(ALLOWED_OPERATIONS.put(fromClass, new ParsingInstructions<>())).putAll(map);
        }
    }

    private static final ParsingInstructions<String> STRING_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
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
            Map.entry(BigInteger.class, (Function<String, BigInteger>) BigInteger::new),
            Map.entry(LocalDateTime.class, (Function<String, LocalDateTime>)GenericDateTimeParser::parse)
            // TODO: to DateTime
            // TODO: to Date
    );

    private static final ParsingInstructions<Integer> INTEGER_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Integer.class, (Function<Integer, Integer>)(x) -> x),
            Map.entry(String.class, (Function<Integer, String>)String::valueOf),
            Map.entry(Double.class, (Function<Integer, Double>)Integer::doubleValue),
            Map.entry(Float.class, (Function<Integer, Float>)Integer::floatValue),
            Map.entry(Byte.class, (Function<Integer, Byte>)Integer::byteValue),
            Map.entry(Long.class, (Function<Integer, Long>)Integer::longValue),
            Map.entry(Short.class, (Function<Integer, Short>)Integer::shortValue),
            Map.entry(Character.class, (Function<Integer, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<Integer, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Integer, BigInteger>)BigInteger::valueOf)
    );

    private static final ParsingInstructions<Double> DOUBLE_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Double.class, (Function<Double, Double>)(x) -> x),
            Map.entry(String.class, (Function<Double, String>)String::valueOf),
            Map.entry(Integer.class, (Function<Double, Integer>)Double::intValue),
            Map.entry(Float.class, (Function<Double, Float>)Double::floatValue),
            Map.entry(Byte.class, (Function<Double, Byte>)Double::byteValue),
            Map.entry(Long.class, (Function<Double, Long>)Double::longValue),
            Map.entry(Short.class, (Function<Double, Short>)Double::shortValue),
            Map.entry(Character.class, (Function<Double, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<Double, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Double, BigInteger>)(x) -> BigInteger.valueOf(x.intValue()))
    );

    private static final ParsingInstructions<Float> FLOAT_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Float.class, (Function<Float, Float>)(x) -> x),
            Map.entry(String.class, (Function<Float, String>)String::valueOf),
            Map.entry(Integer.class, (Function<Float, Integer>)Float::intValue),
            Map.entry(Double.class, (Function<Float, Double>)Float::doubleValue),
            Map.entry(Byte.class, (Function<Float, Byte>)Float::byteValue),
            Map.entry(Long.class, (Function<Float, Long>)Float::longValue),
            Map.entry(Short.class, (Function<Float, Short>)Float::shortValue),
            Map.entry(Character.class, (Function<Float, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<Float, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Float, BigInteger>)(x) -> BigInteger.valueOf(x.intValue()))
    );

    private static final ParsingInstructions<Byte> BYTE_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Byte.class, (Function<Byte, Byte>)(x) -> x),
            Map.entry(String.class, (Function<Byte, String>)String::valueOf),
            Map.entry(Integer.class, (Function<Byte, Integer>)Byte::intValue),
            Map.entry(Double.class, (Function<Byte, Double>)Byte::doubleValue),
            Map.entry(Float.class, (Function<Byte, Float>)Byte::floatValue),
            Map.entry(Long.class, (Function<Byte, Long>)Byte::longValue),
            Map.entry(Short.class, (Function<Byte, Short>)Byte::shortValue),
            Map.entry(Character.class, (Function<Byte, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<Byte, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Byte, BigInteger>)(x) -> BigInteger.valueOf(x.intValue()))
    );

    private static final ParsingInstructions<Long> LONG_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Long.class, (Function<Long, Long>)(x) -> x),
            Map.entry(String.class, (Function<Long, String>)String::valueOf),
            Map.entry(Integer.class, (Function<Long, Integer>)Long::intValue),
            Map.entry(Double.class, (Function<Long, Double>)Long::doubleValue),
            Map.entry(Float.class, (Function<Long, Float>)Long::floatValue),
            Map.entry(Byte.class, (Function<Long, Byte>)Long::byteValue),
            Map.entry(Short.class, (Function<Long, Short>)Long::shortValue),
            Map.entry(Character.class, (Function<Long, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<Long, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Long, BigInteger>)(x) -> BigInteger.valueOf(x.intValue()))
    );

    private static final ParsingInstructions<Short> SHORT_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Short.class, (Function<Short, Short>)(x) -> x),
            Map.entry(String.class, (Function<Short, String>)String::valueOf),
            Map.entry(Integer.class, (Function<Short, Integer>)Short::intValue),
            Map.entry(Double.class, (Function<Short, Double>)Short::doubleValue),
            Map.entry(Float.class, (Function<Short, Float>)Short::floatValue),
            Map.entry(Byte.class, (Function<Short, Byte>)Short::byteValue),
            Map.entry(Long.class, (Function<Short, Long>)Short::longValue),
            Map.entry(Character.class, (Function<Short, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<Short, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Short, BigInteger>)(x) -> BigInteger.valueOf(x.intValue()))
    );

    private static final ParsingInstructions<Character> CHARACTER_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(Character.class, (Function<Character, Character>)(x) -> x),
            Map.entry(String.class, (Function<Character, String>)String::valueOf),
            Map.entry(Integer.class, (Function<Character, Integer>)Integer::valueOf),
            Map.entry(Double.class, (Function<Character, Double>)Double::valueOf),
            Map.entry(Float.class, (Function<Character, Float>)Float::valueOf),
            Map.entry(Byte.class, (Function<Character, Byte>)(x) -> Byte.valueOf(String.valueOf(x))),
            Map.entry(Long.class, (Function<Character, Long>)Long::valueOf),
            Map.entry(Short.class, (Function<Character, Short>)(x) -> Short.valueOf(String.valueOf(x))),
            Map.entry(BigDecimal.class, (Function<Character, BigDecimal>)BigDecimal::valueOf),
            Map.entry(BigInteger.class, (Function<Character, BigInteger>)(x) -> new BigInteger(String.valueOf(x)))
    );

    private static final ParsingInstructions<BigDecimal> BIG_DECIMAL_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(BigDecimal.class, (Function<BigDecimal, BigDecimal>)(x) -> x),
            Map.entry(String.class, (Function<BigDecimal, String>)String::valueOf),
            Map.entry(Integer.class, (Function<BigDecimal, Integer>)BigDecimal::intValue),
            Map.entry(Double.class, (Function<BigDecimal, Double>)BigDecimal::doubleValue),
            Map.entry(Float.class, (Function<BigDecimal, Float>)BigDecimal::floatValue),
            Map.entry(Byte.class, (Function<BigDecimal, Byte>)BigDecimal::byteValue),
            Map.entry(Long.class, (Function<BigDecimal, Long>)BigDecimal::longValue),
            Map.entry(Short.class, (Function<BigDecimal, Short>)BigDecimal::shortValue),
            Map.entry(Character.class, (Function<BigDecimal, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigInteger.class, (Function<BigDecimal, BigInteger>)(x) -> new BigInteger(String.valueOf(x)))
    );

    private static final ParsingInstructions<BigInteger> BIG_INTEGER_ALLOWED_OPERATIONS = ParsingInstructions.ofEntries(
            Map.entry(BigInteger.class, (Function<BigInteger, BigInteger>)(x) -> x),
            Map.entry(String.class, (Function<BigInteger, String>)String::valueOf),
            Map.entry(Integer.class, (Function<BigInteger, Integer>)BigInteger::intValue),
            Map.entry(Double.class, (Function<BigInteger, Double>)BigInteger::doubleValue),
            Map.entry(Float.class, (Function<BigInteger, Float>)BigInteger::floatValue),
            Map.entry(Byte.class, (Function<BigInteger, Byte>)BigInteger::byteValue),
            Map.entry(Long.class, (Function<BigInteger, Long>)BigInteger::longValue),
            Map.entry(Short.class, (Function<BigInteger, Short>)BigInteger::shortValue),
            Map.entry(Character.class, (Function<BigInteger, Character>)(x) -> String.valueOf(x).charAt(0)),
            Map.entry(BigDecimal.class, (Function<BigInteger, BigDecimal>)(x) -> new BigDecimal(String.valueOf(x)))
    );

    /**
     * Instructions for converting from one type to another.<br>
     * You can specify it statically here, or you could add new instructions dynamically via {@link #instruct(Class, Class, Function)}
     */
    private static Map<Class, ParsingInstructions> ALLOWED_OPERATIONS = Map.ofEntries(
            Map.entry(String.class, STRING_ALLOWED_OPERATIONS),
            Map.entry(Integer.class, INTEGER_ALLOWED_OPERATIONS),
            Map.entry(Double.class, DOUBLE_ALLOWED_OPERATIONS),
            Map.entry(Float.class, FLOAT_ALLOWED_OPERATIONS),
            Map.entry(Byte.class, BYTE_ALLOWED_OPERATIONS),
            Map.entry(Long.class, LONG_ALLOWED_OPERATIONS),
            Map.entry(Short.class, SHORT_ALLOWED_OPERATIONS),
            Map.entry(Character.class, CHARACTER_ALLOWED_OPERATIONS),
            Map.entry(BigDecimal.class, BIG_DECIMAL_ALLOWED_OPERATIONS),
            Map.entry(BigInteger.class, BIG_INTEGER_ALLOWED_OPERATIONS)
    );
}
