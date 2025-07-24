package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.servicenow.data.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.snSetupData.enums.ServiceNowApiPathSegment;
import solutions.bellatrix.servicenow.utilities.BaseInstancesUrlGeneration;

import java.util.concurrent.CompletableFuture;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

public class EntityDeleterRepository {
    private Gson gson;
    private final RequestSpecification tableApiRequestSpecification;

    public EntityDeleterRepository() {
        var tableApiRequestSpecification = new RequestSpecBuilder()
                .setBaseUri(BaseInstancesUrlGeneration.getBaseUrl())
                .setBasePath(BaseInstancesUrlGeneration.getBasePath())
                .addPathParam("api", ServiceNowApiPathSegment.TABLE_API)
                .setConfig(new RestAssuredConfigFactory().createDefault())
                .addFilter(new CurlLoggingFilter())
                .build();

        String userName = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getUserName());
        String password = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getPassword());
        tableApiRequestSpecification.auth().basic(userName, password);

        this.tableApiRequestSpecification = tableApiRequestSpecification;
        configureGson();
    }

    protected void configureGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.setLenient();
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    public Gson getGson() {
        return gson;
    }

    public RequestSpecification getTableApiRequestSpecification() {
        return tableApiRequestSpecification;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public Response delete(ApiEntity toBeDeleted) {
        var response = RestAssured.given().spec(getTableApiRequestSpecification())
                .pathParam("table", toBeDeleted.getSysClassName())
                .when()
                .delete("/" + toBeDeleted.getEntityId());

        return response;
    }

    public CompletableFuture<Response> deleteAsync(ApiEntity toBeDeleted) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return RestAssured.given().spec(getTableApiRequestSpecification())
                    .pathParam("table", toBeDeleted.getSysClassName())
                    .when()
                    .delete("/" + toBeDeleted.getEntityId());
            }
            catch (Exception ex) {
                Log.error("Failed to delete entity %s. Error was: %s", toBeDeleted, ex.getMessage());
                return null;
            }
        });
    }
}