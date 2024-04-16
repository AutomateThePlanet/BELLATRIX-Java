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

package solutions.bellatrix.playwright.services;

import com.microsoft.playwright.Dialog;
import lombok.Getter;
import solutions.bellatrix.playwright.components.common.validate.Validator;
import solutions.bellatrix.playwright.utilities.Settings;
import solutions.bellatrix.playwright.utilities.functionalinterfaces.Action;

import java.util.concurrent.*;
import java.util.function.Consumer;

@Getter
public class DialogService extends WebService {
    private Dialog dialog;

    public Dialog runAndWaitForDialog(Action action) {
        listenForDialog();

        tryPerformAction(action, Settings.timeout().inMilliseconds().getActionTimeoutWhenHandlingDialogs());

        Validator.validateCondition(() -> dialog != null, Settings.timeout().inMilliseconds().getWaitForDialogTimeout(), Settings.timeout().inMilliseconds().getSleepInterval());

        return dialog;
    }

    public Dialog runAndWaitForDialog(Action action, long timeout) {
        listenForDialog();

        tryPerformAction(action, timeout);

        Validator.validateCondition(() -> dialog != null, timeout, Settings.timeout().inMilliseconds().getSleepInterval());

        return dialog;
    }

    public void accept() {
        dialog.accept();
        dialog = null;
    }

    public void accept(String promptText) {
        dialog.accept(promptText);
        dialog = null;
    }

    public void dismiss() {
        dialog.dismiss();
        dialog = null;
    }

    public String getMessage() {
        return dialog.message();
    }

    /**
     * By default, playwright dismisses dialogs. <br>
     * If you add a dialog handler yourself, the auto-dismiss function will cease. <br>
     * To remove a handler, use {@link #removeDialogHandler(Consumer)}
     */
    public void addDialogHandler(Consumer<Dialog> handler) {
        wrappedBrowser().getCurrentPage().onDialog(handler);
    }

    public void removeDialogHandler(Consumer<Dialog> handler) {
        wrappedBrowser().getCurrentPage().offDialog(handler);
    }

    private void listenForDialog() {
        wrappedBrowser().getCurrentPage().onceDialog((dialog) -> this.dialog = dialog);
    }

    private void tryPerformAction(Action action, long timeout) {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            try {
                // Create a callable task representing the action to be executed
                Callable<Void> callableTask = () -> {
                    // Perform the action here
                    action.perform();
                    return null;
                };

                // Submit the task to the executor and get a Future object
                Future<Void> future = executor.submit(callableTask);

                // Wait for the result with a timeout
                try {
                    future.get(timeout, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    // Handle timeout
                    System.out.println("Action timed out and was cancelled. Possible reason could be dialog presence before action completion.");
                    future.cancel(true); // Cancel the task
                }
            } catch (Exception e) {
                // Handle any other exceptions
                e.printStackTrace();
            } finally {
                executor.shutdownNow();
            }
        }
    }
}
