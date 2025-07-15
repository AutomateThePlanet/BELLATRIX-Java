package solutions.bellatrix.data.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.data.configuration.http.ClientSideException;
import solutions.bellatrix.data.configuration.http.HTTPMethod;
import solutions.bellatrix.data.configuration.http.RequestConfiguration;
import solutions.bellatrix.data.contracts.HttpEntity;
import solutions.bellatrix.data.contracts.JsonSerializer;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.configuration.HttpSettings;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class HttpRepository<TEntity extends HttpEntity> implements Repository<TEntity> {
    private final EventListener<RequestEventArgs> ON_MAKING_REQUEST = new EventListener<>();
    private final EventListener<RequestEventArgs> ON_REQUEST_MADE = new EventListener<>();
    private final JsonSerializer serializer;
    private final Class<TEntity> entityType;
    private final RequestConfiguration requestConfig;
    private RequestSpecification requestSpecification;

    public HttpRepository(Class<TEntity> entityType, HttpSettings httpSettings) {
        this(entityType, new GsonSerializer(), httpSettings);
    }

    public HttpRepository(Class<TEntity> entityType, JsonSerializer serializer, HttpSettings httpSettings) {
        this.entityType = entityType;
        this.serializer = serializer;
        requestConfig = new RequestConfiguration(httpSettings);
    }

    protected void customizeRequestConfig(Consumer<RequestConfiguration> requestConfigConsumer) {
        requestConfigConsumer.accept(requestConfig);
    }

    public TEntity getById(HttpEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        if (Objects.isNull(entity.getIdentifier()) || entity.getIdentifier().isEmpty()) {
            throw new IllegalArgumentException("Entity identifier cannot be null or empty.");
        }

        customizeRequestConfig(url -> {
            url.addPathParameter(entity.getIdentifier());
            url.addPathParameter("member");
        });

        Response response = executeRequest(HTTPMethod.GET);

        return deserializeEntity(response);
    }

    public TEntity createEntity(TEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        try {
            var x = entityType.getDeclaredConstructor().newInstance();
            System.out.println();

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        //send
        ON_MAKING_REQUEST.broadcast(new RequestEventArgs());
        Response response = executeRequest(HTTPMethod.POST);
        // onResponseReceived
        ON_REQUEST_MADE.broadcast(new RequestEventArgs());

        return deserializeEntity(response);
    }

    public ApiResponse<TEntity> deleteEntity(TEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        if (Objects.isNull(entity.getIdentifier()) || entity.getIdentifier().isEmpty()) {
            throw new IllegalArgumentException("Entity identifier cannot be null or empty.");
        }

        customizeRequestConfig(requestConfiguration -> {
            requestConfiguration.addPathParameter(entity.getIdentifier());
        });

        Response response = executeRequest(HTTPMethod.DELETE);

        return new ApiResponse<>(response, null);
    }

    protected Response executeRequest(HTTPMethod httpMethod) {
        setRequestSpecification();
        return switch (httpMethod) {
            case GET -> requestSender(() -> getRequestSpec().get(requestConfig.getRequestPath()));
            case POST -> requestSender(() -> getRequestSpec().post(requestConfig.getRequestPath()));
            case PUT -> requestSender(() -> getRequestSpec().put(requestConfig.getRequestPath()));
            case DELETE -> requestSender(() -> getRequestSpec().delete(requestConfig.getRequestPath()));
            case PATCH -> requestSender(() -> getRequestSpec().patch(requestConfig.getRequestPath()));
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod.getMethod());
        };
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpecification);
    }

    private void setRequestSpecification() {
        var requestedSpecification = requestConfig.requestSpecification();

        var queryParameters = requestConfig.getRequestQueryParameters();

        if (queryParameters!=null && !queryParameters.isEmpty()) {
            requestedSpecification.queryParams(queryParameters);
        }

        this.requestSpecification = requestedSpecification;
    }

    private TEntity deserializeEntity(Response response) {
        String responseBody = response.getBody().asString();
        try {
            TEntity model = serializer.deserialize(responseBody, entityType);
            model.setResponse(response);

            return model;
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


    private Response requestSender(Supplier<Response> requestSupplier) {
        Response response = null;
        try {
            response = requestSupplier.get();
            if (response.statusCode() >= 400 && response.statusCode() < 500) {
                throw new ClientSideException("Client-side error occurred: " + response.getStatusCode());
            }

            return response;
        } catch (Exception e) {
            Log.error("Request failed with status code: " + response.statusCode());
            Log.error("Response body: " + serializer.serialize(response.getBody().asString()));
        }
        throw new ClientSideException("Client-side error occurred: " + response.getStatusCode());
    }
}