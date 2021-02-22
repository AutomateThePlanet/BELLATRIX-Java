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

package solutions.bellatrix.desktop.components;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import layout.LayoutComponentValidationsBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.desktop.components.contracts.Component;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.findstrategies.*;
import solutions.bellatrix.desktop.infrastructure.DriverService;
import solutions.bellatrix.desktop.services.AppService;
import solutions.bellatrix.desktop.services.ComponentCreateService;
import solutions.bellatrix.desktop.services.ComponentWaitService;
import solutions.bellatrix.desktop.waitstrategies.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class DesktopComponent extends LayoutComponentValidationsBuilder implements Component {
    public final static EventListener<ComponentActionEventArgs> HOVERING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> HOVERED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLING_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLED_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> RETURNING_WRAPPED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING_ELEMENTS = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED_ELEMENTS = new EventListener<>();

    @Getter @Setter(AccessLevel.PROTECTED) private WindowsElement wrappedElement;
    @Getter @Setter private WindowsElement parentWrappedElement;
    @Getter @Setter private int elementIndex;
    @Getter @Setter private FindStrategy findStrategy;
    @Getter private WindowsDriver<WindowsElement> wrappedDriver;
    @Getter protected AppService appService;
    @Getter protected ComponentCreateService componentCreateService;
    @Getter protected ComponentWaitService componentWaitService;
    private List<WaitStrategy> waitStrategies;
    private DesktopSettings desktopSettings;

    public DesktopComponent() {
        this.waitStrategies = new ArrayList<>();
        desktopSettings = ConfigurationService.get(DesktopSettings.class);
        appService = new AppService();
        componentCreateService = new ComponentCreateService();
        componentWaitService = new ComponentWaitService();
        wrappedDriver = DriverService.getWrappedDriver();
    }

    public String getElementName() {
        return String.format("%s (%s)", getComponentClass().getSimpleName(), findStrategy.toString());
    }

    public void waitToBe() {
        findElement();
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this));
        wrappedDriver.getMouse().mouseMove(findElement().getCoordinates());
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

    public <TElementType extends DesktopComponent> TElementType toExists() {
        var waitStrategy = new ToExistsWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends DesktopComponent> TElementType toBeClickable() {
        var waitStrategy = new ToBeClickableWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends DesktopComponent> TElementType toBeVisible() {
        var waitStrategy = new ToBeVisibleWaitStrategy();
        ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends DesktopComponent, TWaitStrategy extends WaitStrategy> TElementType to(Class<TWaitStrategy> waitClass, TElementType element) {
        var waitStrategy = InstanceFactory.create(waitClass);
        element.ensureState(waitStrategy);
        return element;
    }

    public <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return create(componentClass, findStrategy);
    }

    public <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return createAll(componentClass, findStrategy);
    }

    public <TComponent extends DesktopComponent> TComponent createByAccessibilityId(Class<TComponent> componentClass, String accessibilityId) {
        return create(componentClass, new AccessibilityIdFindStrategy(accessibilityId));
    }

    public <TComponent extends DesktopComponent> TComponent createByClass(Class<TComponent> componentClass, String cclass) {
        return create(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends DesktopComponent> TComponent createByXPath(Class<TComponent> componentClass, String xpath) {
        return create(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends DesktopComponent> TComponent createByName(Class<TComponent> componentClass, String name) {
        return create(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends DesktopComponent> TComponent createByTag(Class<TComponent> componentClass, String tag) {
        return create(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends DesktopComponent> TComponent createByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return create(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends DesktopComponent> TComponent createByAutomationId(Class<TComponent> componentClass, String automationId) {
        return create(componentClass, new AutomationIdFindStrategy(automationId));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByAccessibilityId(Class<TComponent> componentClass, String accessibilityId) {
        return createAll(componentClass, new AccessibilityIdFindStrategy(accessibilityId));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByName(Class<TComponent> componentClass, String name) {
        return createAll(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByClass(Class<TComponent> componentClass, String cclass) {
        return createAll(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByXPath(Class<TComponent> componentClass, String xpath) {
        return createAll(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByAutomationId(Class<TComponent> componentClass, String automationId) {
        return createAll(componentClass, new AutomationIdFindStrategy(automationId));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByTag(Class<TComponent> componentClass, String tag) {
        return createAll(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends DesktopComponent> List<TComponent> createAllByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return createAll(componentClass, new IdContainingFindStrategy(idContaining));
    }

    protected <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        component.setParentWrappedElement(wrappedElement);
        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    protected <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var nativeElements = findStrategy.findAllElements(wrappedElement);
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

    protected WindowsElement findElement() {
      if (waitStrategies.stream().count() == 0) {
          waitStrategies.add(Wait.to().exists());
      }

      try {
          for (var waitStrategy:waitStrategies) {
              componentWaitService.wait(this, waitStrategy);
          }

          wrappedElement = findNativeElement();
          scrollToMakeElementVisible(wrappedElement);
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
        wrappedElement.click();

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected Boolean defaultGetDisabledAttribute() {
        var valueAttr = Optional.ofNullable(getAttribute("disabled")).orElse("false");
        return valueAttr.toLowerCase(Locale.ROOT) == "true";
    }

    protected String defaultGetText() {
        return Optional.ofNullable(findElement().getText()).orElse("");
    }

    protected void defaultSetText(EventListener<ComponentActionEventArgs> settingValue, EventListener<ComponentActionEventArgs> valueSet, String value)
    {
        settingValue.broadcast(new ComponentActionEventArgs(this));

        findElement().clear();
        findElement().sendKeys(value);

        valueSet.broadcast(new ComponentActionEventArgs(this));
    }

    private WindowsElement findNativeElement() {
        if (parentWrappedElement == null) {
            return findStrategy.findAllElements(wrappedDriver).get(elementIndex);
        } else {
            return (WindowsElement)findStrategy.findElement(parentWrappedElement);
        }
    }

    private void addArtificialDelay() {
        if (desktopSettings.getArtificialDelayBeforeAction() != 0)
        {
            try {
                Thread.sleep(desktopSettings.getArtificialDelayBeforeAction());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void scrollToMakeElementVisible(WindowsElement wrappedElement) {
        // createBy default scroll down to make the element visible.
        if (desktopSettings.getAutomaticallyScrollToVisible()) {
            scrollToVisible(wrappedElement, false);
        }
    }

    private void scrollToVisible(WindowsElement wrappedElement, Boolean shouldWait)
    {
        SCROLLING_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
        try {
            var action = new Actions(wrappedDriver);
            action.moveToElement(wrappedElement).perform();
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

    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    protected void defaultValidateAttributeSet(Supplier<String> supplier, String attributeName) {
        waitUntil((d) -> !StringUtils.isEmpty(supplier.get()), String.format("The control's %s shouldn't be empty but was.", attributeName));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, null, String.format("validate %s is empty", attributeName)));
    }

    protected void defaultValidateAttributeNotSet(Supplier<String> supplier, String attributeName) {
        waitUntil((d) -> StringUtils.isEmpty(supplier.get()), String.format("The control's %s should be null but was '%s'.", attributeName, supplier.get()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, null, String.format("validate %s is null", attributeName)));
    }

    protected void defaultValidateAttributeIs(Supplier<String> supplier, String value, String attributeName) {
        waitUntil((d) -> supplier.get().strip().equals(value), String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, supplier.get()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, String.format("validate %s is %s", attributeName, value)));
    }

    protected void defaultValidateAttributeContains(Supplier<String> supplier, String value, String attributeName) {
        waitUntil((d) -> supplier.get().strip().contains(value), String.format("The control's %s should contain '%s' but was '%s'.", attributeName, value, supplier.get()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, String.format("validate %s contains %s", attributeName, value)));
    }

    protected void defaultValidateAttributeNotContains(Supplier<String> supplier, String value, String attributeName) {
        waitUntil((d) -> !supplier.get().strip().contains(value), String.format("The control's %s shouldn't contain '%s' but was '%s'.", attributeName, value, supplier.get()));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(this, value, String.format("validate %s doesn't contain %s", attributeName, value)));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), desktopSettings.getTimeoutSettings().getValidationsTimeout(), desktopSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            DebugInformation.printStackTrace(ex);
            throw ex;
        }
    }
}
