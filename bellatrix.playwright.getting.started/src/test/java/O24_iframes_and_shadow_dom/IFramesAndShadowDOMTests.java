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
        iframeAsDiv.create().byXpath(Heading.class, "//preceding-sibling::h1").validateInnerTextIs("The iframe element");

        // Again, if you create a 'normal' component and want to reuse it, this time to search inside it, as if it is <iframe>, you use as() method again
        var iframeAsNormalComponent = parentIFrame.create().byXpath(Div.class, "//iframe[@src='https://www.w3schools.com']");
        var iframeAsFrame = iframeAsNormalComponent.as(Frame.class);

        Assertions.assertEquals(iframeAsFrame.create().byXpath(Div.class, "//div[@id='subtopnav']//a[@title='HTML Tutorial']").getInnerText(), "HTML");
    }

    @Test
    public void testFindingElementsInShadowDOM() {
        app().navigate().to("https://web.dev/articles/shadowdom-v1");
        // First, we create a Frame component, because the elements for this test are inside of it
        var iframe = app().create().byXpath(Frame.class, "//iframe[contains(@src, 'raw/fancy-tabs-demo.html')]");
        iframe.scrollToVisible();

        var tabList = iframe.create().byAttributeContaining(Div.class, "role", "tablist");

        // Be mindful that when in the Shadow DOM, Xpath doesn't work, you'll have to use CSS only
        var tabs = tabList.create().byId(Div.class, "tabsSlot");
        // This Xpath won't find the element, even though it searches by id as well:
        //// var tabs = tabList.create().byXpath(Div.class, "//*[@id='tabsSlot']");

        Assertions.assertEquals(tabs.getAttribute("name"), "title");
    }
}