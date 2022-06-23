package solutions.bellatrix.core.utilities;

import java.util.function.Supplier;

public class SecretsResolver {
    public static String getSecret(Supplier<String> getConfigValue) {
        if (getConfigValue.get().contains("env_"))
        {
            String envName = getConfigValue.get().replace("{env_", "").replace("}", "");
            String environmentalVariable = System.getenv(envName);
            return environmentalVariable;
        }
        else
        {
            return getConfigValue.get();
        }
    }

    public static String getSecret(String configValue) {
        if (configValue.contains("env_"))
        {
            String envName = configValue.replace("{env_", "").replace("}", "");
            String environmentalVariable = System.getenv(envName);
            return environmentalVariable;
        }
        else
        {
            return configValue;
        }
    }
}