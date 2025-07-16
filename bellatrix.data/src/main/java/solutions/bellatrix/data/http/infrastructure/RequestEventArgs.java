package solutions.bellatrix.data.http.infrastructure;

import lombok.Getter;

public class RequestEventArgs {
    @Getter public RequestConfiguration component;

    public RequestEventArgs(RequestConfiguration component) {
        this.component = component;
    }
}