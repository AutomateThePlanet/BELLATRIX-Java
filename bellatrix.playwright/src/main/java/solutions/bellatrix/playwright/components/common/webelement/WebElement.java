/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.playwright.components.common.webelement;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.SelectOption;
import lombok.Getter;
import solutions.bellatrix.playwright.components.common.webelement.options.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Wrapper for Playwright Locator.
 */
@Getter
public class WebElement {
    public WebElement(List<Locator> locators) {
        wrappedLocators = locators;
        wrappedLocator = wrappedLocators.get(0);
    }

    public WebElement(Locator locator) {
        wrappedLocator = locator;
        wrappedLocators = wrappedLocator.all();
    }

    private final Locator wrappedLocator;
    private final List<Locator> wrappedLocators;

    public WebElement getByAltText(String text, GetByAltTextOptions options) {
        return new WebElement(wrappedLocator.getByAltText(text, options.convertTo(Locator.GetByAltTextOptions.class)));
    }

    public WebElement getByAltText(String text) {
        return new WebElement(wrappedLocator.getByAltText(text));
    }

    public WebElement getByAltText(Pattern text, GetByAltTextOptions options) {
        return new WebElement(wrappedLocator.getByAltText(text, options.convertTo(Locator.GetByAltTextOptions.class)));
    }

    public WebElement getByAltText(Pattern text) {
        return new WebElement(wrappedLocator.getByAltText(text));
    }

    public WebElement getByLabel(String text, GetByLabelOptions options) {
        return new WebElement(wrappedLocator.getByLabel(text, options.convertTo(Locator.GetByLabelOptions.class)));
    }

    public WebElement getByLabel(String text) {
        return new WebElement(wrappedLocator.getByLabel(text));
    }

    public WebElement getByLabel(Pattern text, GetByLabelOptions options) {
        return new WebElement(wrappedLocator.getByLabel(text, options.convertTo(Locator.GetByLabelOptions.class)));
    }

    public WebElement getByLabel(Pattern text) {
        return new WebElement(wrappedLocator.getByLabel(text));
    }

    public WebElement getByPlaceholder(String text, GetByPlaceholderOptions options) {
        return new WebElement(wrappedLocator.getByPlaceholder(text, options.convertTo(Locator.GetByPlaceholderOptions.class)));
    }

    public WebElement getByPlaceholder(String text) {
        return new WebElement(wrappedLocator.getByPlaceholder(text));
    }

    public WebElement getByPlaceholder(Pattern text, GetByPlaceholderOptions options) {
        return new WebElement(wrappedLocator.getByPlaceholder(text, options.convertTo(Locator.GetByPlaceholderOptions.class)));
    }

    public WebElement getByPlaceholder(Pattern text) {
        return new WebElement(wrappedLocator.getByPlaceholder(text));
    }

    public WebElement getByRole(AriaRole role, GetByRoleOptions options) {
        return new WebElement(wrappedLocator.getByRole(role, options.convertTo(Locator.GetByRoleOptions.class)));
    }

    public WebElement getByRole(AriaRole role) {
        return new WebElement(wrappedLocator.getByRole(role));
    }

    public WebElement getByText(String text, GetByTextOptions options) {
        return new WebElement(wrappedLocator.getByText(text, options.convertTo(Locator.GetByTextOptions.class)));
    }

    public WebElement getByText(String text) {
        return new WebElement(wrappedLocator.getByText(text));
    }

    public WebElement getByText(Pattern text, GetByTextOptions options) {
        return new WebElement(wrappedLocator.getByText(text, options.convertTo(Locator.GetByTextOptions.class)));
    }

    public WebElement getByText(Pattern text) {
        return new WebElement(wrappedLocator.getByText(text));
    }

    public WebElement getByTitle(String text, GetByTitleOptions options) {
        return new WebElement(wrappedLocator.getByTitle(text, options.convertTo(Locator.GetByTitleOptions.class)));
    }

    public WebElement getByTitle(String text) {
        return new WebElement(wrappedLocator.getByTitle(text));
    }

    public WebElement getByTitle(Pattern text, GetByTitleOptions options) {
        return new WebElement(wrappedLocator.getByTitle(text, options.convertTo(Locator.GetByTitleOptions.class)));
    }

    public WebElement getByTitle(Pattern text) {
        return new WebElement(wrappedLocator.getByTitle(text));
    }

    public WebElement locate(String selector) {
        return new WebElement(wrappedLocator.locator(selector));
    }

    public WebElement locate(WebElement webElement) {
        return new WebElement(wrappedLocator.locator(webElement.wrappedLocator));
    }

    public FrameElement locateFrame(String selector) {
        return new FrameElement(locate(selector));
    }

    public FrameElement locateFrame() {
        return new FrameElement(this);
    }

    public List<WebElement> all() {
        List<WebElement> elements = new ArrayList<>();
        for (Locator locator : wrappedLocators) {
            elements.add(new WebElement(locator));
        }

        return elements;
    }

    public List<String> allInnerTexts() {
        return wrappedLocator.allInnerTexts();
    }

    public List<String> allTextContents() {
        return wrappedLocator.allTextContents();
    }

    public WebElement and(WebElement locator) {
        return new WebElement(wrappedLocator.and(locator.wrappedLocator));
    }

    public void blur(Locator.BlurOptions options) {
        wrappedLocator.blur(options);
    }

    public void blur() {
        wrappedLocator.blur();
    }

    public BoundingBox boundingBox(Locator.BoundingBoxOptions options) {
        return wrappedLocator.boundingBox(options);
    }

    public BoundingBox boundingBox() {
        return wrappedLocator.boundingBox();
    }

    public void check(Locator.CheckOptions options) {
        wrappedLocator.check(options);
    }

    public void check() {
        wrappedLocator.check();
    }

    public void clear(Locator.ClearOptions options) {
        wrappedLocator.clear(options);
    }

    public void clear() {
        wrappedLocator.clear();
    }

    public void click(Locator.ClickOptions options) {
        wrappedLocator.click(options);
    }

    public void click() {
        wrappedLocator.click();
    }

    public int count() {
        return wrappedLocator.count();
    }

    public void dblclick(Locator.DblclickOptions options) {
        wrappedLocator.dblclick(options);
    }

    public void dblclick() {
        wrappedLocator.dblclick();
    }

    public void dispatchEvent(String type, Object eventInit, Locator.DispatchEventOptions options) {
        wrappedLocator.dispatchEvent(type, eventInit, options);
    }

    public void dispatchEvent(String type, Object eventInit) {
        wrappedLocator.dispatchEvent(type, eventInit);
    }

    public void dragTo(WebElement target, Locator.DragToOptions options) {
        wrappedLocator.dragTo(target.wrappedLocator, options);
    }

    public void dragTo(WebElement target) {
        wrappedLocator.dragTo(target.wrappedLocator);
    }


    public ElementHandle elementHandle(Locator.ElementHandleOptions options) {
        return wrappedLocator.elementHandle(options);
    }

    public ElementHandle elementHandle() {
        return wrappedLocator.elementHandle();
    }

    public List<ElementHandle> elementHandles() {
        return wrappedLocator.elementHandles();
    }

    public Object evaluate(String expression, Object arg, Locator.EvaluateOptions options) {
        return wrappedLocator.evaluate(expression, arg, options);
    }

    public Object evaluate(String expression, Object arg) {
        return wrappedLocator.evaluate(expression, arg);
    }

    public Object evaluate(String expression) {
        return wrappedLocator.evaluate(expression);
    }

    public Object evaluateAll(String expression, Object arg) {
        return wrappedLocator.evaluate(expression, arg);
    }

    public Object evaluateAll(String expression) {
        return wrappedLocator.evaluate(expression);
    }

    public JSHandle evaluateHandle(String expression, Object arg, Locator.EvaluateHandleOptions options) {
        return wrappedLocator.evaluateHandle(expression, arg, options);
    }

    public JSHandle evaluateHandle(String expression, Object arg) {
        return wrappedLocator.evaluateHandle(expression, arg);
    }

    public JSHandle evaluateHandle(String expression) {
        return wrappedLocator.evaluateHandle(expression);
    }


    public void fill(String value, Locator.FillOptions options) {
        wrappedLocator.fill(value, options);
    }

    public void fill(String value) {
        wrappedLocator.fill(value);
    }

    public WebElement filter(Locator.FilterOptions options) {
        return new WebElement(wrappedLocator.filter(options));
    }

    public WebElement filter() {
        return new WebElement(wrappedLocator.filter());
    }

    public WebElement first() {
        return new WebElement(wrappedLocators.get(0));
    }

    public void focus(Locator.FocusOptions options) {
        wrappedLocator.focus(options);
    }

    public void focus() {
        wrappedLocator.focus();
    }

    public String getAttribute(String name, Locator.GetAttributeOptions options) {
        return wrappedLocator.getAttribute(name, options);
    }

    public String getAttribute(String name) {
        return wrappedLocator.getAttribute(name);
    }

    public void highlight() {
        wrappedLocator.highlight();
    }

    public void hover(Locator.HoverOptions options) {
        wrappedLocator.hover(options);
    }

    public void hover() {
        wrappedLocator.hover();
    }

    public String innerHTML(Locator.InnerHTMLOptions options) {
        return wrappedLocator.innerHTML(options);
    }

    public String innerHTML() {
        return wrappedLocator.innerHTML();
    }

    public String innerText(Locator.InnerTextOptions options) {
        return wrappedLocator.innerText(options);
    }

    public String innerText() {
        return wrappedLocator.innerText();
    }

    public String inputValue(Locator.InputValueOptions options) {
        return wrappedLocator.inputValue(options);
    }

    public String inputValue() {
        return wrappedLocator.inputValue();
    }

    public boolean isChecked(Locator.IsCheckedOptions options) {
        return wrappedLocator.isChecked(options);
    }

    public boolean isChecked() {
        return wrappedLocator.isChecked();
    }


    public boolean isDisabled(Locator.IsDisabledOptions options) {
        return wrappedLocator.isDisabled(options);
    }

    public boolean isDisabled() {
        return wrappedLocator.isDisabled();
    }

    public boolean isEditable(Locator.IsEditableOptions options) {
        return wrappedLocator.isEditable(options);
    }

    public boolean isEditable() {
        return wrappedLocator.isEditable();
    }

    public boolean isEnabled(Locator.IsEnabledOptions options) {
        return wrappedLocator.isEnabled(options);
    }

    public boolean isEnabled() {
        return wrappedLocator.isEnabled();
    }

    public boolean isHidden(Locator.IsHiddenOptions options) {
        return wrappedLocator.isHidden(options);
    }

    public boolean isHidden() {
        return wrappedLocator.isHidden();
    }

    public boolean isVisible(Locator.IsVisibleOptions options) {
        return wrappedLocator.isVisible(options);
    }

    public boolean isVisible() {
        return wrappedLocator.isVisible();
    }

    public WebElement last() {
        return new WebElement(wrappedLocators.get(wrappedLocator.count() - 1));
    }

    public WebElement nth(int index) {
        return new WebElement(wrappedLocators.get(index));
    }

    public WebElement or(WebElement webElement) {
        return new WebElement(wrappedLocator.or(webElement.wrappedLocator));
    }

    public Page page() {
        return wrappedLocator.page();
    }

    public void press(String key, Locator.PressOptions options) {
        wrappedLocator.press(key, options);
    }

    public void press(String key) {
        wrappedLocator.press(key);
    }

    public void pressSequentially(String text, Locator.PressSequentiallyOptions options) {
        wrappedLocator.pressSequentially(text, options);
    }

    public void pressSequentially(String text) {
        wrappedLocator.pressSequentially(text);
    }

    public byte[] screenshot(Locator.ScreenshotOptions options) {
        return wrappedLocator.screenshot(options);
    }

    public byte[] screenshot() {
        return wrappedLocator.screenshot();
    }

    public void scrollIntoViewIfNeeded(Locator.ScrollIntoViewIfNeededOptions options) {
        wrappedLocator.scrollIntoViewIfNeeded(options);
    }

    public void scrollIntoViewIfNeeded() {
        wrappedLocator.scrollIntoViewIfNeeded();
    }

    public List<String> selectOption(String values, Locator.SelectOptionOptions options) {
        return wrappedLocator.selectOption(values, options);
    }

    public List<String> selectOption(String values) {
        return wrappedLocator.selectOption(values);
    }

    public List<String> selectOption(ElementHandle values, Locator.SelectOptionOptions options) {
        return wrappedLocator.selectOption(values, options);
    }

    public List<String> selectOption(ElementHandle values) {
        return wrappedLocator.selectOption(values);
    }

    public List<String> selectOption(String[] values, Locator.SelectOptionOptions options) {
        return wrappedLocator.selectOption(values, options);
    }

    public List<String> selectOption(String[] values) {
        return wrappedLocator.selectOption(values);
    }

    public List<String> selectOption(SelectOption values, Locator.SelectOptionOptions options) {
        return wrappedLocator.selectOption(values, options);
    }

    public List<String> selectOption(SelectOption values) {
        return wrappedLocator.selectOption(values);
    }

    public List<String> selectOption(ElementHandle[] values, Locator.SelectOptionOptions options) {
        return wrappedLocator.selectOption(values, options);
    }

    public List<String> selectOption(ElementHandle[] values) {
        return wrappedLocator.selectOption(values);
    }

    public List<String> selectOption(SelectOption[] values, Locator.SelectOptionOptions options) {
        return wrappedLocator.selectOption(values, options);
    }

    public List<String> selectOption(SelectOption[] values) {
        return wrappedLocator.selectOption(values);
    }

    public void selectText(Locator.SelectTextOptions options) {
        wrappedLocator.selectText(options);
    }

    public void selectText() {
        wrappedLocator.selectText();
    }

    public void setChecked(boolean checked, Locator.SetCheckedOptions options) {
        wrappedLocator.setChecked(checked, options);
    }

    public void setChecked(boolean checked) {
        wrappedLocator.setChecked(checked);
    }

    public void setInputFiles(Path files, Locator.SetInputFilesOptions options) {
        wrappedLocator.setInputFiles(files, options);
    }

    public void setInputFiles(Path files) {
        wrappedLocator.setInputFiles(files);
    }

    public void setInputFiles(Path[] files, Locator.SetInputFilesOptions options) {
        wrappedLocator.setInputFiles(files, options);
    }

    public void setInputFiles(Path[] files) {
        wrappedLocator.setInputFiles(files);
    }


    public void setInputFiles(FilePayload files, Locator.SetInputFilesOptions options) {
        wrappedLocator.setInputFiles(files, options);
    }

    public void setInputFiles(FilePayload files) {
        wrappedLocator.setInputFiles(files);
    }


    public void setInputFiles(FilePayload[] files, Locator.SetInputFilesOptions options) {
        wrappedLocator.setInputFiles(files, options);
    }

    public void setInputFiles(FilePayload[] files) {
        wrappedLocator.setInputFiles(files);
    }

    public void tap(Locator.TapOptions options) {
        wrappedLocator.tap(options);
    }

    public void tap() {
        wrappedLocator.tap();
    }

    public String textContent(Locator.TextContentOptions options) {
        return wrappedLocator.textContent(options);
    }

    public String textContent() {
        return wrappedLocator.textContent();
    }

    /**
     * Deprecated, because the playwright native method Locator.type() is deprecated.
     */
    @Deprecated
    public void type(String text, Locator.TypeOptions options) {
        wrappedLocator.type(text, options);
    }

    /**
     * Deprecated, because the playwright native method Locator.type() is deprecated.
     */
    @Deprecated
    public void type(String text) {
        wrappedLocator.type(text);
    }

    public void uncheck(Locator.UncheckOptions options) {
        wrappedLocator.uncheck(options);
    }

    public void uncheck() {
        wrappedLocator.uncheck();
    }


    public void waitFor(Locator.WaitForOptions options) {
        wrappedLocator.waitFor(options);
    }

    public void waitFor() {
        wrappedLocator.waitFor();
    }

    public String toString() {
        return wrappedLocator.toString();
    }
}
