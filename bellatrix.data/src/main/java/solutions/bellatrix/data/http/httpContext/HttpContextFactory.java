package solutions.bellatrix.data.http.httpContext;

import solutions.bellatrix.data.http.configuration.HttpSettings;

public class HttpContextFactory {
    public static HttpContext createDefault(HttpSettings httpSettings) {
        return new HttpContext(httpSettings);
    }

    public static HttpContext copyOf(HttpContext original) {
        HttpSettings clonedSettings = original.getHttpSettings();
        HttpContext copy = new HttpContext(clonedSettings);
        copy.addPathParameters(original.getPathParameters());
        copy.addQueryParameters(original.getQueryParameters());
        copy.setRequestSpecBuilder(original.getRequestSpecBuilder());
        return copy;
    }
}