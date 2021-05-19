/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.android.components;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import layout.LayoutComponentValidationsBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import solutions.bellatrix.android.components.contracts.Component;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.android.findstrategies.FindStrategy;
import solutions.bellatrix.android.findstrategies.NameFindStrategy;
import solutions.bellatrix.android.findstrategies.TagFindStrategy;
import solutions.bellatrix.android.findstrategies.XPathFindStrategy;
import solutions.bellatrix.android.infrastructure.DriverService;
import solutions.bellatrix.android.services.AppService;
import solutions.bellatrix.android.services.ComponentCreateService;
import solutions.bellatrix.android.services.ComponentWaitService;
import solutions.bellatrix.android.waitstrategies.*;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.InstanceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AndroidComponent extends LayoutComponentValidationsBuilder implements Component {
    public final static EventListener<ComponentActionEventArgs> HOVERING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> HOVERED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLING_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLED_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> RETURNING_WRAPPED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING_ELEMENTS = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED_ELEMENTS = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    @Setter(AccessLevel.PROTECTED) private MobileElement wrappedElement;
    @Getter @Setter private MobileElement parentWrappedElement;
    @Getter @Setter private int elementIndex;
    @Getter @Setter private FindStrategy findStrategy;
    @Getter private AndroidDriver<MobileElement> wrappedDriver;
    @Getter protected AppService appService;
    @Getter protected ComponentCreateService componentCreateService;
    @Getter protected ComponentWaitService componentWaitService;
    private List<WaitStrategy> waitStrategies;
    private AndroidSettings androidSettings;

    public AndroidComponent() {
        this.waitStrategies = new ArrayList<>();
        androidSettings = ConfigurationService.get(AndroidSettings.class);
        appService = new AppService();
        componentCreateService = new ComponentCreateService();
        componentWaitService = new ComponentWaitService();
        wrappedDriver = DriverService.getWrappedAndroidDriver();
    }

    public MobileElement getWrappedElement() {
        try {
            wrappedElement.isDisplayed(); // checking if getting property throws exception
            return wrappedElement;
        } catch (StaleElementReferenceException | NullPointerException ex) {
            return findElement();
        }
    }

    public String getElementName() {
        return String.format("%s (%s)", getComponentClass().getSimpleName(), findStrategy.toString());
    }

    public void waitToBe() {
        findElement();
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this));
        Actions action = new Actions(wrappedDriver);
        action.moveToElement(findElement()).build().perform();
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

    public String getAttribute(String name) {
        return findElement().getAttribute(name);
    }

    public void ensureState(WaitStrategy waitStrategy) {
        waitStrategies.add(waitStrategy);
    }

    @SuppressWarnings("unchecked")
    public <TElementType extends AndroidComponent> TElementType toExists() {
        var waitStrategy = new ToExistWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType) this;
    }

    @SuppressWarnings("unchecked")
    public <TElementType extends AndroidComponent> TElementType toBeClickable() {
        var waitStrategy = new ToBeClickableWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType) this;
    }

    @SuppressWarnings("unchecked")
    public <TElementType extends AndroidComponent> TElementType toBeVisible() {
        var waitStrategy = new ToBeVisibleWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType) this;
    }

    public <TElementType extends AndroidComponent, TWaitStrategy extends WaitStrategy> TElementType to(Class<TWaitStrategy> waitClass, TElementType element) {
        var waitStrategy = InstanceFactory.create(waitClass);
        element.ensureState(waitStrategy);
        return element;
    }

    public <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return create(componentClass, findStrategy);
    }

    public <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return createAll(componentClass, findStrategy);
    }

    public <TComponent extends AndroidComponent> TComponent createByXPath(Class<TComponent> componentClass, String xpath) {
        return create(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends AndroidComponent> TComponent createByName(Class<TComponent> componentClass, String name) {
        return create(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends AndroidComponent> TComponent createByTag(Class<TComponent> componentClass, String tag) {
        return create(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends AndroidComponent> List<TComponent> createAllByName(Class<TComponent> componentClass, String name) {
        return createAll(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends AndroidComponent> List<TComponent> createAllByXPath(Class<TComponent> componentClass, String xpath) {
        return createAll(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends AndroidComponent> List<TComponent> createAllByTag(Class<TComponent> componentClass, String tag) {
        return createAll(componentClass, new TagFindStrategy(tag));
    }

    protected <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        component.setParentWrappedElement(wrappedElement);
        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    protected <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var nativeElements = findStrategy.findAllElements(wrappedElement);
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

    protected MobileElement findElement() {
        if (waitStrategies.size() == 0) {
            waitStrategies.add(Wait.to().exist());
        }

        try {
            for (var waitStrategy : waitStrategies) {
                componentWaitService.wait(this, waitStrategy);
            }

            wrappedElement = findNativeElement();
            scrollToMakeElementVisible(wrappedElement);
            addArtificialDelay();

            waitStrategies.clear();
        } catch (WebDriverException ex) {
            DebugInformation.printStackTrace(ex);
            System.out.printf("%n%nThe element: %n Name: '%s', %n Locator: '%s', %nWas not found on the page or didn't fulfill the specified conditions.%n%n", getComponentClass().getSimpleName(), findStrategy.toString());
        }

        RETURNING_WRAPPED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return wrappedElement;
    }


    protected void defaultClick(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));

        this.toExists().toBeClickable().waitToBe();
        findElement().click();

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultCheck(EventListener<ComponentActionEventArgs> checking, EventListener<ComponentActionEventArgs> checked) {
        checking.broadcast(new ComponentActionEventArgs(this));

        this.toExists().toBeClickable().waitToBe();
        if (!this.defaultGetCheckedAttribute()) {
            findElement().click();
        }

        checked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultUncheck(EventListener<ComponentActionEventArgs> unchecking, EventListener<ComponentActionEventArgs> unchecked) {
        unchecking.broadcast(new ComponentActionEventArgs(this));

        this.toExists().toBeClickable().waitToBe();
        if (this.defaultGetCheckedAttribute()) {
            findElement().click();
        }

        unchecked.broadcast(new ComponentActionEventArgs(this));
    }

    protected boolean defaultGetDisabledAttribute() {
        var valueAttr = Optional.ofNullable(getAttribute("disabled")).orElse("false");
        return valueAttr.toLowerCase(Locale.ROOT).equals("true");
    }

    protected boolean defaultGetCheckedAttribute() {
        var valueAttr = Optional.ofNullable(getAttribute("checked")).orElse("false");
        return valueAttr.toLowerCase(Locale.ROOT).equals("true");
    }

    protected String defaultGetText() {
        return Optional.ofNullable(findElement().getText()).orElse("");
    }

    protected void defaultSetText(EventListener<ComponentActionEventArgs> settingValue, EventListener<ComponentActionEventArgs> valueSet, String value) {
        settingValue.broadcast(new ComponentActionEventArgs(this));

        findElement().clear();
        findElement().sendKeys(value);

        valueSet.broadcast(new ComponentActionEventArgs(this));
    }

    private MobileElement findNativeElement() {
        if (parentWrappedElement == null) {
            return findStrategy.findAllElements(wrappedDriver).get(elementIndex);
        } else {
            return findStrategy.findElement(parentWrappedElement);
        }
    }

    private void addArtificialDelay() {
        if (androidSettings.getArtificialDelayBeforeAction() != 0) {
            try {
                Thread.sleep(androidSettings.getArtificialDelayBeforeAction());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void scrollToMakeElementVisible(MobileElement wrappedElement) {
        // createBy default scroll down to make the element visible.
        if (androidSettings.getAutomaticallyScrollToVisible()) {
            scrollToVisible(wrappedElement, false);
        }
    }

    private void scrollToVisible(MobileElement wrappedElement, boolean shouldWait) {
        SCROLLING_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
        try {
            var action = new Actions(wrappedDriver);
            action.moveToElement(wrappedElement).perform();
            if (shouldWait) {
                Thread.sleep(500);
                toExists().waitToBe();
            }
        } catch (ElementNotInteractableException | InterruptedException ex) {
            DebugInformation.printStackTrace(ex);
        }

        SCROLLED_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
    }
}
