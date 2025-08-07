package solutions.bellatrix.servicenow.pages.sections.availableSelectPopUpSection;

import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.pages.WebPage;

public class AvailableSelectPopUpSection extends WebPage<Map, Asserts> {
    public void assignRoleIfExists(String availableRole) {
        for (Label role : map().availableRoles()) {
            if (role.getText().equals(availableRole)) {
                map().arrowAddButton().click();
            }
        }
        map().rolesOkButton().click();
    }

    @Override
    protected String getUrl() {
        return null;
    }
}