package solutions.bellatrix.data.core;

import io.restassured.response.Response;
import lombok.Getter;
import solutions.bellatrix.data.contracts.Entity;

@Getter
public class ApiResponse<T extends Entity> {
    private final Response response;
    private final T entity;

    public ApiResponse(Response response, T entity) {
        this.response = response;
        this.entity = entity;
    }
}