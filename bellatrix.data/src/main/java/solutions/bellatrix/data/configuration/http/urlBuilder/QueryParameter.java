package solutions.bellatrix.data.configuration.http.urlBuilder;

import lombok.Getter;

@Getter
public class QueryParameter {
    private String key;
    private Object value;


    private QueryParameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static QueryParameter of(String key, Object value) {
        return new QueryParameter(key, value);
    }

    @Override
    public String toString() {
        return "%s=%s".formatted(key, value);
    }
}