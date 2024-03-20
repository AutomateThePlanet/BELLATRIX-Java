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

package solutions.bellatrix.playwright.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import layout.LayoutComponentValidationsBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.platform.commons.util.StringUtils;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.components.common.create.RelativeCreateService;
import solutions.bellatrix.playwright.components.common.validate.Validator;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.components.contracts.Component;
import solutions.bellatrix.playwright.components.contracts.ComponentVisible;
import solutions.bellatrix.playwright.components.options.actions.CheckOptions;
import solutions.bellatrix.playwright.components.options.actions.ClickOptions;
import solutions.bellatrix.playwright.components.options.actions.HoverOptions;
import solutions.bellatrix.playwright.components.options.actions.UncheckOptions;
import solutions.bellatrix.playwright.components.options.states.BoundingBoxOptions;
import solutions.bellatrix.playwright.configuration.WebSettings;
import solutions.bellatrix.playwright.findstrategies.*;
import solutions.bellatrix.playwright.findstrategies.options.*;
import solutions.bellatrix.playwright.infrastructure.BrowserChoice;
import solutions.bellatrix.playwright.infrastructure.PlaywrightService;
import solutions.bellatrix.playwright.infrastructure.WrappedBrowser;
import solutions.bellatrix.playwright.utilities.Settings;

import java.awt.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;


@SuppressWarnings({"unchecked", "SameParameterValue", "unused"})
public class WebComponent extends LayoutComponentValidationsBuilder implements Component, ComponentVisible {
    public final static EventListener<ComponentActionEventArgs> HOVERING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> HOVERED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> FOCUSING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> FOCUSED = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLING_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SCROLLED_TO_VISIBLE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> SETTING_ATTRIBUTE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> ATTRIBUTE_SET = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> RETURNING_WRAPPED_ELEMENT = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CREATED = new EventListener<>();

    private final WebSettings webSettings;
    @Getter @Setter private WebComponent parentWrappedComponent;
    @Getter @Setter private int elementIndex;
    @Getter @Setter protected FindStrategy findStrategy;
    protected WebElement wrappedElement;
    protected RelativeCreateService createService;

    public WebComponent() {
        createService = new RelativeCreateService(this, CREATING, CREATED);
        webSettings = Settings.web();
    }

    public WebComponent(WebElement locator) {
        this.wrappedElement = locator;

        if (getComponentClass().equals(Frame.class)) {
            wrappedElement.isFrame(true);
        }

        createService = new RelativeCreateService(this, CREATING, CREATED);
        webSettings = Settings.web();
    }

    public RelativeCreateService create() {
        return createService;
    }

    protected static WrappedBrowser wrappedBrowser() {
        return PlaywrightService.wrappedBrowser();
    }

    public WebElement wrappedElement() {
        return wrappedElement;
    }

    public void wrappedElement(WebElement element) {
        this.wrappedElement = element;
    }

    public void scrollToVisible() {
        SCROLLING_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
        wrappedElement().scrollIntoViewIfNeeded();
        SCROLLED_TO_VISIBLE.broadcast(new ComponentActionEventArgs(this));
    }

    public void focus() {
        FOCUSING.broadcast(new ComponentActionEventArgs(this));
        wrappedElement().focus();
        FOCUSED.broadcast(new ComponentActionEventArgs(this));
    }

    public void hover() {
        HOVERING.broadcast(new ComponentActionEventArgs(this));
        internalHover(null);
        HOVERED.broadcast(new ComponentActionEventArgs(this));
    }

    public void hover(Function<Locator.HoverOptions, Locator.HoverOptions> options) {
        HOVERING.broadcast(new ComponentActionEventArgs(this));
        internalHover(HoverOptions.create(options));
        HOVERED.broadcast(new ComponentActionEventArgs(this));
    }

    private void internalHover(HoverOptions options) {
        if (options == null) {
            wrappedElement().hover();
        } else {
            wrappedElement().hover(options.convert());
        }
    }

    public Class<?> getComponentClass() {
        return this.getClass();
    }

    public String getComponentName() {
        return String.format("%s (%s)", getComponentClass().getSimpleName(), findStrategy.toString());
    }

    public String getTagName() {
        return String.valueOf(wrappedElement.evaluate("element => element.TagName;"));
    }

    public WebElement findLocator() {
        if (webSettings.getAutomaticallyScrollToVisible()) {
            scrollToVisible();
        }

        return wrappedElement;
    }

    @Override
    public Point getLocation() {
        return new Point((int)getBoundingBox().x, (int)getBoundingBox().y);
    }

    @Override
    public Dimension getSize() {
        return new Dimension((int)getBoundingBox().width, (int)getBoundingBox().height);
    }

    public BoundingBox getBoundingBox() {
        return wrappedElement.boundingBox();
    }

    public BoundingBox getBoundingBox(Function<Locator.BoundingBoxOptions, Locator.BoundingBoxOptions> options) {
        return wrappedElement.boundingBox(BoundingBoxOptions.create(options).convert());
    }

    public String getAttribute(String name) {
        return findLocator().getAttribute(name);
    }

    @Override
    public boolean isVisible() {
        return findLocator().isVisible();
    }

    // ToDo highlight() method requires testing;
    // Update: We should use js, native playwright highlight() method's function is unknown.
    // Update: the js script seems to work, but not for TextField elements
    public void highlight() {
        if (PlaywrightService.browserConfiguration().browserChoice() == BrowserChoice.CHROME_HEADLESS) {
            return;
        }

        try {
            var originalElementBackground = wrappedElement.evaluate("el => window.getComputedStyle(el).getPropertyValue('background')");
            var originalElementColor = wrappedElement.evaluate("el => window.getComputedStyle(el).getPropertyValue('color')");
            var originalElementOutline = wrappedElement.evaluate("el => window.getComputedStyle(el).getPropertyValue('outline')");

            String script = "function(el) {" +
                    "    el.style.background='yellow';" +
                    "    el.style.color='black';" +
                    "    el.style.outline='1px solid black';" +
                    "    setTimeout(function() {" +
                    "        el.style.background='" + originalElementBackground + "';" +
                    "        el.style.color='" + originalElementColor + "';" +
                    "        el.style.outline='" + originalElementOutline + "';" +
                    "    }, 100);" +
                    "}";

            findLocator().evaluate(script);
        } catch (Exception ignored) {
        }
    }

    protected boolean defaultIsSelected(String selectSelector, String optionValue) {
        return (boolean)wrappedBrowser().currentPage().evaluate(
                String.format("%s option[value='%s']", selectSelector, optionValue),
                "option -> option.selected"
        );
    }

    protected String defaultGetValue() {
        return Optional.ofNullable(getAttribute("value")).orElse("");
    }

    protected void defaultSetValue(EventListener<ComponentActionEventArgs> gettingValue, EventListener<ComponentActionEventArgs> gotValue, String value) {
        gettingValue.broadcast(new ComponentActionEventArgs(this, value));
        findLocator().evaluate(String.format("el => el.value = '%s'", value));
        gotValue.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected String defaultGetName() {
        return Optional.ofNullable(getAttribute("name")).orElse("");
    }

    protected String defaultGetMaxLength() {
        return Optional.ofNullable(getAttribute("max")).orElse("");
    }

    protected String defaultGetMinLength() {
        return Optional.ofNullable(getAttribute("min")).orElse("");
    }

    protected String defaultGetSizeAttribute() {
        return Optional.ofNullable(getAttribute("size")).orElse("");
    }

    protected String defaultGetSizesAttribute() {
        return Optional.ofNullable(getAttribute("sizes")).orElse("");
    }

    protected String defaultGetSrcAttribute() {
        return Optional.ofNullable(getAttribute("src")).orElse("");
    }

    protected String defaultGetSrcSetAttribute() {
        return Optional.ofNullable(getAttribute("srcset")).orElse("");
    }

    protected String defaultGetAltAttribute() {
        return Optional.ofNullable(getAttribute("alt")).orElse("");
    }

    protected String defaultGetColsAttribute() {
        return Optional.ofNullable(getAttribute("cols")).orElse("");
    }

    protected String defaultGetRowsAttribute() {
        return Optional.ofNullable(getAttribute("rows")).orElse("");
    }

    protected String defaultGetLongDescAttribute() {
        return Optional.ofNullable(getAttribute("longdesc")).orElse("");
    }

    protected String defaultGetHeightAttribute() {
        return Optional.ofNullable(getAttribute("height")).orElse("");
    }

    protected String defaultGetWidthAttribute() {
        return Optional.ofNullable(getAttribute("width")).orElse("");
    }

    protected String defaultGetInnerHtmlAttribute() {
        return Optional.ofNullable(findLocator().innerHTML()).orElse("");
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

    protected boolean defaultGetDisabledAttribute() {
        return findLocator().isDisabled();
    }

    protected String defaultGetText() {
        return Optional.ofNullable(findLocator().innerText()).orElse("");
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

    protected String defaultGetWrapAttribute() {
        return Optional.ofNullable(getAttribute("wrap")).orElse("");
    }

    protected String defaultGetPlaceholderAttribute() {
        return Optional.ofNullable(getAttribute("placeholder")).orElse("");
    }

    protected String defaultGetAcceptAttribute() {
        return getAttribute("accept");
    }

    protected boolean defaultGetAutoCompleteAttribute() {
        return Optional.ofNullable(getAttribute("autocomplete")).orElse("").equals("on");
    }

    protected boolean defaultGetSpellCheckAttribute() {
        return Optional.ofNullable(getAttribute("spellcheck")).orElse("").equals("on");
    }

    protected boolean defaultGetReadonlyAttribute() {
        return StringUtils.isNotBlank(Optional.ofNullable(getAttribute("readonly")).orElse(""));
    }

    protected boolean defaultGetRequiredAttribute() {
        return StringUtils.isNotBlank(Optional.ofNullable(getAttribute("required")).orElse(""));
    }

    protected boolean defaultGetMultipleAttribute() {
        return StringUtils.isNotBlank(Optional.ofNullable(getAttribute("multiple")).orElse(""));
    }

    protected String defaultGetList() {
        return Optional.ofNullable(getAttribute("list")).orElse("");
    }

    @SneakyThrows
    protected String defaultGetHref() {
        return unescapeHtml4(URLDecoder.decode(Optional.ofNullable(getAttribute("href")).orElse(""), StandardCharsets.UTF_8));

    }

    protected void defaultSetText(EventListener<ComponentActionEventArgs> settingValue, EventListener<ComponentActionEventArgs> valueSet, String value) {
        settingValue.broadcast(new ComponentActionEventArgs(this, value));
        wrappedElement.fill(value);
        valueSet.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected void defaultSelectByText(EventListener<ComponentActionEventArgs> selectingValue, EventListener<ComponentActionEventArgs> valueSelected, String value) {
        selectingValue.broadcast(new ComponentActionEventArgs(this, value));
        wrappedElement.selectOption(new SelectOption().setLabel(value));
        valueSelected.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected void defaultSelectByValue(EventListener<ComponentActionEventArgs> selectingValue, EventListener<ComponentActionEventArgs> valueSelected, String value) {
        selectingValue.broadcast(new ComponentActionEventArgs(this, value));
        wrappedElement.selectOption(new SelectOption().setValue(value));
        valueSelected.broadcast(new ComponentActionEventArgs(this, value));
    }

    protected void defaultSelectByIndex(EventListener<ComponentActionEventArgs> selectingValue, EventListener<ComponentActionEventArgs> valueSelected, int value) {
        selectingValue.broadcast(new ComponentActionEventArgs(this, "index: " + value));
        wrappedElement.selectOption(new SelectOption().setIndex(value));
        valueSelected.broadcast(new ComponentActionEventArgs(this, "index: " + value));
    }

    private void clickInternal(ClickOptions options) {
        if (options == null) {
            // Multiplying by 1000 to get milliseconds
            var toBeClickableTimeout = webSettings.getTimeoutSettings().inMilliseconds().getElementToBeClickableTimeout();
            options = ClickOptions.create(x -> x.setTimeout(toBeClickableTimeout));
        }

        try {
            findLocator().click(options.convert());
        } catch (Exception ex) {
            wrappedElement.evaluate("el => el.click()");
            DebugInformation.debugInfo("JavaScript click performed instead of playwright click.");
        }
    }

    private void checkInternal(CheckOptions options) {
        if (options == null) {
            // Multiplying by 1000 to get milliseconds
            var toBeCheckableTimeout = webSettings.getTimeoutSettings().inMilliseconds().getElementToBeClickableTimeout();
            options = CheckOptions.create(x -> x.setTimeout(toBeCheckableTimeout));
        }

        try {
            findLocator().check(options.convert());
        } catch (Exception ex) {
            wrappedElement.evaluate("el => el.click()");
            DebugInformation.debugInfo("JavaScript click performed instead of playwright check.");
        }
    }

    private void uncheckInternal(UncheckOptions options) {
        if (options == null) {
            // Multiplying by 1000 to get milliseconds
            var toBeUncheckableTimeout = webSettings.getTimeoutSettings().inMilliseconds().getElementToBeClickableTimeout();
            options = UncheckOptions.create(x -> x.setTimeout(toBeUncheckableTimeout));
        }

        try {
            findLocator().uncheck(options.convert());
        } catch (Exception ex) {
            wrappedElement.evaluate("el => el.click()");
            DebugInformation.debugInfo("JavaScript click performed instead of playwright uncheck.");
        }
    }

    protected void defaultClick(ClickOptions options, EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));
        clickInternal(options);
        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultClick(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));
        clickInternal(null);
        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultCheck(EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));

        checkInternal(null);

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultCheck(CheckOptions options, EventListener<ComponentActionEventArgs> clicking, EventListener<ComponentActionEventArgs> clicked) {
        clicking.broadcast(new ComponentActionEventArgs(this));

        checkInternal(options);

        clicked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultUncheck(EventListener<ComponentActionEventArgs> checking, EventListener<ComponentActionEventArgs> checked) {
        checking.broadcast(new ComponentActionEventArgs(this));

        uncheckInternal(null);

        checked.broadcast(new ComponentActionEventArgs(this));
    }

    protected void defaultUncheck(UncheckOptions options, EventListener<ComponentActionEventArgs> checking, EventListener<ComponentActionEventArgs> checked) {
        checking.broadcast(new ComponentActionEventArgs(this));

        uncheckInternal(options);

        checked.broadcast(new ComponentActionEventArgs(this));
    }

    public <TElementType extends WebComponent> TElementType toExist() {
        wrappedElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(webSettings.getTimeoutSettings().inMilliseconds().getElementToExistTimeout()));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toShadowRootToBeAttached() {
        Validator.validateCondition(() -> (boolean)wrappedElement.evaluate("node => node.shadowRoot !== null"));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotExist() {
        wrappedElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED).setTimeout(webSettings.getTimeoutSettings().inMilliseconds().getElementToNotExistTimeout()));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeVisible() {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(webSettings.getTimeoutSettings().inMilliseconds().getElementToBeVisibleTimeout()));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotBeVisible() {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).not().isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(webSettings.getTimeoutSettings().inMilliseconds().getElementNotToBeVisibleTimeout()));
        return (TElementType)this;
    }

    /**
     * Playwright has auto-wait.
     *
     * @deprecated
     */
    @Deprecated
    public <TElementType extends WebComponent> TElementType toBeClickable() {
        // var waitStrategy = new ToBeClickableWaitStrategy();
        // ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeDisabled() {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).isDisabled(new LocatorAssertions.IsDisabledOptions().setTimeout(webSettings.getTimeoutSettings().inMilliseconds().getValidationsTimeout()));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toHaveContent() {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).not().isEmpty(new LocatorAssertions.IsEmptyOptions().setTimeout(webSettings.getTimeoutSettings().inMilliseconds().getElementToHaveContentTimeout()));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toExist(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        wrappedElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(timeoutIntervalInSeconds * 1000));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotExist(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        wrappedElement.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED).setTimeout(timeoutIntervalInSeconds * 1000));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeVisible(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeoutIntervalInSeconds * 1000));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toNotBeVisible(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).not().isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(timeoutIntervalInSeconds * 1000));
        return (TElementType)this;
    }

    /**
     * Playwright has auto-wait.
     *
     * @deprecated
     */
    @Deprecated
    public <TElementType extends WebComponent> TElementType toBeClickable(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        // var waitStrategy = new ToBeClickableWaitStrategy(timeoutInterval, sleepInterval);
        // ensureState(waitStrategy);
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toBeDisabled(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).isDisabled(new LocatorAssertions.IsDisabledOptions().setTimeout(timeoutIntervalInSeconds * 1000));
        return (TElementType)this;
    }

    public <TElementType extends WebComponent> TElementType toHaveContent(long timeoutIntervalInSeconds, long sleepIntervalInSeconds) {
        PlaywrightAssertions.assertThat(wrappedElement.wrappedLocator()).not().isEmpty(new LocatorAssertions.IsEmptyOptions().setTimeout(timeoutIntervalInSeconds * 1000));
        return (TElementType)this;
    }
    
    @Deprecated
    public <TElementType extends WebComponent> TElementType waitToBe() {
        return (TElementType)this;
    }

    // createBy methods

    public <TComponent extends WebComponent> TComponent createByName(Class<TComponent> componentClass, String name) {
        return create().by(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByName(Class<TComponent> componentClass, String name) {
        return create().allBy(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> TComponent createByNameEndingWith(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new NameEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByNameEndingWith(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new NameEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createByTag(Class<TComponent> componentClass, String tag) {
        return create().by(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTag(Class<TComponent> componentClass, String tag) {
        return create().allBy(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent createByValueContaining(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new ValueContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByValueContaining(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new ValueContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createByLinkText(Class<TComponent> componentClass, String text) {
        return create().by(componentClass, new LinkTextFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByLinkText(Class<TComponent> componentClass, String text) {
        return create().allBy(componentClass, new LinkTextFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent createByLinkTextContaining(Class<TComponent> componentClass, String text) {
        return create().by(componentClass, new LinkTextContainingFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByLinkTextContaining(Class<TComponent> componentClass, String text) {
        return create().allBy(componentClass, new LinkTextContainingFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent createByIdEndingWith(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new IdEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByIdEndingWith(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new IdEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createByPlaceholder(Class<TComponent> componentClass, String text) {
        return create().byPlaceholder(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByPlaceholder(Class<TComponent> componentClass, String text) {
        return create().allByPlaceholder(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent createByPlaceholder(Class<TComponent> componentClass, Pattern pattern) {
        return create().byPlaceholder(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByPlaceholder(Class<TComponent> componentClass, Pattern pattern) {
        return create().allByPlaceholder(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> TComponent createByRole(Class<TComponent> componentClass, AriaRole role) {
        return create().byRole(componentClass, role, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByRole(Class<TComponent> componentClass, AriaRole role) {
        return create().allByRole(componentClass, role, null);
    }

    public <TComponent extends WebComponent> TComponent createByAltText(Class<TComponent> componentClass, String text) {
        return create().byAltText(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByAltText(Class<TComponent> componentClass, String text) {
        return create().allByAltText(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent createByAltText(Class<TComponent> componentClass, Pattern pattern) {
        return create().byAltText(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByAltText(Class<TComponent> componentClass, Pattern pattern) {
        return create().allByAltText(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> TComponent createByText(Class<TComponent> componentClass, String text) {
        return create().byText(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByText(Class<TComponent> componentClass, String text) {
        return create().allByText(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent createByTextContaining(Class<TComponent> componentClass, String text) {
        return create().byText(componentClass, Pattern.compile(String.format(".*%s.*", text)), null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTextContaining(Class<TComponent> componentClass, String text) {
        return create().allByText(componentClass, Pattern.compile(String.format(".*%s.*", text)), null);
    }

    public <TComponent extends WebComponent> TComponent createByTitle(Class<TComponent> componentClass, String text) {
        return create().byTitle(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTitle(Class<TComponent> componentClass, String text) {
        return create().allByTitle(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent createByTitle(Class<TComponent> componentClass, Pattern pattern) {
        return create().byTitle(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTitle(Class<TComponent> componentClass, Pattern pattern) {
        return create().allByTitle(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> TComponent createByIdContaining(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new IdContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByIdContaining(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new IdContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createById(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new IdFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllById(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new IdFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createByClass(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new ClassFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByClass(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new ClassFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createByClassContaining(Class<TComponent> componentClass, String value) {
        return create().by(componentClass, new ClassContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByClassContaining(Class<TComponent> componentClass, String value) {
        return create().allBy(componentClass, new ClassContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent createByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return create().by(componentClass, new AttributeContainingFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return create().allBy(componentClass, new AttributeContainingFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> TComponent createByPlaceholder(Class<TComponent> componentClass, String text, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return create().by(componentClass, new PlaceholderFindStrategy(text, placeholderOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByPlaceholder(Class<TComponent> componentClass, String text, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return create().allBy(componentClass, new PlaceholderFindStrategy(text, placeholderOptions));
    }

    public <TComponent extends WebComponent> TComponent createByPlaceholder(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return create().by(componentClass, new PlaceholderFindStrategy(pattern, placeholderOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByPlaceholder(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return create().allBy(componentClass, new PlaceholderFindStrategy(pattern, placeholderOptions));
    }

    public <TComponent extends WebComponent> TComponent createByTestId(Class<TComponent> componentClass, String text) {
        return create().by(componentClass, new TestIdFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTestId(Class<TComponent> componentClass, String text) {
        return create().allBy(componentClass, new TestIdFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent createByTestId(Class<TComponent> componentClass, Pattern pattern) {
        return create().by(componentClass, new TestIdFindStrategy(pattern));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTestId(Class<TComponent> componentClass, Pattern pattern) {
        return create().allBy(componentClass, new TestIdFindStrategy(pattern));
    }

    public <TComponent extends WebComponent> TComponent createByRole(Class<TComponent> componentClass, AriaRole role, Function<Page.GetByRoleOptions, Page.GetByRoleOptions> options) {
        var roleOptions = RoleOptions.createAbsolute(options);
        return create().by(componentClass, new AriaRoleFindStrategy(role, roleOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByRole(Class<TComponent> componentClass, AriaRole role, Function<Page.GetByRoleOptions, Page.GetByRoleOptions> options) {
        var roleOptions = RoleOptions.createAbsolute(options);
        return create().allBy(componentClass, new AriaRoleFindStrategy(role, roleOptions));
    }

    public <TComponent extends WebComponent> TComponent createByAltText(Class<TComponent> componentClass, String text, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return create().by(componentClass, new AltTextFindStrategy(text, altTextOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByAltText(Class<TComponent> componentClass, String text, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return create().allBy(componentClass, new AltTextFindStrategy(text, altTextOptions));
    }

    public <TComponent extends WebComponent> TComponent createByAltText(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return create().by(componentClass, new AltTextFindStrategy(pattern, altTextOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByAltText(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return create().allBy(componentClass, new AltTextFindStrategy(pattern, altTextOptions));
    }

    public <TComponent extends WebComponent> TComponent createByText(Class<TComponent> componentClass, String text, Function<Page.GetByTextOptions, Page.GetByTextOptions> options) {
        var textOptions = TextOptions.createAbsolute(options);
        return create().by(componentClass, new TextFindStrategy(text, textOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByText(Class<TComponent> componentClass, String text, Function<Page.GetByTextOptions, Page.GetByTextOptions> options) {
        var textOptions = TextOptions.createAbsolute(options);
        return create().allBy(componentClass, new TextFindStrategy(text, textOptions));
    }

    public <TComponent extends WebComponent> TComponent createByText(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByTextOptions, Page.GetByTextOptions> options) {
        var textOptions = TextOptions.createAbsolute(options);
        return create().by(componentClass, new TextFindStrategy(pattern, textOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByText(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByTextOptions, Page.GetByTextOptions> options) {
        var textOptions = TextOptions.createAbsolute(options);
        return create().allBy(componentClass, new TextFindStrategy(pattern, textOptions));
    }

    public <TComponent extends WebComponent> TComponent createByTitle(Class<TComponent> componentClass, String text, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return create().by(componentClass, new TitleFindStrategy(text, titleOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTitle(Class<TComponent> componentClass, String text, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return create().allBy(componentClass, new TitleFindStrategy(text, titleOptions));
    }

    public <TComponent extends WebComponent> TComponent createByTitle(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return create().by(componentClass, new TitleFindStrategy(pattern, titleOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByTitle(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return create().allBy(componentClass, new TitleFindStrategy(pattern, titleOptions));
    }

    public <TComponent extends WebComponent> TComponent createByXpath(Class<TComponent> componentClass, String xpath) {
        return create().by(componentClass, new XpathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByXpath(Class<TComponent> componentClass, String xpath) {
        return create().allBy(componentClass, new XpathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent createByCss(Class<TComponent> componentClass, String css) {
        return create().by(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> createAllByCss(Class<TComponent> componentClass, String css) {
        return create().allBy(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent shadowRootCreate(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return create().by(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> shadowRootCreateAll(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return create().allBy(componentClass, findStrategy);
    }


    // There is no need for shadowRootCreate, because the native playwright locators pierce the shadow DOM (except for xpath)
    public <TComponent extends WebComponent> TComponent shadowRootCreateById(Class<TComponent> componentClass, String id) {
        return create().by(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return create().by(componentClass, new AttributeContainingFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return create().by(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByCss(Class<TComponent> componentClass, String css) {
        return create().by(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByClass(Class<TComponent> componentClass, String className) {
        return create().by(componentClass, new ClassFindStrategy(className));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByName(Class<TComponent> componentClass, String name) {
        return create().by(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return create().by(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return create().by(componentClass, new ValueContainingFindStrategy(valueContaining));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return create().by(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByXPath(Class<TComponent> componentClass, String xpath) {
        return create().by(componentClass, new XpathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByLinkText(Class<TComponent> componentClass, String linkText) {
        return create().by(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return create().by(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByTag(Class<TComponent> componentClass, String tag) {
        return create().by(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return create().by(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> TComponent shadowRootCreateByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return create().by(componentClass, new TextFindStrategy(Pattern.compile(String.format(".*%s.*", innerText))));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllById(Class<TComponent> componentClass, String id) {
        return create().allBy(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return create().allBy(componentClass, new AttributeContainingFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return create().allBy(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByCss(Class<TComponent> componentClass, String css) {
        return create().allBy(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByClass(Class<TComponent> componentClass, String className) {
        return create().allBy(componentClass, new ClassFindStrategy(className));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByName(Class<TComponent> componentClass, String name) {
        return create().allBy(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return create().allBy(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return create().allBy(componentClass, new ValueContainingFindStrategy(valueContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return create().allBy(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByXPath(Class<TComponent> componentClass, String xpath) {
        return create().allBy(componentClass, new XpathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByLinkText(Class<TComponent> componentClass, String linkText) {
        return create().allBy(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return create().allBy(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByTag(Class<TComponent> componentClass, String tag) {
        return create().allBy(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return create().allBy(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> shadowRootCreateAllByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return create().allBy(componentClass, new TextFindStrategy(Pattern.compile(String.format(".*%s.*", innerText))));
    }
}
