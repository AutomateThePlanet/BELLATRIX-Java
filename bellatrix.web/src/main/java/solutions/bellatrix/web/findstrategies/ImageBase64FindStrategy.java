package solutions.bellatrix.web.findstrategies;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.plugins.opencv.Base64Encodable;

import java.util.List;

@Getter
public class ImageBase64FindStrategy extends FindStrategy {
    private final Base64Encodable encodedImage;

    public ImageBase64FindStrategy(Base64Encodable encodedImage) {
        super(encodedImage.getBase64Image());
        this.encodedImage = encodedImage;
    }

    @Override
    public By convert() {
        return new ByImageBase64(encodedImage.getBase64Image());
    }

    @Override
    public String toString() {
        return String.format("image base 64: %s", encodedImage.getBase64Image());
    }

    public static class ByImageBase64 extends By {
        private final String base64EncodedImage;

        public ByImageBase64(String base64EncodedImage) {
            this.base64EncodedImage = base64EncodedImage;
        }

        public static By byImageBase64(String encodedImage) {
            return new ByImageBase64(encodedImage);
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return null;
        }

        public String toString() {
            return "By.imageBase64: " + this.base64EncodedImage;
        }
    }
}