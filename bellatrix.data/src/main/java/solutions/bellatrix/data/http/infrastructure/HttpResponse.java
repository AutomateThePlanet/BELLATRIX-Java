package solutions.bellatrix.data.http.infrastructure;

import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class HttpResponse {
    private final String body;
    private final Response response;

    public HttpResponse(String body, Response response) {
        this.body = body;
        this.response = response;
    }
}