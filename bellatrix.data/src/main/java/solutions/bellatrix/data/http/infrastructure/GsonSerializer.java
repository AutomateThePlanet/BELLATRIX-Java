package solutions.bellatrix.data.http.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import solutions.bellatrix.data.http.contracts.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GsonSerializer implements JsonSerializer {
    private final Gson gson;

    public GsonSerializer() {
        this(builder -> {
        });
    }

    public GsonSerializer(Consumer<GsonBuilder> consumer) {
        this.gson = getInstance(consumer);
    }

    @Override
    public <T> String serialize(T object) {
        if (object==null) {
            return "";
        }

        return gson.toJson(object);
    }

    @Override
    public <T> T deserialize(String json, Class<T> type) {
        if (json==null || json.isEmpty()) {
            return null;
        }

        if (type==null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to type: " + type.getName(), e);
        }
    }

    @Override
    public <T> List<T> deserializeList(String json, Class<T> elementType) {
        if (json==null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        Type listType = TypeToken.getParameterized(List.class, elementType).getType();

        return gson.fromJson(json, listType);
    }

    private Gson getInstance(Consumer<GsonBuilder> consumer) {
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        builder.disableHtmlEscaping();
        builder.setLenient();
        builder.setPrettyPrinting();
        consumer.accept(builder);

        return builder.create();
    }
}