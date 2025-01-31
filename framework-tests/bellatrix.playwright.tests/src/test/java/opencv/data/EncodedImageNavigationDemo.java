package opencv.data;

import lombok.Getter;
import solutions.bellatrix.plugins.opencv.Base64Encodable;

public enum EncodedImageNavigationDemo implements Base64Encodable {
    /**
     * <img src={@value NavigationImageInBase64#homeLabel}/>
     */
    HOME_LABEL("homeLabel", NavigationImageInBase64.homeLabel, "https://demos.bellatrix.solutions/"),
    /**
     * <img src={@value NavigationImageInBase64#blogLabel}/>
     */
    BLOG_LABEL("blogLabel", NavigationImageInBase64.blogLabel, "/blog/"),
    /**
     * <img src={@value NavigationImageInBase64#checkoutLabel}/>
     */
    CHECKOUT_LABEL("checkoutLabel", NavigationImageInBase64.checkoutLabel, "/cart/"),
    /**
     * <img src={@value NavigationImageInBase64#contactFormLabel}/>
     */
    CONTACT_FORM_LABEL("contactFormLabel", NavigationImageInBase64.contactFormLabel, "/contact-form/"),
    /**
     * <img src={@value NavigationImageInBase64#myAccountLabel}/>
     */
    MY_ACCOUNT_LABEL("myAccountLabel", NavigationImageInBase64.myAccountLabel, "/my-account/"),
    /**
     * <img src={@value NavigationImageInBase64#promotionsLabel}/>
     */
    PROMOTIONS_LABEL("promotionsLabel", NavigationImageInBase64.promotionsLabel, "/welcome/");

    private final String imageName;
    private final String encodedImage;
    @Getter private final String expectedUrl;

    EncodedImageNavigationDemo(String imageName, String encodedImage, String expectedUrl) {
        this.imageName = imageName;
        this.encodedImage = encodedImage;
        this.expectedUrl = expectedUrl;
    }

    @Override
    public String getBase64Image() {
        return encodedImage;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
