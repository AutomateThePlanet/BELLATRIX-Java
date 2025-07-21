package solutions.bellatrix.data.http.infrastructure.events;

import lombok.Getter;
import solutions.bellatrix.data.http.httpContext.HttpContext;

public class HttpRequestEventArgs {
    @Getter public final HttpContext requestConfiguration;

    public HttpRequestEventArgs(HttpContext requestConfiguration) {
        this.requestConfiguration = requestConfiguration;
    }
}