package solutions.bellatrix.servicenow.listeners;

import solutions.bellatrix.servicenow.components.serviceNow.SnDefaultComponent;
import solutions.bellatrix.core.plugins.Listener;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.CheckBox;
import solutions.bellatrix.web.components.DateInput;
import solutions.bellatrix.web.components.DateTimeInput;
import solutions.bellatrix.web.components.EmailInput;
import solutions.bellatrix.web.components.FileInput;
import solutions.bellatrix.web.components.MonthInput;
import solutions.bellatrix.web.components.NumberInput;
import solutions.bellatrix.web.components.PasswordInput;
import solutions.bellatrix.web.components.PhoneInput;
import solutions.bellatrix.web.components.SearchField;
import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.TimeInput;
import solutions.bellatrix.web.services.App;
import solutions.bellatrix.servicenow.utilities.UserInteraction;

public class AutoWaitListener extends Listener {
        private static boolean isAutoWaitAfterActionOn = false;

        @Override
        public void addListener() {
            if (!isAutoWaitAfterActionOn) {
                Anchor.CLICKED.addListener((x) -> executeWaitStrategy());
                Button.CLICKED.addListener((x) -> executeWaitStrategy());
                CheckBox.CHECKED.addListener((x) -> executeWaitStrategy());
                DateInput.SETTING_DATE.addListener((x) -> executeWaitStrategy());
                DateTimeInput.SETTING_TIME.addListener((x) -> executeWaitStrategy());
                EmailInput.SETTING_EMAIL.addListener((x) -> executeWaitStrategy());
                FileInput.UPLOADING.addListener((x) -> executeWaitStrategy());
                MonthInput.SETTING_MONTH.addListener((x) -> executeWaitStrategy());
                NumberInput.SETTING_NUMBER.addListener((x) -> executeWaitStrategy());
                PasswordInput.SETTING_PASSWORD.addListener((x) -> executeWaitStrategy());
                PhoneInput.SETTING_PHONE.addListener((x) -> executeWaitStrategy());
                SearchField.SETTING_SEARCH.addListener((x) -> executeWaitStrategy());
                Select.SELECTING.addListener((x) -> executeWaitStrategy());
                TextArea.SETTING_TEXT.addListener((x) -> executeWaitStrategy());
                TextInput.SETTING_TEXT.addListener((x) -> executeWaitStrategy());
                TimeInput.SETTING_TIME.addListener((x) -> executeWaitStrategy());
                SnDefaultComponent.VALUE_SET.addListener((x) -> executeWaitStrategy());
                isAutoWaitAfterActionOn = true;
            }
        }

        private void executeWaitStrategy() {
            App app = new App();
            app.browser().waitUntilPageLoadsCompletely();
            app.browser().waitForAjax();
            UserInteraction.waitUntilSpinnerDisappears();
        }

}
