package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.web.components.WebComponent;

public class DevExtremeDisabledChecker {
    private static boolean hasAriaDisabledAncestorOrSelf(WebComponent webComponent) {
        var xpathLocator = "./ancestor-or-self::*[@aria-disabled='true']";
        var disabledAncestor = webComponent.createAllByXPath(WebComponent.class, xpathLocator);

        return disabledAncestor.size() > 0;
    }

    private static boolean hasClassDisabledAncestorOrSelf(WebComponent webComponent) {
        var xpathLocator = "./ancestor-or-self::*[contains(concat(' ',normalize-space(@class),' '),' dx-state-disabled ')]";
        var disabledAncestor = webComponent.createAllByXPath(WebComponent.class, xpathLocator);
        return disabledAncestor.size() > 0;
    }

    public static boolean isDisabled(WebComponent webComponent) {
        return hasClassDisabledAncestorOrSelf(webComponent) || hasAriaDisabledAncestorOrSelf(webComponent);
    }
}