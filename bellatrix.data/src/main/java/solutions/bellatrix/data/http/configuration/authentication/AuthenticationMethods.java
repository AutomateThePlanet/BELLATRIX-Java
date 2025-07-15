package solutions.bellatrix.data.http.configuration.authentication;

import lombok.Getter;

@Getter
public enum AuthenticationMethods {
    BEARER("Bearer"),
    BASIC("Basic"),
    QUERY_PARAMETER("QueryParameters");

    private final String method;

    AuthenticationMethods(String method) {
        this.method = method;
    }

    public static AuthenticationMethods parse(String y) {
        for (var state : values()) {
            String enumDisplayValue = state.getMethod();
            if (enumDisplayValue!=null && enumDisplayValue.equalsIgnoreCase(y)) {
                return state;
            }
        }

        throw new IllegalArgumentException("No enum constant with value: %s".formatted(y));
    }
}