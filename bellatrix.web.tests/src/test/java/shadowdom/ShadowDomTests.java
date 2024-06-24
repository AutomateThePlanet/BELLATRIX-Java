package shadowdom;

import common.configuration.TestPagesSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
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

        var select = shadowRoot.createByCss(Select.class, "[name='select']");

        select.selectByText("Is");
        Assertions.assertEquals("Is", select.getSelected().getText());
    }

    @Test
    public void findingElementWithXpath_basic() {
        var shadowHost = app().create().byId(Div.class, "basicShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var select = shadowRoot.createByXPath(Select.class, "//select[@name='select']");

        select.selectByText("Is");
        Assertions.assertEquals("Is", select.getSelected().getText());
    }

    @Test
    public void findingElementInNestedShadowRoot_withXpath() {
        var shadowHost = app().create().byId(Div.class, "complexShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var firstEditAnchor = shadowRoot.createByXPath(Anchor.class, "//a[@href='#edit']");
        Assertions.assertEquals("edit", firstEditAnchor.getText());
    }

    @Test
    public void findingElementInNestedShadowRoot_withCss() {
        var shadowHost = app().create().byId(Div.class, "complexShadowHost");
        var shadowRoot = shadowHost.getShadowRoot();

        var firstEditAnchor = shadowRoot.createByCss(Anchor.class, "a[href='#edit']");
        Assertions.assertEquals("edit", firstEditAnchor.getText());
    }

    // TODO: Test Relative Finding of Elements
}
