package solutions.bellatrix.data.http.contracts;

import java.util.List;

public interface JsonSerializer {
    <T> String serialize(T object);
    <T> T deserialize(String json, Class<T> type);
    <T> List<T> deserializeList(String json, Class<T> elementType);
}