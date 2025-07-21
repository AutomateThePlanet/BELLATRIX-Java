package solutions.bellatrix.data.http.infrastructure;

import lombok.Getter;

import java.util.Objects;

@Getter
public class QueryParameter {
    private final String key;
    private final Object value;
    
    public QueryParameter(String key, Object value) {
        if ((Objects.isNull(key)) || key.isBlank()) {
            throw new IllegalArgumentException("QueryParameter key cannot be null or blank");
        }

        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("QueryParameter value cannot be null");
        }

        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        QueryParameter that = (QueryParameter)obj;
        return key.equals(that.key);
    }
}