package solutions.bellatrix.data.http.infrastructure;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.Getter;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.configuration.HttpSettings;
import solutions.bellatrix.data.http.contracts.JsonSerializer;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpRepository<THttpEntity extends HttpEntity> implements Repository<THttpEntity> {
    public static final EventListener<RequestEventArgs> ON_MAKING_REQUEST = new EventListener<>();
    public static final EventListener<ResponseEventArgs> ON_REQUEST_MADE = new EventListener<>();
    private final HttpContext repositoryContext;
    @Getter(AccessLevel.PROTECTED)
    private final JsonSerializer serializer;
    private final Class<THttpEntity> entityType;
    private HttpContext requestContext;
    private RequestSpecification requestSpecification;

    public HttpRepository(Class<THttpEntity> entityType, HttpSettings httpSettings) {
        this(entityType, new GsonSerializer(), httpSettings);
    }

    public HttpRepository(Class<THttpEntity> entityType, HttpContext httpContext) {
        this.entityType = entityType;
        this.serializer = new GsonSerializer();
        this.repositoryContext = httpContext;
//        this.requestContext = HttpContext.from(repositoryContext);
//        this.requestSpecification = httpContext.requestSpecification();
    }

    public HttpRepository(Class<THttpEntity> entityType, JsonSerializer serializer, HttpSettings httpSettings) {
        this.entityType = entityType;
        this.serializer = serializer;
        repositoryContext = new HttpContext(httpSettings);
        // requestContext = HttpContext.from(repositoryContext);
    }

    public THttpEntity getById(HttpEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizePerRequestConfig(requestConfig -> {
            requestConfig.addPathParameter(String.valueOf(entity.getIdentifier()));
        });

        var response = handleResponse(executeRequest(HTTPMethod.GET));

        return deserializeEntityFromResponse(response);
    }

    public THttpEntity create(THttpEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizePerRequestConfig(requestConfig -> {
            requestConfig.customizeRequest(specBuilder -> {
                specBuilder.setBody(serializer.serialize(entity));
            });
        });

        var response = handleResponse(executeRequest(HTTPMethod.POST));

        return deserializeEntityFromResponse(response);
    }


    @Override
    public List<THttpEntity> getAll() {
        Response response = executeRequest(HTTPMethod.GET);

        return deserializeEntitiesFromResponse(response);
    }

    @Override
    public void delete(THttpEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizePerRequestConfig(requestConfig -> {
            requestConfig.addPathParameter(String.valueOf(entity.getIdentifier()));
        });

        executeRequest(HTTPMethod.DELETE);
    }

    @Override
    public THttpEntity update(THttpEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        customizePerRequestConfig(requestConfig -> {
            requestConfig.customizeRequest(requestSpecBuilder -> {
                requestSpecBuilder.setBody(serializer.serialize(entity));
                requestConfig.addPathParameter(String.valueOf(entity.getIdentifier()));
            });
        });

        var response = handleResponse(executeRequest(HTTPMethod.PUT));

        return deserializeEntityFromResponse(response);
    }

    protected Response executeRequest(HTTPMethod httpMethod) {
        setRequestSpecification();
        return switch (httpMethod) {
            case GET -> broadcastRequest(() -> getRequestSpec().get(requestContext.getRequestPath()));
            case POST -> broadcastRequest(() -> getRequestSpec().post(requestContext.getRequestPath()));
            case PUT -> broadcastRequest(() -> getRequestSpec().put(requestContext.getRequestPath()));
            case DELETE -> broadcastRequest(() -> getRequestSpec().delete(requestContext.getRequestPath()));
            case PATCH -> broadcastRequest(() -> getRequestSpec().patch(requestContext.getRequestPath()));
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod.getMethod());
        };
    }


    protected HandledResponse handleResponse(Response response) {
        return new HandledResponse(response.getBody().asString(), response);
    }

    protected void customizePerRequestConfig(Consumer<HttpContext> requestConfigConsumer) {
        requestConfigConsumer.accept(requestContext);
    }

    protected void customizeRequestConfig(Consumer<HttpContext> requestConfigConsumer) {
        requestConfigConsumer.accept(repositoryContext);
    }

    private void resetConfiguration() {
        requestContext = repositoryContext;
    }

    private RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpecification);
    }

    private void setRequestSpecification() {
        var requestedSpecification = requestContext.requestSpecification();

        var queryParameters = requestContext.getRequestQueryParameters();

        if (queryParameters!=null && !queryParameters.isEmpty()) {
            requestedSpecification.queryParams(queryParameters);
        }

        this.requestSpecification = requestedSpecification;
    }

    private THttpEntity deserializeEntityFromResponse(HandledResponse response) {
        try {
            THttpEntity model = serializer.deserialize(response.getBody(), entityType);

            model.setResponse(response.getResponse());

            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as JSON. Response: '" + response.getBody() + "'", e);
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
            ON_MAKING_REQUEST.broadcast(new RequestEventArgs(requestContext, null));
            Response response = responseSupplier.get();
            ON_REQUEST_MADE.broadcast(new ResponseEventArgs(response));
            return response;
        } finally {
            resetConfiguration();
        }
    }
}