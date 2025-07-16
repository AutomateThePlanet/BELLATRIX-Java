package solutions.bellatrix.data.http.infrastructure;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.data.http.contracts.JsonSerializer;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.configuration.HttpSettings;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class HttpRepository<THttpEntity extends HttpEntity> implements Repository<THttpEntity> {
    private final EventListener<RequestEventArgs> ON_MAKING_REQUEST = new EventListener<>();
    private final EventListener<ResponseEventArgs> ON_REQUEST_MADE = new EventListener<>();
    private final JsonSerializer serializer;
    private final Class<THttpEntity> entityType;
    private final RequestConfiguration immutableConfig;
    private RequestConfiguration dynamicConfig;
    private RequestSpecification requestSpecification;

    public HttpRepository(Class<THttpEntity> entityType, HttpSettings httpSettings) {
        this(entityType, new GsonSerializer(), httpSettings);
    }

    public HttpRepository(Class<THttpEntity> entityType, JsonSerializer serializer, HttpSettings httpSettings) {
        this.entityType = entityType;
        this.serializer = serializer;
        immutableConfig = new RequestConfiguration(httpSettings);
        dynamicConfig = new RequestConfiguration(httpSettings);
    }

    public THttpEntity getById(HttpEntity entity) {
        if (Objects.isNull(entity) || entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizeRequestConfig(requestConfig -> {
            requestConfig.addPathParameter(entity.getIdentifier());
        });

        Response response = executeRequest(HTTPMethod.GET);

        return deserializeEntityFromResponse(response);
    }

    public THttpEntity create(THttpEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizeRequestConfig(requestConfig -> {
            requestConfig.customizeRequest(specBuilder -> {
                specBuilder.setBody(serializer.serialize(entity));
            });
        });

        Response response = executeRequest(HTTPMethod.POST);

        return deserializeEntityFromResponse(response);
    }


    @Override
    public List<THttpEntity> getAll() {
        Response response = executeRequest(HTTPMethod.GET);

        return deserializeEntitiesFromResponse(response);
    }

    @Override
    public void delete(THttpEntity entity) {
        if (Objects.isNull(entity) || entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizeRequestConfig(requestConfig -> {
            requestConfig.addPathParameter(entity.getIdentifier());
        });

        executeRequest(HTTPMethod.DELETE);
    }

    @Override
    public THttpEntity update(THttpEntity entity) {
        if (Objects.isNull(entity) || entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizeRequestConfig(requestConfig -> {
            requestConfig.customizeRequest(requestSpecBuilder -> {
                requestSpecBuilder.setBody(serializer.serialize(entity));
                requestConfig.addPathParameter(entity.getIdentifier());
            });
        });

        Response response = executeRequest(HTTPMethod.PUT);

        return deserializeEntityFromResponse(response);
    }


    protected Response executeRequest(HTTPMethod httpMethod) {
        setRequestSpecification();
        return switch (httpMethod) {
            case GET -> broadcastRequest(() -> getRequestSpec().get(dynamicConfig.getRequestPath()));
            case POST -> broadcastRequest(() -> getRequestSpec().post(dynamicConfig.getRequestPath()));
            case PUT -> broadcastRequest(() -> getRequestSpec().put(dynamicConfig.getRequestPath()));
            case DELETE -> broadcastRequest(() -> getRequestSpec().delete(dynamicConfig.getRequestPath()));
            case PATCH -> broadcastRequest(() -> getRequestSpec().patch(dynamicConfig.getRequestPath()));
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod.getMethod());
        };
    }

    protected void customizeRequestConfig(Consumer<RequestConfiguration> requestConfigConsumer) {
        requestConfigConsumer.accept(dynamicConfig);
    }

    private void resetConfiguration() {
        dynamicConfig = immutableConfig;
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpecification);
    }

    private void setRequestSpecification() {
        var requestedSpecification = dynamicConfig.requestSpecification();

        var queryParameters = dynamicConfig.getRequestQueryParameters();

        if (queryParameters!=null && !queryParameters.isEmpty()) {
            requestedSpecification.queryParams(queryParameters);
        }

        this.requestSpecification = requestedSpecification;
    }

    private THttpEntity deserializeEntityFromResponse(Response response) {
        try {
            THttpEntity model = serializer.deserialize(response.getBody().asString(), entityType);

            model.setResponse(response);

            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as JSON. Response: '" + response.getBody().asString() + "'", e);
        }
    }

    private List<THttpEntity> deserializeEntitiesFromResponse(Response response) {
        try {
            List<THttpEntity> result = serializer.deserializeList(response.getBody().asString(), entityType);

            result.forEach(entity -> entity.setResponse(response));

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as JSON. Response: '" + response.getBody().asString() + "'", e);
        }
    }

    private Response broadcastRequest(Supplier<Response> responseSupplier) {
        try {
            ON_MAKING_REQUEST.broadcast(new RequestEventArgs(dynamicConfig));
            Response response = responseSupplier.get();
            ON_REQUEST_MADE.broadcast(new ResponseEventArgs(response));
            return response;
        } finally {
            resetConfiguration();
        }
    }
}