/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required createBy applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.components;

import layout.LayoutAssertionsFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.components.contracts.Component;
import solutions.bellatrix.configuration.ConfigurationService;
import solutions.bellatrix.configuration.WebSettings;
import solutions.bellatrix.findstrategies.*;
import solutions.bellatrix.infrastructure.Browser;
import solutions.bellatrix.infrastructure.DriverService;
import solutions.bellatrix.plugins.EventListener;
import solutions.bellatrix.services.BrowserService;
import solutions.bellatrix.services.ComponentCreateService;
import solutions.bellatrix.services.ComponentWaitService;
import solutions.bellatrix.services.JavaScriptService;
import solutions.bellatrix.utilities.DebugInformation;
import solutions.bellatrix.utilities.InstanceFactory;
import solutions.bellatrix.waitstrategies.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public class WebComponent implements Component {
    public final static EventListener<ComponentActionEventArgs> HOVERING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> HOVERED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> FOCUSING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> FOCUSED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLING_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLED_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SETTING_ATTRIBUTE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> ATTRIBUTE_SET = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> RETURNING_WRAPPED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING_ELEMENTS = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED_ELEMENTS = new EventListener<>();

    @Getter  @Setter(AccessLevel.PROTECTED) private WebElement wrappedElement;
    @Getter @Setter private WebElement parentWrappedElement;
    @Getter @Setter private int elementIndex;
    @Getter @Setter private FindStrategy findStrategy;
    @Getter private WebDriver wrappedDriver;
    @Getter protected JavaScriptService javaScriptService;
    @Getter protected BrowserService browserService;
    @Getter protected ComponentCreateService componentCreateService;
    @Getter protected ComponentWaitService componentWaitService;
    private List<WaitStrategy> waitStrategies;
    private WebSettings webSettings;

    public WebComponent() {
        this.waitStrategies = new ArrayList<>();
        webSettings = ConfigurationService.get(WebSettings.class);
        javaScriptService = new JavaScriptService();
        browserService = new BrowserService();
        componentCreateService = new ComponentCreateService();
        componentWaitService = new ComponentWaitService();
        wrappedDriver = DriverService.getWrappedDriver();
    }

    public LayoutAssertionsFactory layout() {
        return new LayoutAssertionsFactory(this);
    }

    public String getElementName() {
        return String.format("%s (%s)", getComponentClass().getSimpleName(), findStrategy.toString());
    }

    public void waitToBe() {
        findElement();
    }

    public void scrollToVisible() {
        scrollToVisible(findElement(), false);
    }

    public void setAttribute(String name, String value) {
        SETTING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this));
        javaScriptService.execute(String.format("arguments[0].setAttribute('%s', '%s');", name, value), this);
        ATTRIBUTE_SET.broadcast(new ComponentActionEventArgs(this));
    }

    public void focus() {
        FOCUSING.broadcast(new ComponentActionEventArgs(this));
        javaScriptService.execute("window.focus();");
        javaScriptService.execute("arguments[0].focus();", findElement());
        FOCUSED.broadcast(new ComponentActionEventArgs(this));
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this));
        javaScriptService.execute("arguments[0].onmouseover();", findElement());
        HOVERED.broadcast(new ComponentActionEventArgs(this));
    }

    public Class<?> getComponentClass() {
        return getClass();
    }

    public Point getLocation() {
        return findElement().getLocation();
    }

    public Dimension getSize() {
        return findElement().getSize();
    }

    public String getTitle() {
        return getAttribute("title");
    }

    public String getTabIndex() {
        return getAttribute("tabindex");
    }

    public String getAccessKey() {
        return getAttribute("accesskey");
    }

    public String getStyle() {
        return getAttribute("style");
    }

    public String getDir() {
        return getAttribute("dir");
    }

    public String getLang() {
        return getAttribute("lang");
    }

    public String getHtmlClass() {
        return getAttribute("class");
    }

    public String getAttribute(String name) {
        return findElement().getAttribute(name);
    }

    public String getCssValue(String propertyName) {
        return findElement().getCssValue(propertyName);
    }

    public void ensureState(WaitStrategy waitStrategy) {
        waitStrategies.add(waitStrategy);
    }

    public <TElementType extends WebComponent> TElementType toExists() {
        var waitStrategy = new ToExistsWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeClickable() {
        var waitStrategy = new ToBeClickableWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeVisible() {
        var waitStrategy = new ToBeVisibleWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent, TWaitStrategy extends WaitStrategy> TElementType to(Class<TWaitStrategy> waitClass, TElementType element) {
        var waitStrategy = InstanceFactory.create(waitClass);
        element.ensureState(waitStrategy);
        return element;
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return create(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return createAll(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent> TComponent createById(Class<TComponent> componentClass, String id) {
        return create(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> TComponent createByCss(Class<TComponent> componentClass, String css) {
        return create(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> TComponent createByClass(Class<TComponent> componentClass, String cclass) {
        return create(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends WebComponent> TComponent createByXPath(Class<TComponent> componentClass, String xpath) {
        return create(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent createByLinkText(Class<TComponent> componentClass, String linkText) {
        return create(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> TComponent createByTag(Class<TComponent> componentClass, String tag) {
        return create(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent createByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return create(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> TComponent createByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return create(componentClass, new InnerTextContainsFindStrategy(innerText));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllById(Class<TComponent> componentClass, String id) {
        return createAll(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByCss(Class<TComponent> componentClass, String css) {
        return createAll(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByClass(Class<TComponent> componentClass, String cclass) {
        return createAll(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByXPath(Class<TComponent> componentClass, String xpath) {
        return createAll(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByLinkText(Class<TComponent> componentClass, String linkText) {
        return createAll(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTag(Class<TComponent> componentClass, String tag) {
        return createAll(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return createAll(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return createAll(componentClass, new InnerTextContainsFindStrategy(innerText));
    }

    public void highlight() {
        var currentBrowser = DriverService.getBrowserConfiguration().getBrowser();
        if (currentBrowser == Browser.CHROME_HEADLESS || currentBrowser == Browser.EDGE_HEADLESS) return;

        try {
            var nativeElement = wrappedElement;
            var originalElementBorder = nativeElement.getCssValue("background-color");
            javaScriptService.execute("arguments[0].style.background='yellow'; return;", nativeElement);

            var runnable = new Runnable() {
                @SneakyThrows
                public void run() {
                    Thread.sleep(100);
                    javaScriptService.execute("arguments[0].style.background='" + originalElementBorder + "'; return;", nativeElement);
                }
            };
            new Thread(runnable).start();

        } catch (Exception ignored) { }
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        component.setParentWrappedElement(wrappedElement);
        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var nativeElements = wrappedElement.findElements(findStrategy.convert());
        List<TComponent> componentList = new ArrayList<>();
        for (int i = 0; i < nativeElements.stream().count(); i++) {
            var component = InstanceFactory.create(componentClass);
            component.setFindStrategy(findStrategy);
            component.setElementIndex(i);
            component.setParentWrappedElement(wrappedElement);
            componentList.add(component);
        }

        CREATED_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        return componentList;
    }

    protected WebElement findElement() {
      if (waitStrategies.stream().count() == 0) {
          waitStrategies.add(Wait.to().exists());
      }

      try {
          for (var waitStrategy:waitStrategies) {
              componentWaitService.wait(this, waitStrategy);
          }

          wrappedElement = findNativeElement();
          scrollToMakeElementVisible(wrappedElement);
          if (webSettings.getWaitUntilReadyOnElementFound()) {
              browserService.waitForAjax();
          }

          if (webSettings.getWaitForAngular()) {
              browserService.waitForAngular();
          }

          addArtificialDelay();

          waitStrategies.clear();
      } catch (WebDriverException ex) {
          DebugInformation.printStackTrace(ex);
          System.out.print(String.format("\n\nThe element: \n Name: '%s', \n Locator: '%s = %s', \nWas not found on the page or didn't fulfill the specified conditions.\n\n", getComponentClass().getSimpleName(), findStrategy.toString(), findStrategy.getValue()));
      }

        RETURNING_WRAPPED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return wrappedElement;
    }


    protected void defaultClick(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked)
    {
        clicking.broadcast(new ComponentActionEventArgs(this));

        this.toExists().toBeClickable().waitToBe();
        javaScriptService.execute("arguments[0].focus();arguments[0].click();", wrappedElement);

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void setValue(EventListener<ComponentActionEventArgs> gettingValue, EventListener<ComponentActionEventArgs> gotValue, String value)
    {
        gettingValue.broadcast(new ComponentActionEventArgs(this));
        setAttribute("value", value);
        gotValue.broadcast(new ComponentActionEventArgs(this));
    }

    protected String defaultGetValue() {
        return Optional.ofNullable(getAttribute("value")).orElse("");
    }

    protected String defaultGetMaxLength() {
        return Optional.ofNullable(getAttribute("maxlength")).orElse("");
    }

    protected String defaultGetMinLength() {
        return Optional.ofNullable(getAttribute("minlength")).orElse("");
    }

    protected String defaultGetSizeAttribute() {
        return Optional.ofNullable(getAttribute("size")).orElse("");
    }

    protected String defaultGetHeightAttribute() {
        return Optional.ofNullable(getAttribute("height")).orElse("");
    }

    protected String defaultGetWidthAttribute() {
        return Optional.ofNullable(getAttribute("width")).orElse("");
    }

    protected String defaultGetInnerHtmlAttribute() {
        return Optional.ofNullable(getAttribute("innerHTML")).orElse("");
    }

    protected String defaultGetForAttribute() {
        return Optional.ofNullable(getAttribute("for")).orElse("");
    }

    protected String defaultGetTargetAttribute() {
        return Optional.ofNullable(getAttribute("target")).orElse("");
    }

    protected String defaultGetRelAttribute() {
        return Optional.ofNullable(getAttribute("rel")).orElse("");
    }

    protected Boolean defaultGetDisabledAttribute() {
        var valueAttr = Optional.ofNullable(getAttribute("disabled")).orElse("false");
        return valueAttr.toLowerCase(Locale.ROOT) == "true";
    }

    protected String defaultGetText() {
        return Optional.ofNullable(findElement().getText()).orElse("");
    }

    protected String defaultGetMinAttribute() {
        return Optional.ofNullable(getAttribute("min")).orElse("");
    }

    protected String defaultGetMaxAttribute() {
        return Optional.ofNullable(getAttribute("max")).orElse("");
    }

    protected String defaultGetStepAttribute() {
        return Optional.ofNullable(getAttribute("step")).orElse("");
    }

    protected String defaultGetPlaceholderAttribute() {
        return Optional.ofNullable(getAttribute("placeholder")).orElse("");
    }

    protected String defaultGetAcceptAttribute() {
        return Optional.ofNullable(getAttribute("accept")).orElse(null);
    }

    protected Boolean defaultGetAutoCompleteAttribute() {
        return Optional.ofNullable(getAttribute("autocomplete")).orElse("") == "on";
    }

    protected Boolean defaultGetReadonlyAttribute() {
        return !StringUtils.isEmpty(Optional.ofNullable(getAttribute("readonly")).orElse(""));
    }

    protected Boolean defaultGetRequiredAttribute()
    {
        return !StringUtils.isEmpty(Optional.ofNullable(getAttribute("required")).orElse(""));
    }

    protected String defaultGetList() {
        return Optional.ofNullable(getAttribute("list")).orElse("");
    }

    @SneakyThrows
    protected String defaultGetHref() {
        return unescapeHtml4(URLDecoder.decode(Optional.ofNullable(getAttribute("href")).orElse(""), StandardCharsets.UTF_8.name()));
    }

    protected void defaultSetText(EventListener<ComponentActionEventArgs> settingValue, EventListener<ComponentActionEventArgs> valueSet, String value)
    {
        settingValue.broadcast(new ComponentActionEventArgs(this));

        findElement().clear();
        findElement().sendKeys(value);

        valueSet.broadcast(new ComponentActionEventArgs(this));
    }

    private WebElement findNativeElement() {
        if (parentWrappedElement == null) {
            return wrappedDriver.findElements(findStrategy.convert()).get(elementIndex);
        } else {
            return parentWrappedElement.findElements(findStrategy.convert()).get(elementIndex);
        }
    }

    private void addArtificialDelay() {
        if (webSettings.getArtificialDelayBeforeAction() != 0)
        {
            try {
                Thread.sleep(webSettings.getArtificialDelayBeforeAction());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void scrollToMakeElementVisible(WebElement wrappedElement) {
        // createBy default scroll down to make the element visible.
        if (webSettings.getAutomaticallyScrollToVisible()) {
            scrollToVisible(wrappedElement, false);
        }
    }

    private void scrollToVisible(WebElement wrappedElement, Boolean shouldWait)
    {
        SCROLLING_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
        try {
            javaScriptService.execute("arguments[0].scrollIntoView(true);", wrappedElement);
            if (shouldWait)
            {
                Thread.sleep(500);
                toExists().waitToBe();
            }
        } catch (ElementNotInteractableException ex) {
            DebugInformation.printStackTrace(ex);
        } catch (InterruptedException e) {
            DebugInformation.printStackTrace(e);
        }

        SCROLLED_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
    }

    // TODO: Anton(03.02.2021) : Align with Kotlin framework design with only 2 global events and single 10 lines class for BDD logging.
    // TODO: Anton(03.02.2021) : Maybe we can move all of these validate methods to default methods in separate interfaces? However, we cannot easily share protected waitUntil
//    public final static EventListener<ComponentActionEventArgs> VALIDATED_ACCEPT_IS_NULL = new EventListener<>();
//    public final static EventListener<ComponentActionEventArgs> VALIDATED_ACCEPT_IS = new EventListener<>();
//
//    protected void defaultValidateAcceptIsNull() {
//        waitUntil((d) -> defaultGetAcceptAttribute() == null, String.format("The control's accept should be null but was '%s'.", defaultGetAcceptAttribute()));
//        VALIDATED_ACCEPT_IS_NULL.broadcast(new ComponentActionEventArgs(this));
//    }
//
//    protected void defaultValidateAcceptIs(String value) {
//        waitUntil((d) -> defaultGetAcceptAttribute().equals(value), String.format("The control's accept should be '%s' but was '%s'.", defaultGetAcceptAttribute()));
//        VALIDATED_ACCEPT_IS.broadcast(new ComponentActionEventArgs(this));
//    }

    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    protected void defaultValidateAcceptIsNull() {
        waitUntil((d) -> defaultGetAcceptAttribute() == null, String.format("The control's accept should be null but was '%s'.", defaultGetAcceptAttribute()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, null, "validate accept is null"));
    }

    protected void defaultValidateAcceptIs(String value) {
        waitUntil((d) -> defaultGetAcceptAttribute().equals(value), String.format("The control's accept should be '%s' but was '%s'.", defaultGetAcceptAttribute()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, String.format("validate accept is %s", value)));
    }

    protected void defaultValidateHrefIs(String value) {
        waitUntil((d) -> defaultGetHref().equals(value), String.format("The control's href should be '%s' but was '%s'.", value, defaultGetHref()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, String.format("validate href is %s", value)));
    }

    protected void defaultValidateHrefIsSet() {
        waitUntil((d) -> !StringUtils.isEmpty(defaultGetHref()), "The control's href shouldn't be empty but was.");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, null, "validate href is empty"));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), webSettings.getTimeoutSettings().getValidationsTimeout(), webSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            DebugInformation.printStackTrace(ex);
            var validationExceptionMessage = String.format("%s The test failed on URL: %s", exceptionMessage, browserService.getUrl());
            throw new TimeoutException(validationExceptionMessage, ex);
        }
    }
}
