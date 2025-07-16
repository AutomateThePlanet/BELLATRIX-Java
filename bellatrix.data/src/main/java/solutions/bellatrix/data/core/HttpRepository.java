package solutions.bellatrix.data.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.data.configuration.http.HTTPMethod;
import solutions.bellatrix.data.configuration.http.RequestConfiguration;
import solutions.bellatrix.data.contracts.HttpEntity;
import solutions.bellatrix.data.contracts.JsonSerializer;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.configuration.HttpSettings;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class HttpRepository<THttpEntity extends HttpEntity> implements Repository<THttpEntity> {
    private final EventListener<RequestEventArgs> ON_MAKING_REQUEST = new EventListener<>();
    private final EventListener<RequestEventArgs> ON_REQUEST_MADE = new EventListener<>();
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

    protected void customizeRequestConfig(Consumer<RequestConfiguration> requestConfigConsumer) {
        requestConfigConsumer.accept(dynamicConfig);
    }

    public THttpEntity getById(HttpEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        if (Objects.isNull(entity.getIdentifier()) || entity.getIdentifier().isEmpty()) {
            throw new IllegalArgumentException("Entity identifier cannot be null or empty.");
        }

        customizeRequestConfig(url -> {
            url.addPathParameter(entity.getIdentifier());
        });

        Response response = executeRequest(HTTPMethod.GET);
        dynamicConfig = immutableConfig;
        return deserializeEntity(response);
    }

    public THttpEntity create(THttpEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        //send
        ON_MAKING_REQUEST.broadcast(new RequestEventArgs());
        try {
            Response response = executeRequest(HTTPMethod.POST);
            return deserializeEntity(response);
        } catch (Exception e) {
            dynamicConfig = immutableConfig;
            e.printStackTrace();
        }
        // onResponseReceived
        ON_REQUEST_MADE.broadcast(new RequestEventArgs());

        return null;
    }

    protected Response executeRequest(HTTPMethod httpMethod) {
        setRequestSpecification();
        return switch (httpMethod) {
            case GET -> getRequestSpec().get(dynamicConfig.getRequestPath());
            case POST -> getRequestSpec().post(dynamicConfig.getRequestPath());
            case PUT -> getRequestSpec().put(dynamicConfig.getRequestPath());
            case DELETE -> getRequestSpec().delete(dynamicConfig.getRequestPath());
            case PATCH -> getRequestSpec().patch(dynamicConfig.getRequestPath());
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod.getMethod());
        };
    }

    @Override
    public List<THttpEntity> getAll() {
        return List.of();
    }

    @Override
    public void delete(THttpEntity entity) {

    }


    @Override
    public THttpEntity update(THttpEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        if (Objects.isNull(entity.getIdentifier()) || entity.getIdentifier().isEmpty()) {
            throw new IllegalArgumentException("Entity identifier cannot be null or empty.");
        }

        customizeRequestConfig(url -> {
            url.addPathParameter(entity.getIdentifier());
        });

        Response response = executeRequest(HTTPMethod.PUT);
        dynamicConfig = immutableConfig;
        deserializeEntity(response);
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

    private THttpEntity deserializeEntity(Response response) {
        String responseBody = response.getBody().asString();
        try {
            THttpEntity model = serializer.deserialize(responseBody, entityType);
            model.setResponse(response);

            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as JSON. Response: '" + responseBody + "'", e);
        }
    }

    private List<THttpEntity> deserializeEntityList(Response response) {
        String responseBody = response.getBody().asString();
        List<THttpEntity> result = serializer.deserializeList(responseBody, entityType);
        for (THttpEntity entity : result) {
            entity.setResponse(response);
        }
        return result;
    }
}