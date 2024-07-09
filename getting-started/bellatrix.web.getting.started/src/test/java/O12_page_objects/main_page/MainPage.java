package O12_page_objects.main_page;

import solutions.bellatrix.web.pages.WebPage;

public class MainPage extends WebPage<Map, Asserts> {
    @Override
    protected String getUrl() {
        return "http://demos.bellatrix.solutions/";
    }
    public void addRocketToShoppingCart(String rocketName) {
        open();
        map().getProductAddToCartButtonByName(rocketName).click();
        map().viewCartButton().click();
    }
}
