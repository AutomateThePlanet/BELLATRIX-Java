package solutions.bellatrix.core.integrations.azure;

import lombok.Getter;
import lombok.Setter;

public class KeyVaultSettings {
    @Getter @Setter private String keyVaultEndpoint;
    @Getter @Setter private boolean isEnabled;
}