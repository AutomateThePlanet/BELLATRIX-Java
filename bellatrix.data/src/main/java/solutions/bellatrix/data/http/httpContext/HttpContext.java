package solutions.bellatrix.data.http.httpContext;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.data.http.authentication.AuthSchemaFactory;
import solutions.bellatrix.data.http.authentication.AuthenticationMethod;
import solutions.bellatrix.data.http.configuration.HttpSettings;
import solutions.bellatrix.data.http.infrastructure.Entity;
import solutions.bellatrix.data.http.infrastructure.HTTPMethod;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;

import java.util.*;
import java.util.function.Consumer;

public class HttpContext {
    private final HttpSettings httpSettings;
    private final LinkedList<Object> pathParameters;
    private final LinkedHashSet<QueryParameter> queryParameters;
    @Setter
    private RequestSpecBuilder specBuilder;
    @Getter
    private String requestBody;
    @Getter private HTTPMethod httpMethod;

    public HttpContext(HttpSettings settings) {
        this.httpSettings = settings;
        this.pathParameters = new LinkedList<>();
        this.queryParameters = new LinkedHashSet<>();
        this.specBuilder = createInitialSpecBuilder(httpSettings);
    }

    public HttpContext(HttpContext httpContext) {
        this.httpSettings = httpContext.getHttpSettings();
        this.queryParameters = httpContext.getQueryParameters();
        this.pathParameters = httpContext.getPathParameters();
        this.specBuilder = httpContext.getRequestBuilder();
    }

    public HttpSettings getHttpSettings() {
        return new HttpSettings(httpSettings);
    }

    public void updateRequestSpecification(Consumer<RequestSpecBuilder> specBuilderConsumer) {
        specBuilderConsumer.accept(specBuilder);
    }

    public void addRequestBody(String body) {
        this.requestBody = body;
    }

    public void addRequestMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public LinkedList<Object> getPathParameters() {
        return new LinkedList<>(pathParameters);
    }

    public LinkedHashSet<QueryParameter> getQueryParameters() {
        return new LinkedHashSet<>(queryParameters);
    }

    private RequestSpecBuilder getRequestBuilder() {
        return new RequestSpecBuilder().addRequestSpecification(specBuilder.build());
    }

    public RequestSpecification requestSpecification() {
        if (requestBody!=null) {
            specBuilder.setBody(requestBody);
        }

        if (!queryParameters.isEmpty()) {
            specBuilder.addQueryParams(getRequestQueryParameters());
        }

        specBuilder.setBasePath(buildRequestPath());

        return specBuilder.build();
    }

    public void addPathParameter(Object param) {
        if (Objects.isNull(param)) {
            throw new IllegalArgumentException("Path parameter cannot be null.");
        }

        pathParameters.add(String.valueOf(param));
    }

    public void addPathParameters(Collection<Object> params) {
        for (Object param : params) {
            addPathParameter(param);
        }
    }

    public void addQueryParameters(Collection<QueryParameter> parameters) {
        parameters.forEach(this::addQueryParameter);
    }

    public void addQueryParameter(QueryParameter parameter) {
        queryParameters.add(parameter);
    }

    private Map<String, String> getRequestQueryParameters() {
        //todo: this logic should be extracted because create tightly coupling with settings
        LinkedHashMap<String, String> queryParams = new LinkedHashMap<>();
        if (httpSettings.getAuthenticationMethod()==AuthenticationMethod.QUERY_PARAMETER) {
            var option = httpSettings.getAuthentication().getAuthenticationOptions().stream().filter(x -> x.get("type").equals("QueryParameters")).findFirst().get();
            String insertionOrder = (String)option.get("insertionOrder");
            if (insertionOrder.equals("start")) {
                for (var key : option.keySet()) {
                    if (!key.equals("type") && !key.equals("insertionOrder")) {
                        queryParams.put(key, option.get(key).toString());
                    }
                }
                for (QueryParameter queryParameter : queryParameters) {
                    queryParams.put(queryParameter.getKey(), String.valueOf(queryParameter.getValue()));
                }
            } else {
                for (QueryParameter queryParameter : queryParameters) {
                    queryParams.put(queryParameter.getKey(), queryParameter.getValue().toString());
                }
                for (var key : option.keySet()) {
                    if (!key.equals("type") && !key.equals("insertionOrder")) {
                        queryParams.put(key, option.get(key).toString());
                    }
                }

            }
            return queryParams;

        }

        for (QueryParameter queryParameter : queryParameters) {
            queryParams.put(queryParameter.getKey(), queryParameter.getValue().toString());
        }

        return queryParams;
    }


    public String buildRequestPath() {
        if (!pathParameters.isEmpty()) {
            String[] pathList = pathParameters.stream().filter(path -> !String.valueOf(path).isEmpty()).map(String::valueOf).toArray(String[]::new);
            String formattedPath = String.join("/", pathList);
            return httpSettings.getBasePath() + "/%s".formatted(formattedPath);
        }

        return httpSettings.getBasePath();
    }

    private RequestSpecBuilder createInitialSpecBuilder(HttpSettings httpSettings) {
        return new RequestSpecBuilder()
                .setBaseUri(httpSettings.getBaseUrl())
                .setBasePath(httpSettings.getBasePath()).setContentType(httpSettings.getContentType())
                .setAuth(AuthSchemaFactory.getAuthenticationScheme(httpSettings.getAuthentication()))
                .setUrlEncodingEnabled(httpSettings.isUrlEncoderEnabled())
                .log(LogDetail.ALL);
    }
}