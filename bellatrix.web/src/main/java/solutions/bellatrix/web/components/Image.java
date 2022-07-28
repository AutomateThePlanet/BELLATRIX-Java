/*
 * Copyright 2022 Automate The Planet Ltd.
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

import solutions.bellatrix.web.components.contracts.*;

public class Image extends WebComponent implements ComponentSrc, ComponentHeight, ComponentWidth, ComponentLongDesc, ComponentAlt, ComponentSrcSet, ComponentSizes {
    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getAlt() {
        return defaultGetAltAttribute();
    }

    @Override
    public String getLongDesc() {
        return defaultGetLongDescAttribute();
    }

    @Override
    public String getSizes() {
        return defaultGetSizesAttribute();
    }

    @Override
    public String getSrc() {
        return defaultGetSrcAttribute();
    }

    @Override
    public String getSrcSet() {
        return defaultGetSrcSetAttribute();
    }

    @Override
    public int getHeight() {
        return Integer.parseInt(defaultGetHeightAttribute());
    }

    @Override
    public int getWidth() {
        return Integer.parseInt(defaultGetWidthAttribute());
    }
}
