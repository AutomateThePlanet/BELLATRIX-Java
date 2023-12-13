package solutions.bellatrix.core.integrations.azure;

import lombok.Data;

@Data
public class KeyVaultSettings {
    private String keyVaultEndpoint;
    private boolean isEnabled;
}