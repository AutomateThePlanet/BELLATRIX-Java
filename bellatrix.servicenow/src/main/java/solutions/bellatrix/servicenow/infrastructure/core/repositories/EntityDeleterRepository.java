package solutions.bellatrix.servicenow.infrastructure.core.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowApiPathSegment;
import solutions.bellatrix.servicenow.utilities.generators.BaseInstancesUrlGeneration;

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
}