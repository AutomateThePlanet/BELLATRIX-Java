package O13_layout_testing;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.BrowserChoice;
import solutions.bellatrix.playwright.infrastructure.ExecutionBrowser;
import solutions.bellatrix.playwright.infrastructure.Lifecycle;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

// Layout testing is a module from BELLATRIX that allows you to test the responsiveness of your website.
@ExecutionBrowser (browser = BrowserChoice.CHROME, width = 1280, height = 1024, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class LayoutTestingTests extends WebTest {
    @Test
    public void testPageLayout() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEndingWith(Select.class, "orderby");
        Anchor protonRocketAnchor =
                app().create().byAttributeContaining(Anchor.class, "href", "/proton-rocket/");
        Anchor protonMAnchor =
                app().create().byAttributeContaining(Anchor.class, "href", "/proton-m/");
        Anchor saturnVAnchor =
                app().create().byAttributeContaining(Anchor.class, "href", "/saturn-v/");
        Anchor falconHeavyAnchor =
                app().create().byAttributeContaining(Anchor.class, "href", "/falcon-heavy/");
        Anchor falcon9Anchor =
                app().create().byAttributeContaining(Anchor.class, "href", "/falcon-9/");
        Div saturnVRating = saturnVAnchor.createByClassContaining(Div.class, "star-rating");

        // About 50 combinations of assertion methods are available to you to check the exact position of your web elements
        // using the layout validation builder. Browser attribute gives you the option to resize your browser window so
        // that you can test the rearrangement of the web elements on your pages.
        sortDropDown.above(protonRocketAnchor).validate();

        sortDropDown.above(protonRocketAnchor).equal(41).validate();

        // For each available method the validation builder also has comparisons such as >, >=, <, <=.
        sortDropDown.above(protonRocketAnchor).greaterThan(40).validate();
        sortDropDown.above(protonRocketAnchor).greaterThanOrEqual(41).validate();
        sortDropDown.above(protonRocketAnchor).lessThan(50).validate();
        sortDropDown.above(protonRocketAnchor).lessThanOrEqual(43).validate();

        saturnVAnchor.right(sortDropDown).validate();
        sortDropDown.left(saturnVAnchor).validate();

        // You can test whether different web elements are aligned correctly.
        protonRocketAnchor.alignedHorizontallyAll(protonMAnchor).validate();
        protonRocketAnchor.alignedHorizontallyTop(protonMAnchor, saturnVAnchor).validate();
        protonRocketAnchor.alignedHorizontallyCentered(protonMAnchor, saturnVAnchor).validate();
        protonRocketAnchor.alignedHorizontallyBottom(protonMAnchor, saturnVAnchor).validate();

        falcon9Anchor.alignedVerticallyAll(falconHeavyAnchor).validate();
        // You can pass as many elements as you like.
        falcon9Anchor.alignedVerticallyLeft(falconHeavyAnchor).validate();
        falcon9Anchor.alignedVerticallyCentered(falconHeavyAnchor).validate();
        falcon9Anchor.alignedVerticallyRight(falconHeavyAnchor).validate();

        saturnVRating.inside(saturnVAnchor).validate();

        saturnVRating.height().lessThan(100).validate();
        saturnVRating.width().greaterThan(50).validate();
        saturnVRating.width().lessThan(70).validate();
        // Verify the height and width of elements.

        saturnVRating.inside(SpecialComponents.getViewport());
        saturnVRating.inside(SpecialComponents.getScreen());
        // Screen - represents the whole page area inside browser even that which is not visible.
        // Viewport - it takes the browsers client window. It is useful if you want to check some fixed element on the
        //
        // screen which sticks to viewport even when you scroll.
    }
}