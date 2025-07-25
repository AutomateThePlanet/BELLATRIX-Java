package solutions.bellatrix.servicenow.utilities;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class AssertionsHelper {
    public static void assertTrue(boolean condition) {
        Assertions.assertTrue(condition, "Expected condition to be true, but it was false");
    }

    public static void assertEquals(Object expected, Object actual) {
        Assertions.assertEquals(expected, actual, "Expected value to be %s but was %s".formatted(expected, actual));
    }

    public static void assertTableHeaders(List<String> expectedTableHeaders, List<String> actualTableHeaders) {
        var length = expectedTableHeaders.size();
        for (int i = 0; i < length; i++) {
            var expectedColumnValue = expectedTableHeaders.get(i);
            var actualColumnValue = actualTableHeaders.get(i);
            var errorMessage = "Mismatch between expected and actual table header:\nExpected table header: %s \nActual table header: %s\nList of expected column header values: %s\nList of actual column header values: %s".formatted(expectedColumnValue, actualColumnValue, expectedTableHeaders, actualTableHeaders);
            Assertions.assertEquals(expectedColumnValue, actualColumnValue, errorMessage);
        }
    }

    public static <T extends Object> void assertElementsCounts(List<T> elements, int expectedCount) {
        var actualCount = elements.size();
        var errorMessage = "Expected elements count to be %s but was %s".formatted(expectedCount, actualCount);

        Assertions.assertEquals(expectedCount, actualCount, errorMessage);
    }

    public static void assertEmpty(String value) {
        Assertions.assertTrue(value.isEmpty(), "Expected value to be empty, but was not empty");
    }

    public static void assertNotEmpty(String value) {
        Assertions.assertFalse(value.isEmpty(), "Expected value to be not empty, but was empty");
    }

    public static void assertTime(Date expectedDate, Date actualDate, long delta) {
        long time1 = expectedDate.getTime();
        long time2 = actualDate.getTime();

        long difference = Math.abs(time1 - time2);
        Assertions.assertTrue(difference <= delta);
    }
}