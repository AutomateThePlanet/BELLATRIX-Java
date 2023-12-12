package solutions.bellatrix.core.integrations.azure;

import lombok.Data;

//@Data
public class KeyVaultSettings {
    private String keyVaultEndpoint;
    private boolean isEnabled;

    public String getKeyVaultEndpoint() {
        return keyVaultEndpoint;
    }

    public void setKeyVaultEndpoint(String keyVaultEndpoint) {
        this.keyVaultEndpoint = keyVaultEndpoint;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}