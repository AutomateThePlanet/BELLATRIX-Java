package solutions.bellatrix.data.http.httpContext;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.data.http.authentication.AuthSchemaFactory;
import solutions.bellatrix.data.http.authentication.AuthenticationMethods;
import solutions.bellatrix.data.http.configuration.HttpSettings;
import solutions.bellatrix.data.http.configuration.HttpSettingsFactory;
import solutions.bellatrix.data.http.infrastructure.HTTPMethod;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HttpContext {
    private final HttpSettings httpSettings;
    private final LinkedList<String> pathParameters;
    private final LinkedHashMap<String, Object> queryParameters;
    @Setter
    private RequestSpecBuilder requestSpecBuilder;
    @Getter
    private String requestBody;
    @Getter private HTTPMethod httpMethod;

    public HttpContext(HttpSettings settings) {
        this.httpSettings = settings;
        this.pathParameters = new LinkedList<>();
        this.queryParameters = new LinkedHashMap<>();
        this.requestSpecBuilder = createInitialSpecBuilder(httpSettings);
    }

    public HttpSettings getHttpSettings() {
        return HttpSettingsFactory.createCopyOf(httpSettings);
    }

    public LinkedList<Object> getPathParameters() {
        return new LinkedList<>(pathParameters);
    }

    public LinkedHashSet<QueryParameter> getQueryParameters() {
        return new LinkedHashSet<>(queryParameters);
    }

    public RequestSpecBuilder getRequestSpecBuilder() {
        RequestSpecBuilder copy = new RequestSpecBuilder();
        copy.addRequestSpecification(requestSpecBuilder.build());
        copy.addQueryParams(getRequestQueryParameters());
        if (requestBody!=null) {
            copy.setBody(requestBody);
        }

        copy.setBasePath(buildRequestPath());

        return copy;
    }


    public RequestSpecification requestSpecification() {
        var requestSpecification = requestSpecBuilder.build();

        if (queryParameters!=null && !queryParameters.isEmpty()) {
            requestSpecification.queryParams(getRequestQueryParameters());
        }

        return requestSpecification;
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
        queryParameters.addAll(parameters);
    }

    public void addQueryParameter(QueryParameter parameter) {
        queryParameters.add(parameter);
    }

    private Map<String, String> getRequestQueryParameters() {
        LinkedHashMap<String, String> queryParams = new LinkedHashMap<>();
        if (httpSettings.getAuthentication().getAuthenticationMethod().equals(AuthenticationMethods.QUERY_PARAMETER)) {
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
            var x = pathParameters.stream().filter(p -> p!=null && !p.isEmpty()).collect(Collectors.joining("/"));
            return httpSettings.getBasePath() + "/%s".formatted(x);
        }

        return httpSettings.getBasePath();
    }

    public void updateRequestSpecification(Consumer<RequestSpecBuilder> specBuilderConsumer) {
        specBuilderConsumer.accept(requestSpecBuilder);
    }

    public void addRequestBody(String body) {
        this.requestBody = requestBody;
    }

    public void addRequestMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
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