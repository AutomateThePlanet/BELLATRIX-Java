package solutions.bellatrix.data.configuration.http.urlBuilder;

import lombok.Getter;
import solutions.bellatrix.data.configuration.http.HttpSettings;
import solutions.bellatrix.data.configuration.http.auth.AuthenticationMethods;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UrlBuilder {
    @Getter
    private final LinkedList<String> pathParameters;
    @Getter
    private final LinkedList<QueryParameter> queryParameters;
    private final HttpSettings httpSettings;
    @Getter
    private String basePath;

    public UrlBuilder(HttpSettings httpSettings) {
        this.httpSettings = httpSettings;
        this.basePath = buildBasePathFromSettings();
        this.queryParameters = new LinkedList<>();
        pathParameters = new LinkedList<>();
    }


    public void addPathSegments(Collection<String> params) {
        pathParameters.addAll(params);
    }

    public void addQueryParameters(Collection<QueryParameter> parameters) {
        queryParameters.addAll(parameters);
    }

    public String build() {
        return "%s%s%s".formatted(httpSettings.getBaseUrl(), path(), queryString());
    }

    private String queryString() {
        //check if there is auth query parameters to add
        addAuthenticationQueryParameters(httpSettings);


        //if there are no query parameter, custom defined, or added by authentication, return empty string
        if (queryParameters.isEmpty()) {
            return "";
        }

        StringBuilder queryStringBuilder = new StringBuilder("?");
        for (int index = 0; index < queryParameters.size() - 1; index++) {
            queryStringBuilder.append("%s&".formatted(queryParameters.get(index).toString()));
        }
        queryStringBuilder.append("%s".formatted(queryParameters.get(queryParameters.size() - 1).toString()));

        return urlEncode(queryStringBuilder.toString());
    }

    private void addAuthenticationQueryParameters(HttpSettings httpSettings) {
        if (httpSettings.getAuthentication().getAuthenticationMethod()==AuthenticationMethods.QUERY_PARAMETER) {
            var option = httpSettings.getAuthentication().getAuthenticationOptions().stream().filter(x -> x.get("type").equals("QueryParameters")).findFirst().get();
            String insertionOrder = (String)option.get("insertionOrder");
            for (var key : option.keySet()) {
                if (!key.equals("type") && !key.equals("insertionOrder")) {
                    if (insertionOrder.equals("start")) {
                        queryParameters.add(QueryParameter.of(key, option.get(key).toString()));
                    } else {
                        queryParameters.add(queryParameters.size(), QueryParameter.of(key, option.get(key).toString()));
                    }
                }
            }
        }
    }

    private String urlEncode(String value) {
        if (httpSettings.isUrlEncoderEnabled()) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }

        return value;
    }

    public Map<String, Object> getQueryParameters() {
        Map<String, Object> queryParams = new HashMap<>();
        for (QueryParameter queryParameter : queryParameters) {
            queryParams.put(queryParameter.getKey(), queryParameter.getValue());
        }
        return queryParams;
    }

    public String path() {
        List<String> arrayList = new ArrayList<>(List.of(basePath));
        arrayList.addAll(pathParameters);

        return String.join("/", arrayList.toArray(new String[0]));
    }


    private String buildBasePathFromSettings() {
        if (httpSettings.getBasePath()==null || httpSettings.getBasePath().isEmpty()) {
            basePath = "/";
        }

        if ("/".equals(httpSettings.getBasePath())) {
            return httpSettings.getResourcePath().startsWith("/") ? httpSettings.getResourcePath():"/" + httpSettings.getResourcePath();
        }

        String normalizedBase = httpSettings.getBasePath().endsWith("/")
                ? httpSettings.getBasePath().substring(0, httpSettings.getBasePath().length() - 1)
                :httpSettings.getBasePath();

        String normalizedResource = httpSettings.getResourcePath().startsWith("/")
                ? httpSettings.getResourcePath()
                :"/" + httpSettings.getResourcePath();

        return normalizedBase + normalizedResource;
    }


    public void reset() {
        this.pathParameters.clear();
        this.queryParameters.clear();
    }
}