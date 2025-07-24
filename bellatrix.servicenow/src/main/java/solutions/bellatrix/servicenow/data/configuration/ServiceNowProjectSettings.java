package solutions.bellatrix.servicenow.data.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceNowProjectSettings {
    private Boolean isPolarisEnabled;
    private String instance;
    private String userName;
    private String password;
}