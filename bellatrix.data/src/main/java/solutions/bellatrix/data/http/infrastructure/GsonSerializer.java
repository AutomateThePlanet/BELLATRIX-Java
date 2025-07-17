package solutions.bellatrix.data.http.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import solutions.bellatrix.data.http.contracts.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class GsonSerializer implements JsonSerializer {
    protected final Gson gson;

    public GsonSerializer() {
        this(builder -> {
        });
    }

    public GsonSerializer(Consumer<GsonBuilder> consumer) {
        this.gson = getInstance(consumer);
    }

    @Override
    public <T> String serialize(T object) {
        if (Objects.isNull(object)) {
            return "";
        }

        return gson.toJson(object);
    }

    @Override
    public <T> String serialize(T object, Class<?> type) {
        if (Objects.isNull(object)) {
            return "";
        }

        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        try {
            return gson.toJson(object, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object of type: " + type.getName(), e);
        }
    }

    @Override
    public <T> T deserialize(String json, Class<T> type) {
        if (Objects.isNull(json) || json.trim().isEmpty()) {
            return null;
        }

        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to type: " + type.getName(), e);
        }
    }

    @Override
    public <T> List<T> deserializeList(String json, Class<T> type) {
        if (Objects.isNull(json) || json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("Element type cannot be null");
        }

        Type listType = TypeToken.getParameterized(List.class, type).getType();

        try {
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to type: " + type.getName(), e);
        }
    }

    private Gson getInstance(Consumer<GsonBuilder> consumer) {
        //todo: make it readable from config
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        builder.disableHtmlEscaping();
        builder.setLenient();
        builder.setPrettyPrinting();
        consumer.accept(builder);

        return builder.create();
    }
}