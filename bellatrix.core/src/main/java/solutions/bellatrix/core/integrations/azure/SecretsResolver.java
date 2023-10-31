package solutions.bellatrix.core.integrations.azure;

import java.util.function.Supplier;

public class SecretsResolver {
    public static String getSecret(Supplier<String> getConfigValue) {
        String configValue = getConfigValue.get();
        if (configValue.startsWith("env_")) {
            String environmentalVariable = System.getenv(configValue.replace("env_", ""));
            return environmentalVariable;
        } else if (configValue.startsWith("vault_")) {
            String keyVaultValue = KeyVault.getSecret(configValue.replace("vault_", ""));
            return keyVaultValue;
        } else {
            return configValue;
        }
    }

    public static String getSecret(String name) {
        String environmentalVariable = System.getenv(name);
        if (environmentalVariable != null && !environmentalVariable.isEmpty()) {
            return environmentalVariable;
        } else if (KeyVault.isAvailable) {
            String keyVaultValue = KeyVault.getSecret(name);
            return keyVaultValue;
        } else {
            throw new IllegalArgumentException("You need to initialize an environmental variable or key vault secret first.");
        }
    }
}