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

package solutions.bellatrix.web.components;

import layout.LayoutComponentValidationsBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.contracts.Component;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.findstrategies.*;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.DriverService;
import solutions.bellatrix.web.services.BrowserService;
import solutions.bellatrix.web.services.ComponentCreateService;
import solutions.bellatrix.web.services.ComponentWaitService;
import solutions.bellatrix.web.services.JavaScriptService;
import solutions.bellatrix.web.waitstrategies.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class WebComponent extends LayoutComponentValidationsBuilder implements Component {
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

    @Setter(AccessLevel.PROTECTED) private WebElement wrappedElement;
    @Getter @Setter private WebElement parentWrappedElement;
    @Getter @Setter private int elementIndex;
    @Getter @Setter private FindStrategy findStrategy;
    @Getter private final WebDriver wrappedDriver;
    @Getter protected final JavaScriptService javaScriptService;
    @Getter protected final BrowserService browserService;
    @Getter protected final ComponentCreateService componentCreateService;
    @Getter protected final ComponentWaitService componentWaitService;
    private final List<WaitStrategy> waitStrategies;
    private final WebSettings webSettings;

    public WebComponent() {
        waitStrategies = new ArrayList<>();
        webSettings = ConfigurationService.get(WebSettings.class);
        javaScriptService = new JavaScriptService();
        browserService = new BrowserService();
        componentCreateService = new ComponentCreateService();
        componentWaitService = new ComponentWaitService();
        wrappedDriver = DriverService.getWrappedDriver();
    }

    public WebElement getWrappedElement() {
        try {
            wrappedElement.isDisplayed(); // checking if getting property throws exception
            return wrappedElement;
        } catch (StaleElementReferenceException | NoSuchElementException | NullPointerException ex) {
            return findElement();
        }
    }

    public String getComponentName() {
        return String.format("%s (%s)", getComponentClass().getSimpleName(), findStrategy.toString());
    }

    public void waitToBe() {
        findElement();
    }

    public void scrollToVisible() {
        scrollToVisible(getWrappedElement(), false);
    }

    public void setAttribute(String name, String value) {
        SETTING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, name));
        javaScriptService.execute(String.format("arguments[0].setAttribute('%s', '%s');", name, value), this);
        ATTRIBUTE_SET.broadcast(new ComponentActionEventArgs(this));
    }

    public void focus() {
        FOCUSING.broadcast(new ComponentActionEventArgs(this));
        javaScriptService.execute("window.focus();");
        javaScriptService.execute("arguments[0].focus();", getWrappedElement());
        FOCUSED.broadcast(new ComponentActionEventArgs(this));
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this));
        Actions actions = new Actions(wrappedDriver);
        actions.moveToElement(getWrappedElement()).build().perform();
        HOVERED.broadcast(new ComponentActionEventArgs(this));
    }

    public Class<?> getComponentClass() {
        return getClass();
    }

    public Point getLocation() {
        return getWrappedElement().getLocation();
    }

    public Dimension getSize() {
        return getWrappedElement().getSize();
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
        return getWrappedElement().getAttribute(name);
    }

    public String getCssValue(String propertyName) {
        return getWrappedElement().getCssValue(propertyName);
    }

    public void ensureState(WaitStrategy waitStrategy) {
        waitStrategies.add(waitStrategy);
    }

    public <TElementType extends WebComponent> TElementType toExist() {
        var waitStrategy = new ToExistWaitStrategy();
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
        if (currentBrowser == Browser.CHROME_HEADLESS) return;

        try {
            var originalElementBackground = getWrappedElement().getCssValue("background");
            var originalElementColor = getWrappedElement().getCssValue("color");
            var originalElementOutline = getWrappedElement().getCssValue("outline");
            javaScriptService.execute("arguments[0].style.background='yellow';arguments[0].style.color='black';arguments[0].style.outline='1px solid black';return;", wrappedElement);

            var runnable = new Runnable() {
                @SneakyThrows
                public void run() {
                    Thread.sleep(100);
                    try {
                        javaScriptService.execute("arguments[0].style.background='" + originalElementBackground + "';arguments[0].style.color='" + originalElementColor + "';arguments[0].style.outline='" + originalElementOutline + "';return;", wrappedElement);
                    } catch (NoSuchSessionException | NullPointerException ignored) {
                    }
                }
            };
            new Thread(runnable).start();
        } catch (Exception ignored) {
        }
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
        for (int i = 0; i < nativeElements.size(); i++) {
            var component = InstanceFactory.create(componentClass);
            component.setFindStrategy(findStrategy);
            component.setElementIndex(i);
            component.setParentWrappedElement(wrappedElement);
            componentList.add(component);
        }

        CREATED_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        return componentList;
    }

    public WebElement findElement() {
        if (waitStrategies.size() == 0) {
            waitStrategies.add(Wait.to().exist(webSettings.getTimeoutSettings().getElementWaitTimeout(), webSettings.getTimeoutSettings().getSleepInterval()));
        }

        try {
            for (var waitStrategy : waitStrategies) {
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
            Log.error("%n%nThe component: %n" +
                            "     Type: \"\u001B[1m%s\u001B[0m\"%n" +
                            "  Locator: \"\u001B[1m%s\u001B[0m\"%n" +
                            "Was not found on the page or didn't fulfill the specified conditions.%n%n",
                    getComponentClass().getSimpleName(), findStrategy.toString());
            throw ex;
        }

        RETURNING_WRAPPED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return wrappedElement;
    }

    private void simulateClick(WebComponent component) {
        var currentBrowser = DriverService.getBrowserConfiguration().getBrowser();
        var jsClick = "['mousedown','mouseup','click'].forEach(l=>{const a=arguments[0];const r=a.getBoundingClientRect();const x=r.left+(r.right-r.left)/2;y=r.top+(r.bottom-r.top)/2;((e,n,x,y)=>{e.dispatchEvent(new MouseEvent(n,{view:window,bubbles:true,cancelable:true,clientX:x,clientY:y,button:0}))})(a,l,x,y)})";
        if (currentBrowser == Browser.INTERNET_EXPLORER) {
            getWrappedElement().click();
            return;
        }
        try {
            javaScriptService.execute(jsClick, getWrappedElement());
        } catch (StaleElementReferenceException e) {
            javaScriptService.execute(jsClick, findElement());
        }
    }

    protected void defaultClick(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));

        toExist().toBeClickable().waitToBe();
        simulateClick(this);
        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultCheck(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));

        this.toExist().toBeClickable().waitToBe();
        if (!getWrappedElement().isSelected()) {
            simulateClick(this);
        }

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultUncheck(EventListener<ComponentActionEventArgs> checking, EventListener<ComponentActionEventArgs> checked) {
        checking.broadcast(new ComponentActionEventArgs(this));

        toExist().toBeClickable().waitToBe();
        if (getWrappedElement().isSelected()) {
            simulateClick(this);
        }

        checked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void setValue(EventListener<ComponentActionEventArgs> gettingValue, EventListener<ComponentActionEventArgs> gotValue, String value) {
        gettingValue.broadcast(new ComponentActionEventArgs(this, value));
        javaScriptService.execute(String.format("arguments[0].value = '%s';", value), getWrappedElement());
        gotValue.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected void defaultSelectByText(EventListener<ComponentActionEventArgs> selectingValue, EventListener<ComponentActionEventArgs> valueSelected, String value) {
        selectingValue.broadcast(new ComponentActionEventArgs(this, value));
        new Select(getWrappedElement()).selectByVisibleText(value);
        valueSelected.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected void defaultSelectByIndex(EventListener<ComponentActionEventArgs> selectingValue, EventListener<ComponentActionEventArgs> valueSelected, int value) {
        selectingValue.broadcast(new ComponentActionEventArgs(this, "index: " + value));
        new Select(getWrappedElement()).selectByIndex(value);
        valueSelected.broadcast(new ComponentActionEventArgs(this, "index: " + value));
    }

    protected String defaultGetValue() {
        try {
            return Optional.ofNullable(getAttribute("value")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("value")).orElse("");
        }
    }

    protected String defaultGetName() {
        try {
            return Optional.ofNullable(getAttribute("name")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("name")).orElse("");
        }
    }

    protected String defaultGetMaxLength() {
        try {
            return Optional.ofNullable(getAttribute("max")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("max")).orElse("");
        }
    }

    protected String defaultGetMinLength() {
        try {
            return Optional.ofNullable(getAttribute("min")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("min")).orElse("");
        }
    }

    protected String defaultGetSizeAttribute() {
        try {
            return Optional.ofNullable(getAttribute("size")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("size")).orElse("");
        }
    }

    protected String defaultGetSizesAttribute() {
        try {
            return Optional.ofNullable(getAttribute("sizes")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("sizes")).orElse("");
        }
    }

    protected String defaultGetSrcAttribute() {
        try {
            return Optional.ofNullable(getAttribute("src")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("src")).orElse("");
        }
    }

    protected String defaultGetSrcSetAttribute() {
        try {
            return Optional.ofNullable(getAttribute("srcset")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("srcset")).orElse("");
        }
    }

    protected String defaultGetAltAttribute() {
        try {
            return Optional.ofNullable(getAttribute("alt")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("alt")).orElse("");
        }
    }

    protected String defaultGetColsAttribute() {
        try {
            return Optional.ofNullable(getAttribute("cols")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("cols")).orElse("");
        }
    }

    protected String defaultGetRowsAttribute() {
        try {
            return Optional.ofNullable(getAttribute("rows")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("rows")).orElse("");
        }
    }

    protected String defaultGetLongDescAttribute() {
        try {
            return Optional.ofNullable(getAttribute("longdesc")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("longdesc")).orElse("");
        }
    }

    protected String defaultGetHeightAttribute() {
        try {
            return Optional.ofNullable(getAttribute("height")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("height")).orElse("");
        }
    }

    protected String defaultGetWidthAttribute() {
        try {
            return Optional.ofNullable(getAttribute("width")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("width")).orElse("");
        }
    }

    protected String defaultGetInnerHtmlAttribute() {
        try {
            return Optional.ofNullable(getAttribute("innerHTML")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("innerHTML")).orElse("");
        }
    }

    protected String defaultGetForAttribute() {
        try {
            return Optional.ofNullable(getAttribute("for")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("for")).orElse("");
        }
    }

    protected String defaultGetTargetAttribute() {
        try {
            return Optional.ofNullable(getAttribute("target")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("target")).orElse("");
        }
    }

    protected String defaultGetRelAttribute() {
        try {
            return Optional.ofNullable(getAttribute("rel")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("rel")).orElse("");
        }
    }

    protected boolean defaultGetDisabledAttribute() {
        try {
            var valueAttr = Optional.ofNullable(getAttribute("disabled")).orElse("false");
            return valueAttr.toLowerCase(Locale.ROOT).equals("true");
        } catch (StaleElementReferenceException e) {
            var valueAttr = Optional.ofNullable(findElement().getAttribute("disabled")).orElse("false");
            return valueAttr.toLowerCase(Locale.ROOT).equals("true");
        }
    }

    protected String defaultGetText() {
        try {
            return Optional.ofNullable(getWrappedElement().getText()).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getText()).orElse("");
        }
    }

    protected String defaultGetMinAttribute() {
        try {
            return Optional.ofNullable(getAttribute("min")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("min")).orElse("");
        }
    }

    protected String defaultGetMaxAttribute() {
        try {
            return Optional.ofNullable(getAttribute("max")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("max")).orElse("");
        }
    }

    protected String defaultGetStepAttribute() {
        try {
            return Optional.ofNullable(getAttribute("step")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("step")).orElse("");
        }
    }

    protected String defaultGetWrapAttribute() {
        try {
            return Optional.ofNullable(getAttribute("wrap")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("wrap")).orElse("");
        }
    }

    protected String defaultGetPlaceholderAttribute() {
        try {
            return Optional.ofNullable(getAttribute("placeholder")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("placeholder")).orElse("");
        }
    }

    protected String defaultGetAcceptAttribute() {
        try {
            return Optional.ofNullable(getAttribute("accept")).orElse(null);
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("accept")).orElse(null);
        }
    }

    protected boolean defaultGetAutoCompleteAttribute() {
        try {
            return Optional.ofNullable(getAttribute("autocomplete")).orElse("").equals("on");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("autocomplete")).orElse("").equals("on");
        }
    }

    protected boolean defaultGetSpellCheckAttribute() {
        try {
            return Optional.ofNullable(getAttribute("spellcheck")).orElse("").equals("on");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("spellcheck")).orElse("").equals("on");
        }
    }

    protected boolean defaultGetReadonlyAttribute() {
        try {
            return !StringUtils.isEmpty(Optional.ofNullable(getAttribute("readonly")).orElse(""));
        } catch (StaleElementReferenceException e) {
            return !StringUtils.isEmpty(Optional.ofNullable(findElement().getAttribute("readonly")).orElse(""));
        }
    }

    protected boolean defaultGetRequiredAttribute() {
        try {
            return !StringUtils.isEmpty(Optional.ofNullable(getAttribute("required")).orElse(""));
        } catch (StaleElementReferenceException e) {
            return !StringUtils.isEmpty(Optional.ofNullable(findElement().getAttribute("required")).orElse(""));
        }
    }

    protected boolean defaultGetMultipleAttribute() {
        try {
            return !StringUtils.isEmpty(Optional.ofNullable(getAttribute("multiple")).orElse(""));
        } catch (StaleElementReferenceException e) {
            return !StringUtils.isEmpty(Optional.ofNullable(findElement().getAttribute("multiple")).orElse(""));
        }
    }

    protected String defaultGetList() {
        try {
            return Optional.ofNullable(getAttribute("list")).orElse("");
        } catch (StaleElementReferenceException e) {
            return Optional.ofNullable(findElement().getAttribute("list")).orElse("");
        }
    }

    @SneakyThrows
    protected String defaultGetHref() {
        try {
            return unescapeHtml4(URLDecoder.decode(Optional.ofNullable(getAttribute("href")).orElse(""), StandardCharsets.UTF_8.name()));
        } catch (StaleElementReferenceException e) {
            return unescapeHtml4(URLDecoder.decode(Optional.ofNullable(findElement().getAttribute("href")).orElse(""), StandardCharsets.UTF_8.name()));
        }
    }

    protected void defaultSetText(EventListener<ComponentActionEventArgs> settingValue, EventListener<ComponentActionEventArgs> valueSet, String value) {
        settingValue.broadcast(new ComponentActionEventArgs(this, value));

        simulateClick(this);
        getWrappedElement().clear();
        getWrappedElement().sendKeys(value);

        valueSet.broadcast(new ComponentActionEventArgs(this, value));
    }

    private WebElement findNativeElement() {
        if (parentWrappedElement == null) {
            return wrappedDriver.findElements(findStrategy.convert()).get(elementIndex);
        } else {
            return parentWrappedElement.findElements(findStrategy.convert()).get(elementIndex);
        }
    }

    private void addArtificialDelay() {
        if (webSettings.getArtificialDelayBeforeAction() != 0) {
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

    private void scrollToVisible(WebElement wrappedElement, boolean shouldWait) {
        SCROLLING_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
        try {
            javaScriptService.execute("arguments[0].scrollIntoView(true);", wrappedElement);
            if (shouldWait) {
                Thread.sleep(500);
                toExist().waitToBe();
            }
        } catch (ElementNotInteractableException | InterruptedException ex) {
            DebugInformation.printStackTrace(ex);
        }

        SCROLLED_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
    }
}
