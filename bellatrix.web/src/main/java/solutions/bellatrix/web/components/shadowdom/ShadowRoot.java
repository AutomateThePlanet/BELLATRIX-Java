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

package solutions.bellatrix.web.components.shadowdom;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import solutions.bellatrix.core.utilities.ConverterService;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.findstrategies.FindStrategy;

import java.util.List;

public class ShadowRoot extends WebComponent implements ComponentHtml {
    /**
     * Returns the innerHTML of the shadowRoot of the shadow host.
     */
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    @Override
    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var component = ShadowDomService.createFromShadowRoot(componentClass, this, findStrategy);
        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    @Override
    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();
        List<TComponent> componentList = ShadowDomService.createAllFromShadowRoot(componentClass, this, findStrategy);
        CREATED_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        return componentList;
    }

    public ShadowHost getWrappedElement() {
        return ConverterService.convertToClass(super.getWrappedElement(), ShadowHost.class);
    }
}
