package solutions.bellatrix.data.http.infrastructure;

import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class HandledResponse {
    private final String body;
    private final Response response;

    public HandledResponse(String body, Response response) {
        this.body = body;
        this.response = response;
    }
}