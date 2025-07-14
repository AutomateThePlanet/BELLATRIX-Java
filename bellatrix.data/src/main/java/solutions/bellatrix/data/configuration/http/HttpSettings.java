package solutions.bellatrix.data.configuration.http;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.configuration.http.auth.Authentication;

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

    private transient String resourcePath;

    public static HttpSettings getDefaultHttpSettings() {
        return ConfigurationService.get(DataSettings.class).getHttpSettings();
    }

    public static HttpSettings createCustomHttpSettings(Consumer<HttpSettings> httpSettings) {
        var settings = getDefaultHttpSettings();

        httpSettings.accept(settings);

        return settings;
    }
}