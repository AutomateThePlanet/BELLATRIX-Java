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
    protected static <T> T newInstance(Class<T> clazz, Object... initargs) throws InvocationTargetException, InstantiationException, IllegalAccessException, ConstructorNotFoundException {
        var args = new Ref<Object[]>(initargs);

        Constructor<T> suitableConstructor = getSuitableConstructor(clazz, args);

        return (T)suitableConstructor.newInstance(args.value);
    }

    private static <T> Constructor<T> getSuitableConstructor(Class<T> clazz, Ref<Object[]> argumentsReference) throws ConstructorNotFoundException {
        var argumentTypes = getArgumentTypes(argumentsReference.value);

        try {
            var match = findMatch(clazz, argumentTypes);
            if (match.isVarArgs() && match.getParameterCount() != argumentsReference.value.length) {
                argumentsReference.value = Arrays.copyOf(argumentsReference.value, argumentsReference.value.length + 1);
                argumentsReference.value[argumentsReference.value.length - 1] = null;
            }
            return (Constructor<T>)match;
        } catch (NoSuchMethodException e) {
            var types = new StringBuilder();
            for (var type : argumentTypes) {
                types.append(type.getName() + System.lineSeparator());
            }

            throw new ConstructorNotFoundException(("""
                    No constructor with the specified parameters was found.
                    Given argument count: %d
                    Given argument types: %s
                    """).formatted(argumentTypes.length, types.toString())
            );
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

    private static <T> Constructor<T> findMatch(Class clazz, Class[] argumentTypes) throws NoSuchMethodException {
        var constructors = clazz.getDeclaredConstructors();

        for (var constructor : constructors) {
            var parameters = constructor.getParameters();

            if (parametersMatch(parameters, argumentTypes)) {
                return constructor;
            }
        }

        throw new NoSuchMethodException("No matching constructor found for the provided argument types.");
    }

    private static boolean parametersMatch(Parameter[] parameters, Class[] argumentTypes) {
        if (bothNullOrEmpty(parameters, argumentTypes)) {
            return true;
        } else if (lengthMismatch(parameters, argumentTypes)) {
            return false;
        }

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isVarArgs() && argumentTypes.length == i) {
                return true;
            }

            if (argumentTypes.length - 1 < i || !argumentTypes[i].equals(parameters[i].getType())) {
                return false;
            }
        }

        return true;
    }

    private static boolean lengthMismatch(Parameter[] parameters, Class[] argumentTypes) {
        return (argumentTypes == null) || (parameters.length < argumentTypes.length);
    }

    private static boolean bothNullOrEmpty(Parameter[] parameters, Class[] argumentTypes) {
        return (argumentTypes == null || argumentTypes.length == 0) && (parameters == null || parameters.length == 0);
    }

    public static class ConstructorNotFoundException extends Exception {
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
