Left Navigation Tests prerequisites:

1. In
    O5_LeftNavigation/data/ProjectLeftNavigationItem.java
    Add 3 enums that represent actual menu items in the left navigation tree.

    Follow the example:
    In the left menu hierarchy SERVICE_DESK has child INCIDENTS and SELF_SERVICE has child INCIDENTS.
    Three enums are added:
        SELF_SERVICE("Self-Service"),
        INCIDENTS("Incidents"),
        SERVICE_DESK("Service Desk");

2. Two users are expected for the tests
    2.1 User One has "admin" role and has access to both functionalities:
    SERVICE_DESK - INCIDENTS and
    SELF_SERVICE - INCIDENTS.

    This user's credentials will be read from the config file testFrameworkSettings.dev.json.
    Change "user" and "pass" in testFrameworkSettings.dev.json
    section "serviceNowProjectSettings" with the actual data for User One:
          "userName": "user",
          "password": "pass"

    2.2 User Two has no access to SERVICE_DESK - INCIDENTS.
    Replace "User Impersonate" with User Two in the method:
         serviceNowPage.impersonateUser("User Impersonate")
         in test optionNotVisible_when_nonAuthorizedUserSearchInLeftNavigation ().