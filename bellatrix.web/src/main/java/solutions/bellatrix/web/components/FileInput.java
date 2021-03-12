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

package solutions.bellatrix.web.components;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.web.components.contracts.*;

public class FileInput extends WebComponent implements ComponentRequired, ComponentMultiple, ComponentAccept {
    public final static EventListener<ComponentActionEventArgs> UPLOADING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> UPLOADED = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public void upload(String file) {
        defaultSetText(UPLOADING, UPLOADED, file);
    }

    @Override
    public String getAccept() {
        return defaultGetAcceptAttribute();
    }

    @Override
    public boolean isMultiple() {
        return defaultGetMultipleAttribute();
    }

    @Override
    public boolean isRequired() {
        return defaultGetRequiredAttribute();
    }
}
