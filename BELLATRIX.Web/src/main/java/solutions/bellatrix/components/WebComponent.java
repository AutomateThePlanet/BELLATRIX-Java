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

package solutions.bellatrix.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import org.openqa.selenium.*;
import solutions.bellatrix.components.contracts.Component;
import solutions.bellatrix.configuration.ConfigurationService;
import solutions.bellatrix.configuration.WebSettings;
import solutions.bellatrix.findstrategies.FindStrategy;
import solutions.bellatrix.plugins.EventListener;
import solutions.bellatrix.services.BrowserService;
import solutions.bellatrix.services.ComponentCreateService;
import solutions.bellatrix.services.ComponentWaitService;
import solutions.bellatrix.services.JavaScriptService;
import solutions.bellatrix.utilities.InstanceFactory;
import solutions.bellatrix.waitstrategies.*;

import java.util.ArrayList;
import java.util.List;

@ExtensionMethod({WebComponent.class, WaitStrategyElementsExtensions.class})
public class WebComponent implements Component {
    private WebSettings webSettings;
    @Getter  @Setter(AccessLevel.PROTECTED) private WebElement wrappedElement;
    @Getter @Setter private WebElement parentWrappedElement;
    @Getter @Setter private int elementIndex;
    @Getter @Setter private FindStrategy findStrategy;
    @Getter private WebDriver wrappedDriver;

    // TODO: set elementName and pageName
    @Getter private String elementName;
    @Getter private String pageName;

    private List<WaitStrategy> waitStrategies;
    @Getter protected JavaScriptService javaScriptService;
    @Getter protected BrowserService browserService;
    @Getter protected ComponentCreateService componentCreateService;
    @Getter protected ComponentWaitService componentWaitService;

    public WebComponent() {
        this.waitStrategies = new ArrayList<>();
        webSettings = ConfigurationService.get(WebSettings.class);
        javaScriptService = new JavaScriptService();
        browserService = new BrowserService();
        componentCreateService = new ComponentCreateService();
        componentWaitService = new ComponentWaitService();
    }

//    public WebComponent(FindStrategy findStrategy) {
//        this(findStrategy, 0, null);
//    }
//
//    public WebComponent(FindStrategy by, int elementIndex, WebElement parentWrappedElement) {
//        this.parentWrappedElement = parentWrappedElement;
//        this.elementIndex = elementIndex;
//        this.findStrategy = findStrategy;
//        this.waitStrategies = new ArrayList<>();
//        webSettings = ConfigurationService.get(WebSettings.class);
//    }

    public void waitToBe() {
        findElement();
    }

    public void scrollToVisible() {
        scrollToVisible(findElement(), false);
    }

    public void setAttribute(String name, String value) {
//        SettingAttribute?.Invoke(this, new ElementActionEventArgs(this));
        javaScriptService.execute(String.format("arguments[0].setAttribute('%s', '%s');", name, value), this);

//        AttributeSet?.Invoke(this, new ElementActionEventArgs(this));
    }

    public void focus() {
//        Focusing?.Invoke(this, new ElementActionEventArgs(this));
        javaScriptService.execute("window.focus();");
        javaScriptService.execute("arguments[0].focus();", findElement());
//        Focused?.Invoke(this, new ElementActionEventArgs(this));
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

    public <TElementType extends WebComponent> TElementType toExists(TElementType element) {
        var waitStrategy = new ToExistsWaitStrategy();
        element.ensureState(waitStrategy);
        return element;
    }

    public <TElementType extends WebComponent> TElementType toBeClickable(TElementType element) {
        var waitStrategy = new ToBeClickableWaitStrategy();
        element.ensureState(waitStrategy);
        return element;
    }

    public <TElementType extends WebComponent> TElementType toBeVisible(TElementType element) {
        var waitStrategy = new ToBeVisibleWaitStrategy();
        element.ensureState(waitStrategy);
        return element;
    }

    public <TElementType extends WebComponent, TWaitStrategy extends WaitStrategy> TElementType to(Class<TWaitStrategy> waitClass, TElementType element) {
        var waitStrategy = InstanceFactory.create(waitClass);
        element.ensureState(waitStrategy);
        return element;
    }

    protected WebElement findElement() {
      if (waitStrategies.stream().count() == 0) {
          waitStrategies.add(Wait.to().exists());
      }

      WebElement wrappedElement = null;
      try {
          for (var waitStrategy:waitStrategies) {
              componentWaitService.wait(this, waitStrategy);
          }

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
          System.out.print(String.format("\n\nThe element: \n Name: '%s', \n Locator: '%s = %s', \n Type: '%s' \nWas not found on the page or didn't fulfill the specified conditions.\n\n", elementName, findStrategy.toString(), findStrategy.getValue(), getClass().getName()));
      }

        return wrappedElement;
    }


    protected void click(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked)
    {
        clicking.broadcast(new ComponentActionEventArgs(this));

        this.toExists(this).toBeClickable(this).waitToBe();
        javaScriptService.execute("arguments[0].focus();arguments[0].click();", this);

        clicked.broadcast(new ComponentActionEventArgs(this));
    }
//
//    internal void Hover(EventHandler<ElementActionEventArgs> hovering, EventHandler<ElementActionEventArgs> hovered)
//    {
//        hovering?.Invoke(this, new ElementActionEventArgs(this));
//
//        JavaScriptService.Execute("arguments[0].onmouseover();", this);
//
//        hovered?.Invoke(this, new ElementActionEventArgs(this));
//    }
//
//    internal string GetInnerText()
//    {
//        return WrappedElement.Text.Replace("\r\n", string.Empty);
//    }
//
//    internal void SetValue(EventHandler<ElementActionEventArgs> gettingValue, EventHandler<ElementActionEventArgs> gotValue, string value)
//    {
//        gettingValue?.Invoke(this, new ElementActionEventArgs(this, value));
//        SetAttribute("value", value);
//        gotValue?.Invoke(this, new ElementActionEventArgs(this, value));
//    }
//
//    internal string DefaultGetValue()
//    {
//        return WrappedElement.GetAttribute("value");
//    }
//
//    internal int? DefaultGetMaxLength()
//    {
//        int? result = string.IsNullOrEmpty(GetAttribute("maxlength")) ? null : (int?)int.Parse(GetAttribute("maxlength"));
//        if (result != null && (result == 2147483647 || result == -1))
//        {
//            result = null;
//        }
//
//        return result;
//    }
//
//    internal int? DefaultGetMinLength()
//    {
//        int? result = string.IsNullOrEmpty(GetAttribute("minlength")) ? null : (int?)int.Parse(GetAttribute("minlength"));
//
//        if (result != null && result == -1)
//        {
//            result = null;
//        }
//
//        return result;
//    }
//
//    internal int? GetSizeAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("size")) ? null : (int?)int.Parse(GetAttribute("size"));
//    }
//
//    internal int? GetHeightAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("height")) ? null : (int?)int.Parse(GetAttribute("height"));
//    }
//
//    internal int? GetWidthAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("width")) ? null : (int?)int.Parse(GetAttribute("width"));
//    }
//
//    internal string GetInnerHtmlAttribute()
//    {
//        return WrappedElement.GetAttribute("innerHTML");
//    }
//
//    internal string GetForAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("for")) ? null : GetAttribute("for");
//    }
//
//    protected bool GetDisabledAttribute()
//    {
//        string valueAttr = WrappedElement.GetAttribute("disabled");
//        return valueAttr == "true";
//    }
//
//    internal string GetText()
//    {
//        return WrappedElement.Text;
//    }
//
//    internal int? GetMinAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("min")) ? null : (int?)int.Parse(GetAttribute("min"));
//    }
//
//    internal int? GetMaxAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("max")) ? null : (int?)int.Parse(GetAttribute("max"));
//    }
//
//    internal string GetMinAttributeAsString()
//    {
//        return string.IsNullOrEmpty(GetAttribute("min")) ? null : GetAttribute("min");
//    }
//
//    internal string GetMaxAttributeAsString()
//    {
//        return string.IsNullOrEmpty(GetAttribute("max")) ? null : GetAttribute("max");
//    }
//
//    internal int? GetStepAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("step")) ? null : (int?)int.Parse(GetAttribute("step"));
//    }
//
//    internal string GetPlaceholderAttribute()
//    {
//        return string.IsNullOrEmpty(GetAttribute("placeholder")) ? null : GetAttribute("placeholder");
//    }
//
//    internal bool GetAutoCompleteAttribute()
//    {
//        return GetAttribute("autocomplete") == "on";
//    }
//
//    internal bool GetReadonlyAttribute()
//    {
//        return !string.IsNullOrEmpty(GetAttribute("readonly"));
//    }
//
//    internal bool GetRequiredAttribute()
//    {
//        return !string.IsNullOrEmpty(GetAttribute("required"));
//    }
//
//    internal string GetList()
//    {
//        return string.IsNullOrEmpty(GetAttribute("list")) ? null : GetAttribute("list");
//    }
//
//    internal void DefaultSetText(EventHandler<ElementActionEventArgs> settingValue, EventHandler<ElementActionEventArgs> valueSet, string value)
//    {
//        settingValue?.Invoke(this, new ElementActionEventArgs(this, value));
//
//        findElement().clear();
//        findElement().sendKeys(value);
//
//        valueSet?.Invoke(this, new ElementActionEventArgs(this, value));
//    }

    private WebElement findNativeElement()
    {
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
        // By default scroll down to make the element visible.
        if (webSettings.getAutomaticallyScrollToVisible()) {
            scrollToVisible(wrappedElement, false);
        }
    }

    private void scrollToVisible(WebElement wrappedElement, Boolean shouldWait)
    {
        //ScrollingToVisible?.Invoke(this, new ElementActionEventArgs(this));
        try {
            javaScriptService.execute("arguments[0].scrollIntoView(true);", wrappedElement);
            if (shouldWait)
            {
                Thread.sleep(500);
                //this.ToExists().WaitToBe();
            }
        } catch (ElementNotInteractableException ex) {
            System.out.print(ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //ScrolledToVisible?.Invoke(this, new ElementActionEventArgs(this));
    }
}
