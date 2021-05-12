package solutions.bellatrix.web.services;

import lombok.Getter;

public class WebServiceEventArgs {
    @Getter private final WebService service;
    @Getter private final String actionValue;
    @Getter private final String message;

    public WebServiceEventArgs(WebService service, String actionValue, String message) {
        this.service = service;
        this.actionValue = actionValue;
        this.message = message;
    }

    public WebServiceEventArgs(WebService service, String actionValue) {
        this.service = service;
        this.actionValue = actionValue;
        message = "";
    }

    public WebServiceEventArgs(WebService service) {
        this.service = service;
        this.actionValue = "";
        message = "";
    }
}
