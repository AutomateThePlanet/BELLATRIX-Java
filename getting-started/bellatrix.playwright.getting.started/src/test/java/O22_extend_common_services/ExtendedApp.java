package O22_extend_common_services;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.services.App;

public class ExtendedApp extends App {
    // A way to extend the BELLATRIX common services is to create a class extending the service for the additional action
    // and create a class, extending the App, pointing to the new extended service, then override the app method in the test.
    @Override
    public ExtendedNavigationService navigate() {
        return SingletonFactory.getInstance(ExtendedNavigationService.class);
    }
}
