package solutions.bellatrix.data.http.configuration.events;

import lombok.Getter;
import solutions.bellatrix.data.configuration.http.RequestConfiguration;

public class RequestEventArgs {
    @Getter public RequestConfiguration component;

    public RequestEventArgs(RequestConfiguration component) {
        this.component = component;
    }
}