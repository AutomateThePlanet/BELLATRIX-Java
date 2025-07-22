package solutions.bellatrix.data.http.infrastructure.events;

import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class ResponseProcessingEventArgs {
    private final Response response;

    public ResponseProcessingEventArgs(Response response) {
        this.response = response;
    }
}