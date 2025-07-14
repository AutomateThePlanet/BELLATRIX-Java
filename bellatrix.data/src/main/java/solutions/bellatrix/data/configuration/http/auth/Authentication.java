package solutions.bellatrix.data.configuration.http.auth;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@Data
public class Authentication {
    @SerializedName("method")
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private String method;
    @Setter(AccessLevel.PRIVATE)
    @SerializedName("options")
    private LinkedList<LinkedHashMap<String, Object>> authenticationOptions;
    
    @Setter(AccessLevel.PRIVATE)
    private transient AuthenticationMethods authenticationMethod;

    public AuthenticationMethods getAuthenticationMethod() {
        setAuthenticationMethod(AuthenticationMethods.parse(getMethod()));
        return authenticationMethod;
    }
}