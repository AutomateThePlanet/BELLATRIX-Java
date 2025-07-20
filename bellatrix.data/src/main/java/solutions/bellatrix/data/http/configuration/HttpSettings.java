package solutions.bellatrix.data.http.configuration;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import solutions.bellatrix.data.http.authentication.Authentication;

@Data
public class HttpSettings {
    @SerializedName("baseUrl")
    private String baseUrl;
    @SerializedName("basePath")
    private String basePath;
    @SerializedName("contentType")
    private String contentType;
    @SerializedName("authentication")
    private Authentication authentication;
    @SerializedName("urlEncoderEnabled")
    private boolean urlEncoderEnabled;
}