package O22_extend_common_services;

import solutions.bellatrix.playwright.services.JavaScriptService;
import solutions.bellatrix.playwright.services.NavigationService;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ExtendedNavigationService extends NavigationService {
    public void viaJavaScript(String url) {
        var javaScriptService = new JavaScriptService();

        if (!isUrlValid(url)) {
            throw new IllegalArgumentException(String.format("The specified URL – %s is not in a valid format!", url));
        }

        javaScriptService.execute(String.format("window.location.href = '%s';", url));
    }

    public boolean isUrlValid(String url) {
        try {
            URL obj = new URL(url);
            obj.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}