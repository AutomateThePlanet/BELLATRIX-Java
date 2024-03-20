package O4_locate_components;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class LocateElementsTests extends WebTest {
    @Test
    public void promotionsPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        var promotionsLink = app().create().byLinkText(Anchor.class, "Promotions");
        //  There are different ways to locate components on the page. To do it you use the component create service.
        //  You need to know that BELLATRIX has a built-in complex mechanism for waiting for components, so you do not
        //  need to worry about this anymore. Keep in mind that when you use the create methods, the component is not
        //  searched on the page. All components use lazy loading. Which means that they are searched once you perform
        //  an action or assertion on them. By default, on each new action, the component is searched again and is refreshed.

        promotionsLink.click();

        System.out.println(promotionsLink.findStrategy().getValue());

        System.out.println(promotionsLink.getTagName());

        //  You can access the Playwright wrapped element through wrappedElement and the current Playwright and Browser instance
        //  through wrappedDriver.
    }

    @Test
    public void blogPageOpened_When_PromotionsButtonClicked() {
        app().navigate().to("http://demos.bellatrix.solutions/");
        //  BELLATRIX extends the vanilla Playwright selectors and give you additional ones.
        //  CreateByIdEndingWith  --> app().create().byIdEndingWith(Anchor.class, "myIdSuffix");
        //  Searches the component by ID ending with the locator.

        //  CreateByTag   -->  app().create().byTag(Anchor.class, "a");
        //  Searches the component by its tag.

        //  CreateById   -->  app().create().byId(Button.class, "myId");
        //  Searches the component by its ID.

        //  CreateByIdContaining   -->  app().create().byIdContaining(Button.class, "myIdMiddle");
        //  Searches the component by ID containing the specified text.

        //  CreateByValueEndingWith   -->  app().create().byValueContaining(Button.class, "pay");
        //  Searches the component by value attribute containing the specified text.

        //  CreateByXpath   -->  app().create().byXpath(Button.class, "//*[@title='Add to cart']");
        //  Searches the component by Xpath locator.

        //  CreateByLinkText   -->  app().create().byLinkText(Anchor.class, "blog");
        //  Searches the component by its link (href)

        //  CreateByLinkTextContaining   -->  app().create().byLinkTextContaining(Anchor.class, "account");
        //  Searches the component by its link (href) if it contains specified value.

        //  CreateByClass   -->  app().create().byClass(Anchor.class, "product selected");
        //  Searches the component by its CSS classes.

        //  CreateByClassContaining   -->  app().create().byClassContaining(Anchor.class, "selected");
        //  Searches the component by its CSS classes containing the specified values.

        //  CreateByTextContaining   -->  app().create().byTextContaining(Div.class, "Showing all");
        //  Searches the component by its inner text content, including all child HTML elements.

        //  CreateByNameEndingWith   -->  app().create().byNameEndingWith(SearchField.class, "a");
        //  Searches the component by its name containing the specified text.

        //  CreateByAttributesContaining   -->  app().create().byAttributeContaining(Anchor.class, "data-product_id", "31");
        //  Searches the component  by some of its attribute containing the specifed value.
        var blogLink = app().create().byLinkText(Anchor.class, "Blog");

        blogLink.click();
    }

    @Test
    public void checkAllAddToCartButtons() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        // Sometimes we need to find more than one component. For example, in this test we want to locate all Add to Cart buttons.
        // To do it you can use the allByXpath.
        // Available create methods:

        // CreateAllByIdEndingWith  --> app().create().allByIdEndingWith(Anchor.class, "myIdSuffix");
        // Searches the components by ID ending with the locator.

        // CreateAllByTag   -->  app().create().allByTag(Anchor.class, "a");
        // Searches the components by its tag.

        // CreateAllById   -->  app().create().allById(Button.class, "myId");
        // Searches the components by its ID.

        // CreateAllByIdContaining   -->  app().create().allByIdContaining(Button.class, "myIdMiddle");
        // Searches the components by ID containing the specified text.

        // CreateAllByValueEndingWith   -->  app().create().allByValueContaining(Button.class, "pay");
        // Searches the components by value attribute containing the specified text.

        // CreateAllByXpath   -->  app().create().allByXpath(Button.class, "//*[@title='Add to cart']");
        // Searches the components by Xpath locator.

        // CreateAllByLinkText   -->  app().create().allByLinkText(Anchor.class, "blog");
        // Searches the components by its link (href)

        // CreateAllByLinkTextContaining   -->  app().create().allByLinkTextContaining(Anchor.class, "account");
        // Searches the components by its link (href) if it contains specified value.

        // CreateAllByClass   -->  app().create().allByClass(Anchor.class, "product selected");
        // Searches the components by its CSS classes.

        // CreateAllByClassContaining   -->  app().create().allByClassContaining(Anchor.class, "selected");
        // Searches the components by its CSS classes containing the specified values.

        // CreateAllByTextContaining   -->  app().create().allByTextContaining(Div.class, "Showing all");
        // Searches the components by its inner text content, including all child HTML elements.

        // CreateAllByNameEndingWith   -->  app().create().allByNameEndingWith(SearchField.class, "a");
        // Searches the components by its name containing the specified text.

        // CreateAllByAttributesContaining   -->  app().create().allByAttributeContaining(Anchor.class, "data-product_id", "31");
        // Searches the components by some of its attribute containing the specifed value.

        var blogLink = app().create().allByXpath(Anchor.class, "//*[@title='Add to cart']");
    }

    // TODO: Find how to use nested components
}