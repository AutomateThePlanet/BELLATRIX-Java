package solutions.bellatrix.data.http.contracts;

import solutions.bellatrix.data.http.infrastructure.GsonConverter;

import java.util.List;

/**
 * The {@code JsonSerializer} interface provides methods for serializing and deserializing
 * objects to and from JSON strings.
 * <p>
 * If you need to implement a custom JSON serialization mechanism, you can create a class
 * that implements this interface.
 * <p>
 * Example class:
 * {@link GsonConverter}
 */

public interface ObjectConverter {
    /**
     * Converts the given object of type T to a string representation.
     *
     * @param object the object to be converted to a string
     * @param <T>    the type of the object
     * @return the string representation of the object
     */
    <T> String toString(T object);

    /**
     * Converts the given JSON string to an object of type T.
     *
     * @param data string to convert to an object
     * @param type the class of the type to deserialize into
     * @param <T>  the type of the object
     * @return the deserialized object of type T
     */
    <T> T fromString(String data, Class<T> type);

    /**
     * Converts the given JSON string to a list of objects of type T.
     *
     * @param json        the JSON string to deserialize
     * @param elementType the class of the elements in the list
     * @param <T>         the type of the elements in the list
     * @return a list of deserialized objects of type T
     */
    <T> List<T> fromStringToList(String json, Class<T> elementType);
}