package O24_iframes_and_shadow_dom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class IFramesAndShadowDOMTests extends WebTest {
    @Test
    public void testFindingIFramesOnThePage() {
        app().navigate().to("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml_iframe");
        app().create().byId(Button.class, "accept-choices").click();

        // If you create a Frame component, it automatically will start searching for elements inside an <iframe> html element
        // You can work with iframes in bellatrix.playwright module as if they were any other component
        var parentIFrame = app().create().byXpath(Frame.class, "//iframe[@id='iframeResult']");

        // This <iframe> is located inside the parentIFrame
        var iframe = parentIFrame.create().byXpath(Frame.class, "//iframe[@src='https://www.w3schools.com']");

        // This button is located inside iframe
        iframe.create().byXpath(Button.class, "//div[@id='accept-choices']").click();

        // In case you want to reuse the component locating logic, but stop looking inside the <iframe>, you can use the as() method;
        // It will automatically switch to using normal locating strategies and you will stop being able to pierce the <iframe>
        var iframeAsDiv = iframe.as(Div.class);
        iframeAsDiv.create().byXpath(Heading.class, "//preceding-sibling::h1").validateTextIs("The iframe element");

        // Again, if you create a 'normal' component and want to reuse it, this time to search inside it, as if it is <iframe>, you use as() method again
        var iframeAsNormalComponent = parentIFrame.create().byXpath(Div.class, "//iframe[@src='https://www.w3schools.com']");
        var iframeAsFrame = iframeAsNormalComponent.as(Frame.class);

        Assertions.assertEquals(iframeAsFrame.create().byXpath(Div.class, "//div[@id='subtopnav']//a[@title='HTML Tutorial']").getText(), "HTML");
    }

    @Test
    public void testFindingElementsInShadowDOM() {
        app().navigate().to("https://web.dev/articles/shadowdom-v1");
        var iframe = app().create().byXpath(Frame.class, "//iframe[contains(@src, 'raw/fancy-tabs-demo.html')]");
        iframe.scrollToVisible();

        // As you might know, when working with shadowRoot, we can use only CSS locators, which might prove difficult
        // When we want to find an element relative to another or an element by its inner text.
        // BELLATRIX offers a solution to this problem.

        // Not going into detail, this works in the following way:
        // 1. Find the shadow root
        // 2. Get its inner HTML
        // 3. Get the element using jsoup to navigate inside it with xpath
        // 4. Get its absolute position in the form of an absolute xpath
        // 5. Convert the absolute xpath into CSS
        // 6. Use this CSS locator to find the actual element on the page

        // Let's test a simply shadow DOM:
        // <div id="tabs">
        //   <slot id="tabsSlot" name="title"></slot>
        // </div>
        // <div id="panels">
        //   <slot id="panelsSlot"></slot>
        // </div>

        // To get inside the shadowRoot, we first have to locate its host
        // And then we use the method getShadowRoot(), which will return a ShadowRoot component
        var shadowRoot = iframe.create().byXpath(Div.class, "//fancy-tabs").getShadowRoot();


        // Absolute xpath: /div[1]/slot
        // CSS locator: div:nth-child(1)/slot
        var elementInShadow = shadowRoot.create().byXpath(Div.class, "//div[@id='tabs']//slot[@id='tabsSlot']");

        // What if we wanted to go back in the DOM?
        // Absolute xpath: /div[1]
        // CSS locator: div:nth-child(1)
        var parentInShadow = elementInShadow.create().byXpath(Div.class, "//parent::div");

        System.out.println(elementInShadow.getFindStrategy().toString());

        // The same as elementInShadow...
        // Absolute xpath: /div[1]/slot
        // CSS locator: div:nth-child(1)/slot
        var slot = parentInShadow.create().byXpath(Div.class, "//slot");

        Assertions.assertEquals(slot.getAttribute("name"), "title");

        // More complex xpath will also work the same way.
        // Of course, the test here doesn't make much sense, but it is a showcase of
        // what you can do with BELLATRIX regarding shadow DOM:
        // - Use xpath to locate elements inside the shadow DOM
        // - Go 'back' in the DOM tree (something you cannot achieve with CSS)
        // - Locate elements by their inner text (something you cannot achieve with CSS)
    }
}