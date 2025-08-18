package solutions.bellatrix.plugins.opencv;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class OpenCvServiceSettings {
    @SerializedName("defaultMatchThreshold")
    private double defaultMatchThreshold = 0.8;
    @SerializedName("shouldGrayscale")
    private boolean shouldGrayscale = true;
}