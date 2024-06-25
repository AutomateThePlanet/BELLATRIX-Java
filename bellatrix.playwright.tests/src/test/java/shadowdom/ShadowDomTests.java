package shadowdom;

import configuration.TestPagesSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.playwright.components.Anchor;
import solutions.bellatrix.playwright.components.Div;
import solutions.bellatrix.playwright.components.Select;
import solutions.bellatrix.playwright.components.shadowdom.ShadowRoot;
import solutions.bellatrix.playwright.infrastructure.BrowserTypes;
import solutions.bellatrix.playwright.infrastructure.ExecutionBrowser;
import solutions.bellatrix.playwright.infrastructure.Lifecycle;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

@ExecutionBrowser(browser = BrowserTypes.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class ShadowDomTests extends WebTest {
    @BeforeEach
    public void init() {
        var url = ConfigurationService.get(TestPagesSettings.class).getShadowDomPage();
        app().navigate().toLocalPage(url);
    }

    @Test
    public void findingElementWithCss_basic() {
        var shadowHost = app().create().byId(Div.class, "basicShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var select = shadowRoot.create().byCss(Select.class, "[name='select']");

        select.selectByText("Is");
        Assertions.assertEquals("Is", select.getSelected().getText());
    }

    @Test
    public void findingElementWithXpath_basic() {
        var shadowHost = app().create().byId(Div.class, "basicShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var select = shadowRoot.create().byXpath(Select.class, "//select[@name='select']");

        select.selectByText("Is");
        Assertions.assertEquals("Is", select.getSelected().getText());
    }

    @Test
    public void findingElementInNestedShadowRoot_withXpath() {
        var shadowHost = app().create().byId(Div.class, "complexShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var firstEditAnchor = shadowRoot.create().byXpath(Anchor.class, "//a[@href='#edit']");
        Assertions.assertEquals("edit", firstEditAnchor.getText());
    }

    @Test
    public void findingElementInNestedShadowRoot_withCss() {
        var shadowHost = app().create().byId(Div.class, "complexShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var firstEditAnchor = shadowRoot.create().byCss(Anchor.class, "a[href='#edit']");
        Assertions.assertEquals("edit", firstEditAnchor.getText());
    }

    // TODO: Test Relative Finding of Elements
}
