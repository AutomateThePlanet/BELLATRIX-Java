package solutions.bellatrix.data.http.infrastructure.events;

import io.restassured.response.Response;
import lombok.Getter;

public class ResponseProcessingEventArgs {
    @Getter private final Response response;

    public ResponseProcessingEventArgs(Response response) {
        this.response = response;
    }
}