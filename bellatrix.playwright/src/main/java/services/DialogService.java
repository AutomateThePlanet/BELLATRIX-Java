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

package services;

import com.microsoft.playwright.Dialog;

import java.util.function.Consumer;

public class DialogService extends WebService {
    /**
     * By default, playwright dismisses dialogs. <br>
     * If you add a dialog handler yourself, the auto-dismiss function will cease. <br>
     * To remove a handler, use {@link #removeDialogHandler(Consumer)}
     */
    public void addDialogHandler(Consumer<Dialog> handler) {
        wrappedBrowser().currentPage().onDialog(handler);
    }

    public void removeDialogHandler(Consumer<Dialog> handler) {
        wrappedBrowser().currentPage().offDialog(handler);
    }
}
