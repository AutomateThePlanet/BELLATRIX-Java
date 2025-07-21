/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriyam Kyoseva
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public abstract class ObjectFactory {
    protected static <T> T newInstance(Class<T> clazz, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, ConstructorNotFoundException {
        Constructor<T> suitableConstructor = getSuitableConstructor(clazz, args);
        try {
            if (suitableConstructor.isVarArgs() && args.length < suitableConstructor.getParameterCount()) {
                return (T)suitableConstructor.newInstance(addNullVarArgsTo(args));
            } else
                return (T)suitableConstructor.newInstance(args);
        } catch (Exception e) {
            var exception = (InvocationTargetException)e;
            throw new InvocationTargetException(exception.getTargetException());
        }
    }

    private static Object[] addNullVarArgsTo(Object... args) {
        return Arrays.copyOf(args, args.length + 1);
    }

    private static <T> Constructor<T> getSuitableConstructor(Class<T> clazz, Object[] arguments) throws ConstructorNotFoundException {
        var argumentTypes = getArgumentTypes(arguments);

        try {
            return findConstructorMatch(clazz, argumentTypes);
        } catch (NoSuchMethodException e) {
            if (argumentTypes == null || argumentTypes.length == 0) {
                throw new ConstructorNotFoundException();
            }

            var types = new StringBuilder();
            for (var type : argumentTypes) {
                types.append(type.getName() + System.lineSeparator());
            }

            throw new ConstructorNotFoundException(argumentTypes.length, types.toString());
        }
    }

    private static Class[] getArgumentTypes(Object... initargs) {
        if (initargs == null || initargs.length == 0) return null;

        Class[] argumentTypes = new Class[initargs.length];
        for (var i = 0; i < initargs.length; i++) {
            argumentTypes[i] = initargs[i].getClass();
        }

        return argumentTypes;
    }

    private static <T> Constructor<T> findConstructorMatch(Class clazz, Class[] argumentTypes) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredConstructor(argumentTypes);
        } catch (NoSuchMethodException e) {
            return findVarArgsConstructor(clazz, argumentTypes);
        }
    }

    private static <T> Constructor<T> findVarArgsConstructor(Class clazz, Class[] argumentTypes) throws NoSuchMethodException {
        try {
            var args = Arrays.copyOf(argumentTypes, argumentTypes.length + 1);
            args[args.length - 1] = getVarArgsType(clazz, argumentTypes);

            return clazz.getDeclaredConstructor(args);
        } catch (NoSuchMethodException | NullPointerException ignored) {
        }

        throw new NoSuchMethodException("No matching constructor found for the provided argument types.");
    }

    private static Class<?> getVarArgsType(Class clazz, Class[] argumentTypes) {
        var constructors = clazz.getDeclaredConstructors();

        for (var constructor : constructors) {
            var parameters = constructor.getParameters();
            if (constructor.isVarArgs() && lengthMatches(parameters, argumentTypes) && parameterTypesMatch(parameters, argumentTypes)) {
                return parameters[parameters.length - 1].getType();
            }
        }

        return null;
    }

    private static boolean lengthMatches(Parameter[] parameters, Class[] argumentTypes) {
        return parameters.length == argumentTypes.length + 1 && parameters[parameters.length - 1].isVarArgs();
    }

    private static boolean parameterTypesMatch(Parameter[] parameters, Class[] argumentTypes) {
        for (int i = 0; i < argumentTypes.length; i++) {
            if (!parameters[i].getType().equals(argumentTypes[i]))
                return false;
        }

        return true;
    }

    public static class ConstructorNotFoundException extends Exception {
        public ConstructorNotFoundException(int numberOfTypes, String types) {
            super(("""
                    No constructor with the specified parameters was found.
                    Given argument count: %d
                    Given argument types:\n%s
                    """).formatted(numberOfTypes, types));
        }

        public ConstructorNotFoundException() {
            super("No default constructor was found." + System.lineSeparator());
        }

        public ConstructorNotFoundException(String message) {
            super(message);
        }

        public ConstructorNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ConstructorNotFoundException(Throwable cause) {
            super(cause);
        }

        public ConstructorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}