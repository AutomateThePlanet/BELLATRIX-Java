package solutions.bellatrix.data.configuration;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import solutions.bellatrix.data.http.configuration.HttpSettings;

@Data
public class DataSettings {
    @SerializedName("dataSourceType")
    private String datasourceType;
    @SerializedName("httpSettings")
    private HttpSettings httpSettings;
}