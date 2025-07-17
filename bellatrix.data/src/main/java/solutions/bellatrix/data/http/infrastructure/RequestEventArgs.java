package solutions.bellatrix.data.http.infrastructure;

import lombok.Getter;

public class RequestEventArgs {
    @Getter public final HttpContext requestConfiguration;
    @Getter public final Entity entity;

    public RequestEventArgs(HttpContext requestConfiguration, Entity entity) {
        this.requestConfiguration = requestConfiguration;
        this.entity = entity;
    }
}