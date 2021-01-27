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

package solutions.bellatrix.services;

import org.apache.commons.lang3.NotImplementedException;
import solutions.bellatrix.components.WebComponent;
import solutions.bellatrix.findstrategies.FindStrategy;

import java.util.List;

public class ComponentCreateService extends WebService {
    WebComponent findById(String id) {
        throw new NotImplementedException();
    }

    WebComponent findByXPath(String xpath) {
        throw new NotImplementedException();
    }

    WebComponent findByTag(String tag) {
        throw new NotImplementedException();
    }

    WebComponent findByClass(String cssClass) {
        throw new NotImplementedException();
    }

    WebComponent findByCss(String css) {
        throw new NotImplementedException();
    }

    WebComponent findByLinkText(String linkText) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAllById(String id) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAllByXPath(String xpath) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAllByTag(String tag) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAllByClass(String cssClass) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAllByCss(String css) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAllByLinkText(String linkText) {
        throw new NotImplementedException();
    }

    List<WebComponent> findAll(FindStrategy findStrategy) {
        throw new NotImplementedException();
    }

    WebComponent find(FindStrategy findStrategy) {
        throw new NotImplementedException();
    }
}
