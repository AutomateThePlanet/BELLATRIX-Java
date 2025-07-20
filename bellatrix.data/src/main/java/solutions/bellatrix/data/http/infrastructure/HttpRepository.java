package solutions.bellatrix.data.http.infrastructure;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.contracts.ObjectConverter;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.httpContext.HttpContextFactory;
import solutions.bellatrix.data.http.infrastructure.internal.DeserializationMode;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static solutions.bellatrix.data.http.infrastructure.HTTPMethod.*;

public abstract class HttpRepository<THttpEntity extends HttpEntity> implements Repository<THttpEntity> {
    public static final EventListener<RequestEventArgs> SENDING_REQUEST = new EventListener<>();
    public static final EventListener<ResponseEventArgs> REQUEST_SEND = new EventListener<>();
    protected final HttpContext repositoryContext;
    private final Class<THttpEntity> entityType;
    private final ObjectConverter objectConverter;
    private HttpContext requestContext;
    //  private RequestSpecification requestSpecification;

    protected HttpRepository(Class<THttpEntity> entityType, ObjectConverter objectConverter, Supplier<HttpContext> repositoryContext) {
        this.entityType = entityType;
        this.objectConverter = objectConverter;
        this.repositoryContext = repositoryContext.get();
        this.requestContext = HttpContextFactory.copyOf(this.repositoryContext);
    }

    @Override
    public THttpEntity getById(HttpEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity identifier cannot be null.");
        }

        updateRequestContext(requestContext -> {
            requestContext.addPathParameter(entity.getIdentifier());
            requestContext.addRequestMethod(GET);
        });

        var response = handleResponse(() -> sendRequest(requestContext));

        var record = (THttpEntity)deserializeInternal(response, DeserializationMode.SINGLE);

        return record;
    }

    @Override
    public THttpEntity create(THttpEntity entity) {
        updateRequestContext(requestContext -> {
            requestContext.addRequestBody((objectConverter.toString(entity)));
            requestContext.addRequestMethod(POST);
        });

        var response = handleResponse(() -> sendRequest(requestContext));

        var record = (THttpEntity)deserializeInternal(response, DeserializationMode.SINGLE);

        return record;
    }

    @Override
    public List<THttpEntity> getAll() {
        updateRequestContext(requestContext -> {
            requestContext.addRequestMethod(GET);
        });

        var response = handleResponse(() -> sendRequest(requestContext));

        var entities = (List<THttpEntity>)deserializeInternal(response, DeserializationMode.LIST);

        return entities;
    }

    @Override
    public void delete(THttpEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity identifier cannot be null.");
        }

        updateRequestContext(requestContext -> {
            requestContext.addPathParameter(String.valueOf(entity.getIdentifier()));
            requestContext.addRequestMethod(DELETE);
        });

        sendRequest(requestContext);
    }

    @Override
    public THttpEntity update(THttpEntity entity) {
        if (entity.hasInvalidIdentifier()) {
            throw new IllegalArgumentException("Entity identifier cannot be null.");
        }

        updateRequestContext(requestContext -> {
            requestContext.addRequestMethod(PUT);
            requestContext.addPathParameter(String.valueOf(entity.getIdentifier()));
            requestContext.updateRequestSpecification(specBuilder -> {
                specBuilder.setBody(objectConverter.toString(entity));
            });
        });

        var response = handleResponse(() -> sendRequest(requestContext));

        var record = (THttpEntity)deserializeInternal(response, DeserializationMode.SINGLE);

        return record;
    }

    protected Response sendRequest(HttpContext requestContext) {
        return switch (requestContext.getHttpMethod()) {
            case GET -> broadcastRequest(() -> client().get());
            case POST -> broadcastRequest(() -> client().post());
            case PUT -> broadcastRequest(() -> client().put(requestContext.buildRequestPath()));
            case DELETE -> broadcastRequest(() -> client().delete(requestContext.buildRequestPath()));
            case PATCH -> broadcastRequest(() -> client().patch(requestContext.buildRequestPath()));
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + requestContext.getHttpMethod());
        };
    }

    /**
     * Gets the serializer used by this repository.
     *
     * @return the  ObjectConverter instance used for type conversion
     */
    protected ObjectConverter getObjectConverter() {
        return objectConverter;
    }

    /**
     * Handles the response from the executed request.
     * This method can be overridden to customize the response handling.
     *
     * @param response the response from the executed request
     * @return a HandledResponse containing the response body and original response
     */
    protected HttpResponse handleResponse(Supplier<Response> response) {
        return new HttpResponse(response.get().getBody().asString(), response.get());
    }

    /**
     * Updates the request context with the provided configuration.
     * This context is used for the next request made by this repository.
     *
     * @param requestConfigConsumer a consumer that accepts the HttpContext to modify it
     */
    protected void updateRequestContext(Consumer<HttpContext> requestConfigConsumer) {
        requestConfigConsumer.accept(requestContext);
    }

    /**
     * Updates the repository context with the provided configuration.
     * This context is used as base for all requests made by this repository.
     *
     * @param requestConfigConsumer a consumer that accepts the HttpContext to modify it
     */
    protected void updateRepositoryContext(Consumer<HttpContext> requestConfigConsumer) {
        requestConfigConsumer.accept(repositoryContext);
    }

    /**
     * Restores the request context to the original repository context.
     * Allowing reuse of the repository context for subsequent requests without the context from the previous request.
     */
    private void restoreRequestContext() {
        requestContext = HttpContextFactory.copyOf(repositoryContext);
    }

    private RequestSpecification client() {
        System.out.println();
        return RestAssured.given().spec(requestContext.getRequestSpecBuilder().build());
    }

    private <R> R deserializeInternal(HttpResponse response, DeserializationMode mode) {
        try {
            if (mode==DeserializationMode.LIST) {
                List<THttpEntity> entities = objectConverter.fromStringToList(response.getBody(), entityType);
                entities.forEach(entity -> entity.setResponse(response.getResponse()));
                return (R)entities;
            } else {
                THttpEntity entity = objectConverter.fromString(response.getBody(), entityType);
                entity.setResponse(response.getResponse());
                return (R)entity;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response: " + response.getBody(), e);
        }
    }

//    private THttpEntity deserializeEntityFromResponse(HttpResponse response) {
//        try {
//            THttpEntity model = objectConverter.fromString(response.getBody(), entityType);
//
//            model.setResponse(response.getResponse());
//
//            return model;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse response: " + response.getBody(), e);
//        }
//    }
//
//    private List<THttpEntity> deserializeEntitiesFromResponse(Response response) {
//        try {
//            List<THttpEntity> result = objectConverter.fromStringToList(response.getBody().asString(), entityType);
//
//            result.forEach(entity -> entity.setResponse(response));
//
//            return result;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse response: " + response.getBody(), e);
//        }
//    }

    private Response broadcastRequest(Supplier<Response> responseSupplier) {
        try {
            SENDING_REQUEST.broadcast(new RequestEventArgs(requestContext, null));
            Response response = responseSupplier.get();
            REQUEST_SEND.broadcast(new ResponseEventArgs(response));
            return response;
        } finally {
            restoreRequestContext();
        }
    }
}