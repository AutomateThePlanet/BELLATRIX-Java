package solutions.bellatrix.data.http.infrastructure.internal;

import lombok.Getter;

@Getter
public enum HttpStatusCode {
    // 1xx Informational responses
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    EARLY_HINTS(103, "Early Hints"),

    // 2xx Success
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    // 3xx Redirection
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),

    // 4xx Client errors
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    // 5xx Server errors
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private final int code;
    private final String reasonPhrase;

    HttpStatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public static HttpStatusCode parse(int statusCode) {
        for (var state : values()) {
            int enumDisplayValue = state.getCode();
            if (enumDisplayValue == statusCode) {
                return state;
            }
        }

        // If the status code is not found in the enum, return a generic status code
        // based on the status code range
        if (statusCode >= 200 && statusCode < 300) {
            return OK; // Return OK for any 2xx success code
        } else if (statusCode >= 400 && statusCode < 500) {
            return BAD_REQUEST; // Return BAD_REQUEST for any 4xx client error
        } else if (statusCode >= 500 && statusCode < 600) {
            return INTERNAL_SERVER_ERROR; // Return INTERNAL_SERVER_ERROR for any 5xx server error
        } else {
            // For other status codes (1xx, 3xx, etc.), return OK as default
            return OK;
        }
    }
}