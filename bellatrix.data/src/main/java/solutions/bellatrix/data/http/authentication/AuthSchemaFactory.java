package solutions.bellatrix.data.http.authentication;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.authentication.NoAuthScheme;
import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;

/**
 * Factory class to create authentication schemes based on the provided Authentication object.
 * It supports Basic, Bearer, and Query Parameter authentication methods.
 * This class is tightly coupled with res assured library for handling HTTP authentication schemes.
 */
public class AuthSchemaFactory {
    public static AuthenticationScheme getAuthenticationScheme(Authentication authentication) {
        var authType = authentication.getAuthenticationMethod();
        var option = authentication.getAuthenticationOptions().stream().filter(x -> x.get("type").equals(authType.getMethod())).findFirst();
        if (option.isEmpty()) {
            throw new IllegalArgumentException("Authentication type not found: %s, Supported types : ".formatted(authType));
        }

        switch (authType) {
            case BASIC -> {
                var basicAuth = option.get();
                String username = basicAuth.get("username").toString();
                String password = basicAuth.get("password").toString();
                var basicSchema = new BasicAuthScheme();
                basicSchema.setUserName(username);
                basicSchema.setPassword(password);
                return basicSchema;
            }
            case BEARER -> {
                var bearerAuth = option.get();
                String token = bearerAuth.get("token").toString();
                var bearerSchema = new PreemptiveOAuth2HeaderScheme();
                bearerSchema.setAccessToken(token);
                return bearerSchema;
            }
            case QUERY_PARAMETER -> {
                return new NoAuthScheme();
            }
            default -> throw new IllegalArgumentException("Unsupported authentication type: %s".formatted(authType));
        }
    }
}