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
            if (!propertiesNotToCompareList.contains(currentRealProperty.getName())) {
                String currentRealPropertyName = currentRealProperty.getName();
                Method currentExpectedProperty = null;
                try {
                    currentExpectedProperty = expectedObject.getClass().getMethod(currentRealPropertyName);
                } catch (NoSuchMethodException e) {
                    failedAssertions.add(e);
                }

                var exceptionMessage = "The property " + currentRealProperty.getName() + " of class " + realObject.getClass().getSimpleName() + " was not as expected.";

                try {
                    if (currentRealProperty.getReturnType() == LocalDateTime.class) {
                        LocalDateTimeAssert.areEqual(
                                (LocalDateTime) currentExpectedProperty.invoke(expectedObject),
                                (LocalDateTime) currentRealProperty.invoke(realObject),
                                deltaType, deltaQuantity, exceptionMessage);
                    } else {
                        Assertions.assertEquals(
                                currentExpectedProperty.invoke(expectedObject),
                                currentRealProperty.invoke(realObject),
                                exceptionMessage);
                    }

                } catch (Exception ex) {
                    failedAssertions.add(ex);
                }
            }
        }
        return failedAssertions;
    }
}
