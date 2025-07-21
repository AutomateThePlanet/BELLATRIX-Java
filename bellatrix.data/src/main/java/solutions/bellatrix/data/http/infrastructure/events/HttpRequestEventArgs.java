package solutions.bellatrix.data.http.infrastructure.events;

import lombok.Getter;
import solutions.bellatrix.data.http.httpContext.HttpContext;

@Getter
public class HttpRequestEventArgs {
    public final HttpContext requestConfiguration;

    public HttpRequestEventArgs(HttpContext requestConfiguration) {
        this.requestConfiguration = requestConfiguration;
    }
}