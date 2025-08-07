package solutions.bellatrix.data.http.configuration;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.authentication.Authentication;
import solutions.bellatrix.data.http.authentication.AuthenticationMethod;
import solutions.bellatrix.data.http.infrastructure.HttpHeader;

import java.util.LinkedHashSet;
import java.util.function.Consumer;

@Data
public class HttpSettings {
    @SerializedName("baseUrl")
    private String baseUrl;
    @SerializedName("basePath")
    private String basePath;
    @SerializedName("contentType")
    private String contentType;
    @SerializedName("authentication")
    private Authentication authentication;
    @SerializedName("urlEncoderEnabled")
    private boolean urlEncoderEnabled;
    @SerializedName("headers")
    private LinkedHashSet<HttpHeader> headers;

    public HttpSettings(HttpSettings httpSettings) {
        setBaseUrl(httpSettings.getBaseUrl());
        setBasePath(httpSettings.getBasePath());
        setContentType(httpSettings.getContentType());
        setUrlEncoderEnabled(httpSettings.isUrlEncoderEnabled());
        setAuthentication(httpSettings.getAuthentication());
        setHeaders(httpSettings.getHeaders());
    }

    public static HttpSettings custom(Consumer<HttpSettings> httpSettings) {
        var settings = ConfigurationService.get(DataSettings.class).getHttpSettings();
        if (settings == null) {
            throw new IllegalStateException("Include the httpSettings section in your config file");

        }
        httpSettings.accept(settings);
        return settings;
    }

    public AuthenticationMethod getAuthenticationMethod() {
        return authentication.getAuthenticationMethod();
    }
}