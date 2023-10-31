package solutions.bellatrix.core.integrations.azure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KeyVaultSettings {
    private String keyVaultEndpoint;
    private boolean isEnabled;
}