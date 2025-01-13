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
import java.util.Arrays;

public abstract class ObjectFactory {
    protected static <T> T newInstance(Class<T> clazz, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException, ConstructorNotFoundException {
        Constructor<T> suitableConstructor = getSuitableConstructor(clazz, args);

        if (suitableConstructor.isVarArgs() && args.length < suitableConstructor.getParameterCount()) {
            return (T)suitableConstructor.newInstance(addNullVarArgs(args));
        } else
            return (T)suitableConstructor.newInstance(args);
    }

    private static Object[] addNullVarArgs(Object... args) {
        var newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[newArgs.length - 1] = null;

        return newArgs;
    }

    private static <T> Constructor<T> getSuitableConstructor(Class<T> clazz, Object[] arguments) throws ConstructorNotFoundException {
        var argumentTypes = getArgumentTypes(arguments);

        try {
            return (Constructor<T>)findConstructorMatch(clazz, argumentTypes);
        } catch (NoSuchMethodException e) {
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
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor(argumentTypes);
        } catch (NoSuchMethodException e) {
            constructor = findVarArgsConstructor(clazz, argumentTypes);
        }

        return constructor;
    }

    private static <T> Constructor<T> findVarArgsConstructor(Class clazz, Class[] argumentTypes) throws NoSuchMethodException {
        try {
            var args = Arrays.copyOf(argumentTypes, argumentTypes.length + 1);
            args[args.length - 1] = Object[].class;

            return clazz.getDeclaredConstructor(args);
        } catch (NoSuchMethodException ignored) {
        }

        throw new NoSuchMethodException("No matching constructor found for the provided argument types.");
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
