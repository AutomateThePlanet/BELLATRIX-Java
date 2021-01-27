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

package solutions.bellatrix.configuration;/*
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

public class WebSettings {
    private  String baseUrl;
    private BrowserSettings chrome;
    private BrowserSettings firefox;
    private BrowserSettings edge;
    private  int elementWaitTimeout;

    public String getBaseUrl() {
        return baseUrl;
    }

    public BrowserSettings getChrome() {
        return chrome;
    }

    public BrowserSettings getFirefox() {
        return firefox;
    }

    public BrowserSettings getEdge() {
        return edge;
    }

    public int getElementWaitTimeout() {
        return elementWaitTimeout;
    }

    @Override
    public String toString() {
        return "solutions.bellatrix.configuration.WebSettings{" +
                "baseUrl='" + baseUrl + '\'' +
                ", chrome=" + chrome +
                ", firefox=" + firefox +
                ", edge=" + edge +
                ", elementWaitTimeout=" + elementWaitTimeout +
                '}';
    }
}
