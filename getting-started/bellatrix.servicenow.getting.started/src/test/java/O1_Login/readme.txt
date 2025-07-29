Test Automation Project prerequisites:

1. Client instance of ServiceNow available
    Instance options are:
     1/ company live ServiceNow instance
     2/ testing instance could be provided by ServiceNow following the steps:
           Personal Developer Instance (Free)
           ----------------------------------
           Go to https://developer.servicenow.com/
           Click "Get Started" or "Request Instance"
           Create a ServiceNow Developer account if you don't have one
           Request a Personal Developer Instance (PDI)
           You'll receive an email with your instance URL and credentials
           The instance will be available for 10 days of inactivity before hibernating
      3/ create user with admin rights
            1. Login in with your Developer account
            2. Click All In case Navigation menu is not open
            3. In filter input "sys_user.list" and press Enter
            4. Click New button and fill and submit the New Record form
            5. In the Table with users find the newly created user and open the record
            6. On the bottom table Click "Role" Tab
            7. Click "Edit" Button,
               type "admin" in the Collections,
               add "admin" to "Role List" and "Save"

2. Update the config file
    src/main/resources/testFrameworkSettings.dev.json

     Update "serviceNowProjectSettings" section parameters:
        1/ change the value for the instance with the actual instance name
             "instance": "dev329858"

        2/  change  "platformRelease" to correspond to the instance
             "platformRelease": "yokohama"

        3/ check if on the instance isPolarisEnabled and set properly
            "isPolarisEnabled": "true",

        4/ use actual username and password data
        Options:
        4.1/ change "user" and "pass" with actual data in the config file
              "userName": "user",
              "password": "pass"
        or
        4.2/ use environment variables in the config fle
            "userName": "{env_servicenow-username-instance}",
            "password": "{env_servicenow-password-instance}"

            create environment variables:
                servicenow-username-instance
                servicenow-password-instance

3. set environment in file src/main/resources/application.properties to dev
     environment=dev

Login Tests
    1. Base url set up
        The url is set in class BaseInstancesUrlGeneration.

        The method serviceNowPage.loginSection().login() navigates to url:
        "https://{instance}.service-now.com/login".

    2. To execute the test loadInitialServiceNowPageWithCredentialsInput()

    serviceNowPage.loginSection().login("user", "pass");
    "user" and "pass" need to be changed with the actual credentials.

    3. For the test loadInitialServiceNowPageWithConfigCredentials()
     the credentials are read from the config file
     testFrameworkSettings.dev.json.

*Note
The example is for the update of "dev" instance
and the changes are applied to the config file: testFrameworkSettings.dev.json.

In case "qa" instance will be used, the above-mentioned changes need to be applied
to the config file testFrameworkSettings.qa.json and the update environment=qa
needed in the file application.properties.

