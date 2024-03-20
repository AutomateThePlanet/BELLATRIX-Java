package O12_page_objects.main_page;

import solutions.bellatrix.playwright.components.Button;
import solutions.bellatrix.playwright.pages.PageMap;

public class Map extends PageMap {
    public Button getProductAddToCartButtonByName(String name) {
        return create().byXpath(Button.class, String.format("//h2[text()='%s']/parent::a[1]/following-sibling::a[1]", name));
    }

    public Button viewCartButton() {
        return create().byCss(Button.class, "[class*='added_to_cart wc-forward']");
    }
}