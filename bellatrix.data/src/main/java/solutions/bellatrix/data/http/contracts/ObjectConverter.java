package solutions.bellatrix.data.http.contracts;

import java.util.List;

public interface ObjectConverter {
    <T> String toString(T object);

    <T> T fromString(String data, Class<T> type);

    <T> List<T> fromStringToList(String json, Class<T> elementType);
}