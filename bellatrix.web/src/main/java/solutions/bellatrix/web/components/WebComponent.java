/*
 * Copyright 2022 Automate The Planet Ltd.
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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.contracts.Component;
import solutions.bellatrix.web.components.contracts.ComponentStyle;
import solutions.bellatrix.web.components.contracts.ComponentVisible;
import solutions.bellatrix.web.components.shadowdom.ShadowHost;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.components.shadowdom.ShadowDomService;
import solutions.bellatrix.web.components.enums.ScrollPosition;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.findstrategies.*;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.DriverService;
import solutions.bellatrix.web.services.BrowserService;
import solutions.bellatrix.web.services.ComponentCreateService;
import solutions.bellatrix.web.services.ComponentWaitService;
import solutions.bellatrix.web.services.JavaScriptService;
import solutions.bellatrix.web.waitstrategies.*;

import java.awt.Dimension;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class WebComponent extends LayoutComponentValidationsBuilder implements Component, ComponentVisible, ComponentStyle {
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
    @Getter @Setter protected WebComponent parentComponent;
    @Getter @Setter private SearchContext parentWrappedElement;
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

    /**
     * Convert this component to another type of component.
     *
     * @param componentClass type of component
     */
    public <ComponentT extends WebComponent> ComponentT as(Class<ComponentT> componentClass) {
        if (this.getClass() == componentClass) return (ComponentT)this;

        var component = InstanceFactory.create(componentClass);

        if (componentClass != ShadowRoot.class) {
            component.setWrappedElement(this.wrappedElement);
        }
        component.setParentComponent(this.parentComponent);
        component.setParentWrappedElement(this.parentWrappedElement);
        component.setFindStrategy(this.findStrategy);
        component.setElementIndex(this.elementIndex);

        return component;
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
        scrollToVisible(getWrappedElement(), false, ScrollPosition.CENTER);
    }

    public void scrollToTop() {
        scrollToVisible(getWrappedElement(), false, ScrollPosition.START);
    }

    public void scrollToBottom() {
        scrollToVisible(getWrappedElement(), false, ScrollPosition.END);
    }

    public void setAttribute(String name, String value) {
        SETTING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, name));
        javaScriptService.execute(String.format("arguments[0].setAttribute('%s', '%s');", name, value), this);
        ATTRIBUTE_SET.broadcast(new ComponentActionEventArgs(this));
    }

    public SearchContext shadowRoot() {
        return getWrappedElement().getShadowRoot();
    }

    /**
     * Returns {@link ShadowRoot} from which you can safely create elements inside the shadow DOM
     * even with XPath.
     *
     * @return {@link ShadowRoot}
     */
    public ShadowRoot getShadowRoot() {
        return this.as(ShadowRoot.class);
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

    public java.awt.Point getLocation() {
        var location = findElement().getLocation();
        return new java.awt.Point(location.getX(), location.getY());
    }

    public Dimension getSize() {
        var size = findElement().getSize();
        return new java.awt.Dimension(size.getWidth(), size.getHeight());
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

    public <TElementType extends WebComponent> TElementType toShadowRootToBeAttached() {
        var waitStrategy = new ToShadowRootToBeAttachedWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotExist() {
        var waitStrategy = new ToNotExistWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeVisible() {
        var waitStrategy = new ToBeVisibleWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotBeVisible() {
        var waitStrategy = new ToNotBeVisibleWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeClickable() {
        var waitStrategy = new ToBeClickableWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeDisabled() {
        var waitStrategy = new ToBeDisabledWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toHaveContent() {
        var waitStrategy = new ToHaveContentWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toExist(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToExistWaitStrategy(timeoutInterval, sleepInterval);
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotExist(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToNotExistWaitStrategy(timeoutInterval, sleepInterval);
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeVisible(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToBeVisibleWaitStrategy(timeoutInterval, sleepInterval);
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotBeVisible(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToNotBeVisibleWaitStrategy(timeoutInterval, sleepInterval);
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeClickable(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToBeClickableWaitStrategy(timeoutInterval, sleepInterval);
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeDisabled(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToBeDisabledWaitStrategy(timeoutInterval, sleepInterval);
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toHaveContent(long timeoutInterval, long sleepInterval) {
        var waitStrategy = new ToHaveContentWaitStrategy(timeoutInterval, sleepInterval);
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

    public <TComponent extends WebComponent> TComponent createByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return create(componentClass, new AttributeContainingWithFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> TComponent createByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return create(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    public <TComponent extends WebComponent> TComponent createByCss(Class<TComponent> componentClass, String css) {
        return create(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> TComponent createByClass(Class<TComponent> componentClass, String className) {
        return create(componentClass, new ClassFindStrategy(className));
    }

    public <TComponent extends WebComponent> TComponent createByName(Class<TComponent> componentClass, String name) {
        return create(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> TComponent createByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return create(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    public <TComponent extends WebComponent> TComponent createByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return create(componentClass, new ValueContainingWithFindStrategy(valueContaining));
    }

    public <TComponent extends WebComponent> TComponent createByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return create(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    public <TComponent extends WebComponent> TComponent createByXPath(Class<TComponent> componentClass, String xpath) {
        return create(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent createByLinkText(Class<TComponent> componentClass, String linkText) {
        return create(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> TComponent createByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return create(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    public <TComponent extends WebComponent> TComponent createByTag(Class<TComponent> componentClass, String tag) {
        return create(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent createByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return create(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> TComponent createByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return create(componentClass, new InnerTextContainingFindStrategy(innerText));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllById(Class<TComponent> componentClass, String id) {
        return createAll(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return createAll(componentClass, new AttributeContainingWithFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return createAll(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByCss(Class<TComponent> componentClass, String css) {
        return createAll(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByClass(Class<TComponent> componentClass, String className) {
        return createAll(componentClass, new ClassFindStrategy(className));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByName(Class<TComponent> componentClass, String name) {
        return createAll(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return createAll(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return createAll(componentClass, new ValueContainingWithFindStrategy(valueContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return createAll(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByXPath(Class<TComponent> componentClass, String xpath) {
        return createAll(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByLinkText(Class<TComponent> componentClass, String linkText) {
        return createAll(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return createAll(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTag(Class<TComponent> componentClass, String tag) {
        return createAll(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return createAll(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return createAll(componentClass, new InnerTextContainingFindStrategy(innerText));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent shadowRootCreate(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return shadowRootCreate(componentClass, findStrategy);
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> shadowRootCreateAll(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return shadowRootCreateAll(componentClass, findStrategy);
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateById(Class<TComponent> componentClass, String id) {
        return shadowRootCreate(componentClass, new IdFindStrategy(id));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return shadowRootCreate(componentClass, new AttributeContainingWithFindStrategy(attributeName, value));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return shadowRootCreate(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByCss(Class<TComponent> componentClass, String css) {
        return shadowRootCreate(componentClass, new CssFindStrategy(css));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByClass(Class<TComponent> componentClass, String className) {
        return shadowRootCreate(componentClass, new ClassFindStrategy(className));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByName(Class<TComponent> componentClass, String name) {
        return shadowRootCreate(componentClass, new NameFindStrategy(name));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return shadowRootCreate(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return shadowRootCreate(componentClass, new ValueContainingWithFindStrategy(valueContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return shadowRootCreate(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByXPath(Class<TComponent> componentClass, String xpath) {
        return shadowRootCreate(componentClass, new XPathFindStrategy(xpath));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByLinkText(Class<TComponent> componentClass, String linkText) {
        return shadowRootCreate(componentClass, new LinkTextFindStrategy(linkText));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return shadowRootCreate(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByTag(Class<TComponent> componentClass, String tag) {
        return shadowRootCreate(componentClass, new TagFindStrategy(tag));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return shadowRootCreate(componentClass, new IdContainingFindStrategy(idContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> TComponent shadowRootCreateByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return shadowRootCreate(componentClass, new InnerTextContainingFindStrategy(innerText));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllById(Class<TComponent> componentClass, String id) {
        return shadowRootCreateAll(componentClass, new IdFindStrategy(id));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return shadowRootCreateAll(componentClass, new AttributeContainingWithFindStrategy(attributeName, value));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return shadowRootCreateAll(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByCss(Class<TComponent> componentClass, String css) {
        return shadowRootCreateAll(componentClass, new CssFindStrategy(css));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByClass(Class<TComponent> componentClass, String className) {
        return shadowRootCreateAll(componentClass, new ClassFindStrategy(className));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByName(Class<TComponent> componentClass, String name) {
        return shadowRootCreateAll(componentClass, new NameFindStrategy(name));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return shadowRootCreateAll(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return shadowRootCreateAll(componentClass, new ValueContainingWithFindStrategy(valueContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return shadowRootCreateAll(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByXPath(Class<TComponent> componentClass, String xpath) {
        return shadowRootCreateAll(componentClass, new XPathFindStrategy(xpath));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByLinkText(Class<TComponent> componentClass, String linkText) {
        return shadowRootCreateAll(componentClass, new LinkTextFindStrategy(linkText));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return shadowRootCreateAll(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByTag(Class<TComponent> componentClass, String tag) {
        return shadowRootCreateAll(componentClass, new TagFindStrategy(tag));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return shadowRootCreateAll(componentClass, new IdContainingFindStrategy(idContaining));
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return shadowRootCreateAll(componentClass, new InnerTextContainingFindStrategy(innerText));
    }

    public void highlight() {
        if (this.getWrappedElement() instanceof ShadowHost) return;

        var currentBrowser = DriverService.getBrowserConfiguration().getBrowser();
        if (currentBrowser == Browser.CHROME_HEADLESS) return;

        try {
            var originalElementBackground = getWrappedElement().getCssValue("background");
            var originalElementColor = getWrappedElement().getCssValue("color");
            var originalElementOutline = getWrappedElement().getCssValue("outline");

            String script = "arguments[0].style.background='yellow';arguments[0].style.color='black';arguments[0].style.outline='1px solid black';" +
                    "setTimeout(function() {" +
                    "arguments[0].style.background='" + originalElementBackground + "';" +
                    "arguments[0].style.color='" + originalElementColor + "';" +
                    "arguments[0].style.outline='" + originalElementOutline + "';" +
                    "}, 100);";

            javaScriptService.execute(script, wrappedElement);
        } catch (Exception ignored) {
        }
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent shadowRootCreate(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        component.setParentWrappedElement(getWrappedElement().getShadowRoot());
        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    /**
     * Use {@link WebComponent#getShadowRoot() } instead
     */
    @Deprecated
    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> shadowRootCreateAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var shadowRoot = getWrappedElement().getShadowRoot();
        var nativeElements = shadowRoot.findElements(findStrategy.convert());
        List<TComponent> componentList = new ArrayList<>();
        for (int i = 0; i < nativeElements.size(); i++) {
            var component = InstanceFactory.create(componentClass);
            component.setFindStrategy(findStrategy);
            component.setElementIndex(i);
            component.setParentWrappedElement(shadowRoot);
            componentList.add(component);
        }

        CREATED_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        return componentList;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();

        TComponent component;

        if (inShadowContext()) {
            component = ShadowDomService.createInShadowContext(this, componentClass, findStrategy);
        } else {
            component = InstanceFactory.create(componentClass);
            component.setFindStrategy(findStrategy);
            component.setParentComponent(this);
            component.setParentWrappedElement(this.getWrappedElement());
        }

        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();

        List<TComponent> componentList = new ArrayList<>();

        if (inShadowContext()) {
            componentList = ShadowDomService.createAllInShadowContext(this, componentClass, findStrategy);
        } else {
            var nativeElements = wrappedElement.findElements(findStrategy.convert());

            for (int i = 0; i < nativeElements.size(); i++) {
                var component = InstanceFactory.create(componentClass);
                component.setFindStrategy(findStrategy);
                component.setElementIndex(i);
                component.setParentWrappedElement(wrappedElement);
                componentList.add(component);
            }
        }

        CREATED_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        return componentList;
    }

    private boolean inShadowContext() {
        var component = this;

        while (component != null) {
            if (component instanceof ShadowRoot) return true;
            component = component.getParentComponent();
        }

        return false;
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
        } catch (Exception ex) {
            var formattedException = String.format("The component: \n" +
                            "     Type: %s" +
                            "  Locator: %s" +
                            "  URL: %s\"%n" +
                            "Was not found on the page or didn't fulfill the specified conditions.%n%n",
                    getComponentClass().getSimpleName(), findStrategy.toString(), getWrappedDriver().getCurrentUrl());
            Log.error(formattedException);

            throw new NotFoundException(formattedException, ex);
        }

        RETURNING_WRAPPED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return wrappedElement;
    }

    private void clickInternal() {
        long toBeClickableTimeout = webSettings.getTimeoutSettings().getElementToBeClickableTimeout();
        long sleepInterval = webSettings.getTimeoutSettings().getSleepInterval();

        FluentWait<WebDriver> wait = new FluentWait<>(getWrappedDriver())
                .withTimeout(Duration.ofSeconds(toBeClickableTimeout))
                .pollingEvery(Duration.ofSeconds(sleepInterval > 0 ? sleepInterval : 1));

        try {
            wait.until(x -> tryClick());
        } catch (TimeoutException e) {
            toBeVisible().toBeClickable().findElement().click();
        }
    }

    private boolean tryClick() {
        try {
            toBeVisible().toBeClickable().findElement().click();
            return true;
        } catch (ElementNotInteractableException e) {
            return false;
        } catch (WebDriverException e) {
            toBeVisible().toBeClickable().waitToBe();
            return false;
        }
    }

    protected void defaultClick(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));
        clickInternal();
        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultCheck(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));

        this.toExist().toBeClickable().waitToBe();
        if (!getWrappedElement().isSelected()) {
            clickInternal();
        }

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultUncheck(EventListener<ComponentActionEventArgs> checking, EventListener<ComponentActionEventArgs> checked) {
        checking.broadcast(new ComponentActionEventArgs(this));

        toExist().toBeClickable().waitToBe();
        if (getWrappedElement().isSelected()) {
            clickInternal();
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

        getWrappedElement().clear();
        getWrappedElement().sendKeys(value);

        valueSet.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected void defaultUpload(EventListener<ComponentActionEventArgs> settingValue, EventListener<ComponentActionEventArgs> valueSet, String value) {
        settingValue.broadcast(new ComponentActionEventArgs(this, value));

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
            scrollToVisible(wrappedElement, false, ScrollPosition.CENTER);
        }
    }


    private void scrollToVisible(WebElement wrappedElement, boolean shouldWait, ScrollPosition scrollPosition) {
        SCROLLING_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
        try {
            javaScriptService.execute("arguments[0].scrollIntoView({ block: \"" + scrollPosition.getValue() + "\", behavior: \"instant\", inline: \"nearest\" });", wrappedElement);
            if (shouldWait) {
                Thread.sleep(500);
                toExist().waitToBe();
            }
        } catch (ElementNotInteractableException | InterruptedException | ScriptTimeoutException ex) {
            DebugInformation.printStackTrace(ex);
        }

        SCROLLED_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
    }

    @Override
    public boolean isVisible() {
        try {
            return getWrappedElement().isDisplayed();
        } catch (NotFoundException e) {
            return false;
        }
    }
}
