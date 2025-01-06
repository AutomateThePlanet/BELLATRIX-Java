package solutions.bellatrix.playwright.findstrategies;

import com.microsoft.playwright.Page;
import lombok.Getter;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.plugins.opencv.Base64Encodable;

@Getter
public class ImageBase64FindStrategy extends FindStrategy {
    private final Base64Encodable encodedImage;

    public ImageBase64FindStrategy(Base64Encodable encodedImage) {
        this.encodedImage = encodedImage;
    }

    @Override
    public WebElement convert(Page page) {
        return null;
    }

    @Override
    public WebElement convert(WebElement locator) {
        return null;
    }
}
