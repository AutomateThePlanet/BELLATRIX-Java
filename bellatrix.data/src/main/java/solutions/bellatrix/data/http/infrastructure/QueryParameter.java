package solutions.bellatrix.data.configuration.http.urlBuilder;

import lombok.Getter;

import java.util.Objects;

@Getter
public class QueryParameter {
    private final String key;
    private final Object value;

    private QueryParameter(String key, Object value) {
        if ((Objects.isNull(key)) || key.isBlank()) {
            throw new IllegalArgumentException("QueryParameter key cannot be null or blank");
        }

        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("QueryParameter value cannot be null");
        }

        this.key = key;
        this.value = value;
    }

    public static QueryParameter of(String key, Object value) {
        return new QueryParameter(key, value);
    }
}