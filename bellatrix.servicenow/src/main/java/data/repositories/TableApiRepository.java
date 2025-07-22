package data.repositories;

import data.entities.ServiceNowEntity;
import data.contracts.ServiceNowTable;
import data.converters.TableApiConverter;
import io.restassured.response.Response;
import solutions.bellatrix.data.http.configuration.HttpSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpResponse;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;

import java.util.Map;
import java.util.function.Supplier;

public abstract class TableApiRepository<T extends ServiceNowEntity> extends ServiceNowRepository<T> {
    protected TableApiRepository(Class<T> entityType, ServiceNowTable serviceNowTable) {
        super(entityType, new TableApiConverter(), () -> {
            HttpSettings httpSettings = HttpSettings.custom(x -> x.setBasePath("api/now/table/%s".formatted(serviceNowTable.getTableName())));
            var httpContext = new HttpContext(httpSettings);
            httpContext.addQueryParameter(new QueryParameter("sysparm_exclude_reference_link", "true"));
            return httpContext;
        });
    }

    @Override
    protected HttpResponse handleResponse(Supplier<Response> responseSupplier) {
        var suppliedResponse = responseSupplier.get();
        var entityJson = suppliedResponse.jsonPath().getJsonObject("result");
        TableApiConverter servicenowDeserializer = (TableApiConverter)getObjectConverter();
        String body = servicenowDeserializer.toString(entityJson, Map.class);
        return new HttpResponse(body, suppliedResponse);
    }
}