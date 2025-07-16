package solutions.bellatrix.data.http.configuration;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.authentication.Authentication;

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

    public static HttpSettings getDefaultHttpSettings() {
        return ConfigurationService.get(DataSettings.class).getHttpSettings();
    }

    public static HttpSettings extendBasePath(String basePath) {
        return HttpSettings.createCustomHttpSettings(x -> x.setBasePath(basePath));
    }

    public static HttpSettings createCustomHttpSettings(Consumer<HttpSettings> httpSettings) {
        var settings = getDefaultHttpSettings();

        httpSettings.accept(settings);

        return settings;
    }
}