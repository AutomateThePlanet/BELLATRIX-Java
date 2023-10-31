package solutions.bellatrix.core.integrations.azure;

import com.azure.identity.ChainedTokenCredential;
import com.azure.identity.ChainedTokenCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import solutions.bellatrix.core.configuration.ConfigurationService;

public class KeyVault {
    private static SecretClient secretClient;

    static {
        initializeClient();
    }

    public static boolean isAvailable = secretClient != null;

    public static String getSecret(String name) {
        if (secretClient == null) {
            return null;
        }

        KeyVaultSecret secret = secretClient.getSecret(name);
        return secret.getValue();
    }

    private static void initializeClient() {
        if (secretClient == null) {

            KeyVaultSettings settings = ConfigurationService.get(KeyVaultSettings.class);
            if (settings.isEnabled() && !settings.getKeyVaultEndpoint().isEmpty()) {
            var url = settings.getKeyVaultEndpoint();
                secretClient = new SecretClientBuilder()
                        .vaultUrl(settings.getKeyVaultEndpoint())
                        .credential(new DefaultAzureCredentialBuilder().build())
                        .buildClient();
            }
        }
    }
}
