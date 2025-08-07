package solutions.bellatrix.data.http.infrastructure;

import io.restassured.response.Response;
import lombok.Getter;
import solutions.bellatrix.data.http.infrastructure.internal.HttpStatusCode;

@Getter
public class HttpResponse {
    private final String body;
    private final Response nativeResponse;
    private final HttpStatusCode statusCode;

    public HttpResponse(String body, Response response) {
        this.body = body;
        this.nativeResponse = response;
        statusCode = HttpStatusCode.parse(response.getStatusCode());
    }
}