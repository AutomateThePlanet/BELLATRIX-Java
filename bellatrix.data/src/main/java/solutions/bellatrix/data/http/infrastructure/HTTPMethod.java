package solutions.bellatrix.data.http.infrastructure;

import lombok.Getter;

@Getter
public enum HTTPMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH");
    
    private final String method;

    HTTPMethod(String method) {
        this.method = method;
    }
}