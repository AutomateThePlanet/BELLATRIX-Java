package solutions.bellatrix.data.http.infrastructure;

import lombok.Getter;
import solutions.bellatrix.data.http.httpContext.HttpContext;

public class RequestEventArgs {
    @Getter public final HttpContext requestConfiguration;

    public RequestEventArgs(HttpContext requestConfiguration) {
        this.requestConfiguration = requestConfiguration;
    }
}