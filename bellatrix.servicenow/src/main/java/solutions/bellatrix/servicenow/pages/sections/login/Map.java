package solutions.bellatrix.servicenow.pages.sections.login;

import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.PasswordInput;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.pages.PageMap;

public class Map extends PageMap {
    public TextInput usernameInput() {
        return create().byId(TextInput.class, "user_name");
    }

    public PasswordInput passwordInput() {
        return create().byId(PasswordInput.class, "user_password");
    }

    public Button logInButton() {
        return create().byId(Button.class, "sysverb_login");
    }
}