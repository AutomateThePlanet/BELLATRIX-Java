package solutions.bellatrix.web.findstrategies;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.plugins.opencv.Base64Encodable;
import solutions.bellatrix.plugins.opencv.OpenCvService;
import solutions.bellatrix.plugins.opencv.OpenCvServiceSettings;
import solutions.bellatrix.web.services.JavaScriptService;

import java.util.List;

@Getter
public class ImageBase64FindStrategy extends FindStrategy {
    private final Base64Encodable encodedImage;
    private final boolean shouldGrayScale;
    private final double precisionThreshold;

    public ImageBase64FindStrategy(Base64Encodable encodedImage) {
        super(encodedImage.getBase64Image());
        this.encodedImage = encodedImage;
        var serviceSettings = ConfigurationService.get(OpenCvServiceSettings.class);
        if (serviceSettings == null) {
            serviceSettings = new OpenCvServiceSettings();
        }

        this.shouldGrayScale = serviceSettings.isShouldGrayscale();
        this.precisionThreshold = serviceSettings.getDefaultMatchThreshold();
    }

    public ImageBase64FindStrategy(Base64Encodable encodedImage, boolean shouldGrayScale, double precisionThreshold) {
        super(encodedImage.getBase64Image());
        this.shouldGrayScale = shouldGrayScale;
        this.precisionThreshold = precisionThreshold;
        this.encodedImage = encodedImage;
    }

    @Override
    public By convert() {
        return new ByImageBase64(encodedImage);
    }

    @Override
    public String toString() {
        return String.format("image base 64: %s", encodedImage.getImageName());
    }

    public static class ByImageBase64 extends By {
        private final Base64Encodable base64EncodedImage;

        public ByImageBase64(Base64Encodable base64EncodedImage) {
            this.base64EncodedImage = base64EncodedImage;
        }

        public static By byImageBase64(Base64Encodable encodedImage) {
            return new ByImageBase64(encodedImage);
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            var location = OpenCvService.getLocation(base64EncodedImage);
            Log.info("Coordinates located: %s", location.toString());
            return SingletonFactory.getInstance(JavaScriptService.class).<List<WebElement>>genericExecute("return document.elementsFromPoint(%s, %s);".formatted(location.x, location.y));
        }

        public String toString() {
            return "By.imageBase64: " + this.base64EncodedImage;
        }
    }
}