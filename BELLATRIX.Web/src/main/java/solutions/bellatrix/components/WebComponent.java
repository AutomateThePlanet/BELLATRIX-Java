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

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.configuration.ConfigurationService;
import solutions.bellatrix.configuration.WebSettings;
import solutions.bellatrix.findstrategies.FindStrategy;
import solutions.bellatrix.services.BrowserService;
import solutions.bellatrix.services.ComponentCreateService;
import solutions.bellatrix.services.ComponentWaitService;
import solutions.bellatrix.services.JavaScriptService;
import solutions.bellatrix.waitstrategies.Wait;
import solutions.bellatrix.waitstrategies.WaitStrategy;

import java.util.ArrayList;
import java.util.List;

public class WebComponent {
    private WebSettings webSettings;
    @Getter  private WebElement wrappedElement;
    @Getter private WebElement parentWrappedElement;
    @Getter private int elementIndex;
    @Getter private WebDriver wrappedDriver;

    // TODO: set elementName and pageName
    @Getter private String elementName;
    @Getter private String pageName;
    @Getter private FindStrategy findStrategy;
    private List<WaitStrategy> waitStrategies;
    @Getter protected JavaScriptService javaScriptService;
    @Getter protected BrowserService browserService;
    @Getter protected ComponentCreateService componentCreateService;
    @Getter protected ComponentWaitService componentWaitService;

    public WebComponent(FindStrategy findStrategy) {
        this(findStrategy, 0, null);
    }

    public WebComponent(FindStrategy by, int elementIndex, WebElement parentWrappedElement) {
        this.parentWrappedElement = parentWrappedElement;
        this.elementIndex = elementIndex;
        this.findStrategy = findStrategy;
        this.waitStrategies = new ArrayList<>();
        webSettings = ConfigurationService.get(WebSettings.class);
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

    public String getAttribute(String name) {
        return findElement().getAttribute(name);
    }

    public void ensureState(WaitStrategy waitStrategy) {
        waitStrategies.add(waitStrategy);
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
