package solutions.bellatrix.servicenow.infrastructure.core.repositories;

import com.google.gson.*;
import solutions.bellatrix.data.http.infrastructure.internal.DeserializationMode;
import solutions.bellatrix.servicenow.infrastructure.core.entities.ServiceNowEntity;
import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.utilities.converters.TableApiConverter;
import io.restassured.response.Response;
import solutions.bellatrix.data.http.configuration.HttpSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpResponse;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;
import solutions.bellatrix.servicenow.models.annotations.TableTarget;

import java.util.List;
import java.util.function.Supplier;

import static solutions.bellatrix.data.http.infrastructure.HTTPMethod.GET;

public abstract class TableApiRepository<T extends ServiceNowEntity> extends ServiceNowRepository<T> {
    protected TableApiRepository(Class<T> entityType, ServiceNowTable serviceNowTable) {
        super(entityType, new TableApiConverter(), () -> {
            HttpSettings httpSettings = HttpSettings.custom(x -> x.setBasePath("/api/now/table/%s".formatted(serviceNowTable.getTableName())));
            var httpContext = new HttpContext(httpSettings);
            httpContext.addQueryParameter(new QueryParameter("sysparm_exclude_reference_link", "true"));
            return httpContext;
        });
    }

    protected TableApiRepository(Class<T> entityType) {
        super(entityType, new TableApiConverter(), () -> {
            String annotation = entityType.getAnnotation(TableTarget.class).value();
            HttpSettings httpSettings = HttpSettings.custom(x -> x.setBasePath("/api/now/table/%s".formatted(annotation)));
            HttpContext httpContext = new HttpContext(httpSettings);
            httpContext.addQueryParameter(new QueryParameter("sysparm_exclude_reference_link", "true"));
            return httpContext;
        });
    }

    @Override
    protected HttpResponse handleResponse(Supplier<Response> responseSupplier) {
        var suppliedResponse = responseSupplier.get();

        String stringResponse = suppliedResponse.getBody().asString();

        JsonObject responseObject = JsonParser.parseString(stringResponse).getAsJsonObject();
        JsonElement resultElement = responseObject.get("result");
        var cleanedResponse = new Gson().toJson(resultElement);

        return new HttpResponse(cleanedResponse, suppliedResponse);
    }

    public void validateEntityDoesNotExist(ServiceNowEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity identifier cannot be null.");
        }

        requestContext.addPathParameter(entity.getIdentifier());
        requestContext.addRequestMethod(GET);

        var httpResponse = sendRequest(requestContext);

       Assertions.assertEquals(404, httpResponse.getStatusCode());
    }

    public void validateEntityExists(ServiceNowEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity identifier cannot be null.");
        }

        var requestContext = new HttpContext(this.repositoryContext);
        requestContext.addPathParameter(entity.getIdentifier());
        requestContext.addRequestMethod(GET);

        var httpResponse = this.sendRequest(requestContext);

        Assertions.assertEquals(true, httpResponse.getBody().asString().isEmpty());
    }

    public List<T> getEntitiesByParameters (List<QueryParameter> listParameters) {
        this.requestContext.addRequestMethod(GET);
        this.requestContext.addQueryParameters(listParameters);

        var response = handleResponse(() -> sendRequest(this.requestContext));

        var entities = (List<T>)deserializeInternal(response, DeserializationMode.LIST);

        return entities;
    }
}