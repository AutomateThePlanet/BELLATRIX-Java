package solutions.bellatrix.data.core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.data.configuration.http.HTTPMethod;
import solutions.bellatrix.data.configuration.http.HttpSettings;
import solutions.bellatrix.data.configuration.http.auth.AuthSchemaFactory;
import solutions.bellatrix.data.configuration.http.urlBuilder.UrlBuilder;
import solutions.bellatrix.data.contracts.HttpEntity;
import solutions.bellatrix.data.contracts.JsonSerializer;
import solutions.bellatrix.data.contracts.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class HttpRepository<TEntity extends HttpEntity> implements Repository<TEntity>, AutoCloseable {
    private final UrlBuilder urlBuilder;
    private final JsonSerializer serializer;
    private final Class<TEntity> entityType;
    private final RequestSpecBuilder requestSpecBuilder;
    private final HttpSettings httpSettings;
    private List<TEntity> entities;

    public HttpRepository(Class<TEntity> entityType, HttpSettings httpSettings) {
        this(entityType, new GsonSerializer(), httpSettings);
    }

    public HttpRepository(Class<TEntity> entityType, JsonSerializer serializer, HttpSettings httpSettings) {
        this.entityType = entityType;
        this.serializer = serializer;
        this.httpSettings = httpSettings;
        this.urlBuilder = createUrlBuilder();
        this.requestSpecBuilder = createRequestSpecificationBuilder(httpSettings);
    }

    public ApiResponse<TEntity> get(HttpEntity entity) {
        customizeUrl(url -> {
            url.addPathSegments(List.of(entity.getIdentifier()));
        });

        Response response = executeRequest(HTTPMethod.GET);

        return deserializeEntity(response);
    }

    public List<ApiResponse<TEntity>> getAll(TEntity entity) {
        customizeUrl(url -> {
            url.addQueryParameters(entity.toQueryParams());
        });

        Response response = executeRequest(HTTPMethod.GET);

        return deserializeEntityList(response);
    }

    public ApiResponse<TEntity> create(TEntity entity) {
        Response response = executeRequest(HTTPMethod.POST);

        return deserializeEntity(response);
    }

    public ApiResponse<TEntity> update(TEntity entity) {
        customizeUrl(url -> {
            url.addPathSegments(List.of(entity.getIdentifier()));
        });

        Response response = executeRequest(HTTPMethod.PUT);

        return deserializeEntity(response);
    }

    public ApiResponse<TEntity> delete(TEntity entity) {
        customizeUrl(url -> {
            url.addPathSegments(List.of(entity.getIdentifier()));
        });

        Response response = executeRequest(HTTPMethod.DELETE);

        return deserializeEntity(response);
    }

    protected void customizeUrl(Consumer<UrlBuilder> urlBuilderConsumer) {
        urlBuilderConsumer.accept(urlBuilder);
    }

    protected void customizeRequest(Consumer<RequestSpecBuilder> specBuilderConsumer) {
        specBuilderConsumer.accept(requestSpecBuilder);
    }

    protected Response executeRequest(HTTPMethod httpMethod) {
        RequestSpecification spec = getRequestSpec();
        String url = urlBuilder.build();
        spec.baseUri(urlBuilder.getBasePath());
        spec.queryParams(urlBuilder.getQueryParameters());
        return switch (httpMethod) {
            case GET -> requestHandler(() -> spec.get());
            case POST -> requestHandler(() -> spec.post(url));
            case PUT -> requestHandler(() -> spec.put(url));
            case DELETE -> requestHandler(() -> spec.delete(url));
            case PATCH -> requestHandler(() -> spec.patch(url));
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod.getMethod());
        };
    }

    private UrlBuilder createUrlBuilder() {
        return new UrlBuilder(httpSettings);
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpecBuilder.build());
    }

    private RequestSpecBuilder createRequestSpecificationBuilder(HttpSettings httpSettings) {
        var builder = new RequestSpecBuilder();
        builder.log(LogDetail.ALL);
        builder.setContentType(httpSettings.getContentType());
        builder.setAuth(AuthSchemaFactory.getAuthenticationScheme(httpSettings.getAuthentication()));
        return builder;
    }

    private ApiResponse<TEntity> deserializeEntity(Response response) {
        String responseBody = response.getBody().asString();
        try {
            TEntity model = serializer.deserialize(responseBody, entityType);
            return new ApiResponse<>(response, model);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as JSON. Response: '" + responseBody + "'", e);
        }
    }

    private List<ApiResponse<TEntity>> deserializeEntityList(Response response) {
        String responseBody = response.getBody().asString();
        List<TEntity> entities = serializer.deserializeList(responseBody, entityType);

        List<ApiResponse<TEntity>> result = new ArrayList<>();
        for (TEntity entity : entities) {
            result.add(new ApiResponse<>(response, entity));
        }

        return result;
    }


    private Response requestHandler(Supplier<Response> requestSupplier) {
        try {
            Response response = requestSupplier.get();
            if (response.statusCode() >= 400 || response.statusCode() < 500) {
                System.out.println();
            }
            System.out.println();
            return response;

        } catch (Exception e) {
            // Step 5: Handle any runtime exceptions during request execution
            // throw new RequestExecutionException("Failed to execute HTTP request", e);
        }

        return null;
    }

    @Override
    public void close() throws Exception {

    }
}