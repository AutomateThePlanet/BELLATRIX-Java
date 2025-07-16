package solutions.bellatrix.data.http.infrastructure;

import io.restassured.response.Response;
import lombok.Getter;

public class ResponseEventArgs {
    @Getter private final Response response;

    public ResponseEventArgs(Response response) {
        this.response = response;
    }
}