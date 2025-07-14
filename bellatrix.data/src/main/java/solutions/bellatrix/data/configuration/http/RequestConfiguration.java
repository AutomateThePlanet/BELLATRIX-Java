package solutions.bellatrix.data.configuration.http;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.data.configuration.http.auth.AuthSchemaFactory;
import solutions.bellatrix.data.configuration.http.auth.AuthenticationMethods;
import solutions.bellatrix.data.configuration.http.urlBuilder.QueryParameter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RequestConfiguration {
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private final HttpSettings httpSettings;
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private final LinkedList<String> pathParameters;
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private final LinkedList<QueryParameter> queryParameters;
    private final RequestSpecBuilder requestSpecBuilder;

    public RequestConfiguration(HttpSettings settings) {
        this.httpSettings = settings;
        this.pathParameters = new LinkedList<>();
        this.queryParameters = new LinkedList<>();
        this.requestSpecBuilder = createRequestSpecificationBuilder(httpSettings);
    }

    public void addPathParameter(String param) {
        pathParameters.add(param);
    }

    public void addPathParameters(Collection<String> params) {
        pathParameters.addAll(params);
    }

    public void addQueryParameters(Collection<QueryParameter> parameters) {
        queryParameters.addAll(parameters);
    }

    public void addQueryParameter(QueryParameter parameter) {
        queryParameters.add(parameter);
    }

    public RequestSpecification requestSpecification() {
        return requestSpecBuilder.build();
    }

    public Map<String, String> getRequestQueryParameters() {
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
                        //   queryParams.put(key, QueryParameter.of(key, option.get(key).toString()));
                    }
                }

            }
            return queryParams;

            // return queryParams;
        }

//        for (QueryParameter queryParameter : queryParameters) {
//            queryParams.put(queryParameter.getKey(), queryParameter.getValue());
//        }

        return null;
    }

    public String getRequestPath() {
        return "/" + pathParameters.stream()
                .filter(p -> p!=null && !p.isEmpty())
                .collect(Collectors.joining("/"));
    }

    public void customizeRequest(Consumer<RequestSpecBuilder> specBuilderConsumer) {
        specBuilderConsumer.accept(requestSpecBuilder);
    }

    private RequestSpecBuilder createRequestSpecificationBuilder(HttpSettings httpSettings) {
        var builder = new RequestSpecBuilder();
        builder.setBasePath(httpSettings.getBasePath());
        builder.setBaseUri(httpSettings.getBaseUrl());
        builder.setContentType(httpSettings.getContentType());
        builder.setAuth(AuthSchemaFactory.getAuthenticationScheme(httpSettings.getAuthentication()));
        builder.setUrlEncodingEnabled(httpSettings.isUrlEncoderEnabled());
        builder.log(LogDetail.ALL);
        return builder;
    }
}