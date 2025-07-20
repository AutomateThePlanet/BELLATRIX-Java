package solutions.bellatrix.data.http.configuration;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;

import java.util.function.Consumer;

public class HttpSettingsFactory {
    public static HttpSettings createDefault() {
        return ConfigurationService.get(DataSettings.class).getHttpSettings();
    }

    public static HttpSettings createCopyOf(HttpSettings httpSettings) {
        HttpSettings newSettings = new HttpSettings();
        newSettings.setBaseUrl(httpSettings.getBaseUrl());
        newSettings.setBasePath(httpSettings.getBasePath());
        newSettings.setContentType(httpSettings.getContentType());
        newSettings.setUrlEncoderEnabled(httpSettings.isUrlEncoderEnabled());
        newSettings.setAuthentication(httpSettings.getAuthentication());
        return newSettings;
    }

    /**
     * Creates a new HttpSettings instance with the default settings and applies the customizations provided by the consumer.
     * Provide only properties that differ from the default settings.
     *
     * @param httpSettings A consumer that accepts an HttpSettings instance for customization.
     * @return A new HttpSettings instance with the applied customizations.
     */
    public static HttpSettings createWithCustomSettings(Consumer<HttpSettings> httpSettings) {
        var settings = createDefault();

        httpSettings.accept(settings);

        return settings;
    }
}