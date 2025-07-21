package solutions.bellatrix.data.http.authentication;

import lombok.Getter;

@Getter
public enum AuthenticationMethod {
    BEARER("Bearer"),
    BASIC("Basic"),
    QUERY_PARAMETER("QueryParameters");

    private final String method;

    AuthenticationMethod(String method) {
        this.method = method;
    }

    public static AuthenticationMethod parse(String y) {
        for (var state : values()) {
            String enumDisplayValue = state.getMethod();
            if (enumDisplayValue != null && enumDisplayValue.equalsIgnoreCase(y)) {
                return state;
            }
        }

        throw new IllegalArgumentException("No enum constant with value: %s".formatted(y));
    }
}