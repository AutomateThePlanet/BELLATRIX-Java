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
package solutions.bellatrix.core.assertions;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntitiesAsserter {
    public static <TEntity> Boolean areEqual(TEntity expectedObject, TEntity realObject, DateTimeDeltaType deltaType, int deltaQuantity, String... propertiesNotToCompare) {
        List<Exception> failedAssertions = assertAreEqualsInternal(expectedObject, realObject, deltaType, deltaQuantity, propertiesNotToCompare);
        if (failedAssertions.size() > 1) {
            return false;
        }
        return true;
    }
    public static <TEntity> Boolean areEqual(TEntity expectedObject, TEntity realObject, String... propertiesNotToCompare) {
        return areEqual(expectedObject, realObject, DateTimeDeltaType.MILLISECONDS, 300, propertiesNotToCompare);
    }

    public static <TEntity> void assertAreEqual(TEntity expectedObject, TEntity realObject, DateTimeDeltaType deltaType, int deltaQuantity, String... propertiesNotToCompare) {
        List<Exception> failedAssertions = assertAreEqualsInternal(expectedObject, realObject, deltaType, deltaQuantity, propertiesNotToCompare);
        if (failedAssertions.size() > 1) {
            StringBuilder allFailedAssertions = new StringBuilder();
            allFailedAssertions.append("\nExceptions are:\n");
            for (var exception : failedAssertions) {
                allFailedAssertions.append(exception.toString() + "\n");
            }
            Assertions.fail(allFailedAssertions.toString());
        }
    }

    public static <TEntity> void assertAreEqual(TEntity expectedObject, TEntity realObject, String... propertiesNotToCompare) {
        assertAreEqual(expectedObject, realObject, DateTimeDeltaType.MILLISECONDS, 300, propertiesNotToCompare);
    }

    private static <TEntity> List<Exception> assertAreEqualsInternal(TEntity expectedObject, TEntity realObject, DateTimeDeltaType deltaType, int deltaQuantity, String[] propertiesNotToCompare) {
        List<Exception> failedAssertions = new ArrayList<>();
        List<String> propertiesNotToCompareList = Arrays.stream(propertiesNotToCompare).toList();
        var properties = Arrays.stream(realObject.getClass().getMethods()).toList()
                .stream().filter(s -> s.getName().startsWith("get")).toList();

        for (var currentRealProperty : properties) {
            String currentRealPropertyName = currentRealProperty.getName().replaceFirst("get", "");
            if (propertiesNotToCompareList.stream().filter(p -> p.equalsIgnoreCase(currentRealPropertyName)).count() == 0) {
                Method currentExpectedProperty = null;
                try {
                    currentExpectedProperty = expectedObject.getClass().getMethod(currentRealProperty.getName());
                } catch (NoSuchMethodException e) {
                    System.out.println(String.format("Property %s not found.", currentRealProperty));
//                    failedAssertions.add(e);
                }

                var exceptionMessage = "The property " + currentRealProperty.getName() + " of class " + realObject.getClass().getSimpleName() + " was not as expected.";

                try {
                    if (currentRealProperty.getReturnType() == LocalDateTime.class) {
                        LocalDateTimeAssert.areEqual(
                                (LocalDateTime)currentExpectedProperty.invoke(expectedObject),
                                (LocalDateTime)currentRealProperty.invoke(realObject),
                                deltaType, deltaQuantity, exceptionMessage);
                    } else {
                        Assertions.assertEquals(
                                currentExpectedProperty.invoke(expectedObject),
                                currentRealProperty.invoke(realObject),
                                exceptionMessage);
                    }
                }
                catch (NoSuchMethodException nsm){
                    //ignore this case
                }
                catch (NullPointerException nsmex){
                    //ignore this case
                }
                catch (Exception ex) {
                    failedAssertions.add(ex);
                }
            }
        }
        return failedAssertions;
    }
}
