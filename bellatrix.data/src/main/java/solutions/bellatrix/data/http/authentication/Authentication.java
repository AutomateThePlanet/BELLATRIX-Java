package solutions.bellatrix.data.http.authentication;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Authentication {
    @SerializedName("method")
    private String method;
    @SerializedName("options")
    @Getter private LinkedList<LinkedHashMap<String, Object>> authenticationOptions;

    @Setter(AccessLevel.PRIVATE)
    private transient AuthenticationMethod authenticationMethod;

    public AuthenticationMethod getAuthenticationMethod() {
        setAuthenticationMethod(AuthenticationMethod.parse(method));
        return authenticationMethod;
    }
}