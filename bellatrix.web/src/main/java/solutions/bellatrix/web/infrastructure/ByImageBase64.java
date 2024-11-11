package solutions.bellatrix.web.infrastructure;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ByImageBase64 extends By {
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