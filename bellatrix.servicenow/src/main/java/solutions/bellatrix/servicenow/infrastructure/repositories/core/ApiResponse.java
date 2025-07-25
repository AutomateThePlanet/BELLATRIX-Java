package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import io.restassured.response.Response;

public class ApiResponse<TEntity> {
    private Response response;
    private TEntity result;

    public ApiResponse(Response response, TEntity result) {
        this.response = response;
        this.result = result;
    }

    public Response getResponse() {
        return response;
    }

    public TEntity getResult() {
        return result;
    }
}