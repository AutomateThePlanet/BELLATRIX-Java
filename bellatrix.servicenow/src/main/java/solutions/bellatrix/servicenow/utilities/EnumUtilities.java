package solutions.bellatrix.servicenow.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class provides utility methods for working with enums.
 */
public class EnumUtilities {
    /**
     * Retrieves the string values of the constants in the specified enum class. Do not forget to override toString()
     *
     * @param enumClass the enum class
     * @param <T>       the type of the enum
     * @return a list of string values representing the enum constants
     */
    public static <T extends Enum<T>> List<String> getEnumValues(Class<T> enumClass) {
        return Stream.of(enumClass.getEnumConstants()).map(Enum::toString).toList();
    }

    /**
     * Retrieves a list of all constants in the specified enum class.
     *
     * @param enumClass the enum class from which to retrieve the constants
     * @param <T>       the type of the enum
     * @return a list of enum constants of the specified type
     */
    public static <T extends Enum<T>> List<T> getEnums(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).toList();
    }
}