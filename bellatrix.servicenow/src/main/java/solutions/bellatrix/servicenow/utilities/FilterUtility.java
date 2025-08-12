package solutions.bellatrix.servicenow.utilities;

import java.util.Objects;

public class FilterUtility {
    public static boolean Equal(Double elementToCompare, Double valueToCompare) {
        return Objects.equals(elementToCompare, valueToCompare);
    }

    public static boolean DoesNotEqual(Double elementToCompare, Double valueToCompare) {
        return !Objects.equals(elementToCompare, valueToCompare);
    }

    public static boolean IsLessThan(Double elementToCompare, Double valueToCompare) {
        return elementToCompare < valueToCompare;
    }

    public static boolean IsGreaterThan(Double elementToCompare, Double valueToCompare) {
        return elementToCompare > valueToCompare;
    }

    public static boolean IsLessThanOrEqualTo(Double elementToCompare, Double valueToCompare) {
        return elementToCompare <= valueToCompare;
    }

    public static boolean IsGreaterThanOrEqualTo(Double elementToCompare, Double valueToCompare) {
        return elementToCompare >= valueToCompare;
    }

    public static boolean Equal(String elementToCompare, String valueToCompare) {
        return Objects.equals(elementToCompare, valueToCompare);
    }

    public static boolean Contains(String elementToCompare, String valueToCompare) {
        return elementToCompare.contains(valueToCompare);
    }
}