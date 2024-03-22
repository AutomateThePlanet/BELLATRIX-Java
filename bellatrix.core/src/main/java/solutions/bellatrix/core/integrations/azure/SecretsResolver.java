package solutions.bellatrix.core.integrations.azure;

import java.util.function.Supplier;

public class SecretsResolver {
    public static String getSecret(Supplier<String> getConfigValue) {
        String configValue = getConfigValue.get();

        if (configValue.startsWith("env_")) {
            return System.getenv(configValue.replace("env_", ""));
        }

        return configValue;
    }

    public static String getSecret(String name) {

        String environmentalVariable = System.getenv(name);

        if (environmentalVariable == null || environmentalVariable.isEmpty()) {
            throw new IllegalArgumentException("You need to initialize an environmental variable or key vault secret first.");
        }

        return environmentalVariable;
    }
}