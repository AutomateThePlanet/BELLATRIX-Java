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

2. create customized testFrameworkSettings file and save it in the src/main/resources
    1/ use the sample config file:
            O1_Login/sectionData/testFrameworkSettings.instanceName.json

    2/ edit "serviceNowProjectSettings" section parameters
    with the respective parameters of the instance

    3/ use actual username and password data
        Options:
        3.1/ change "user" and "pass" with actual data in the config file
              "userName": "user",
              "password": "pass"
        or
        3.2/ use environment variables in the config fle
            "userName": "{env_servicenow-username-instance}",
            "password": "{env_servicenow-password-instance}"

            create environment variables:
                servicenow-username-instance
                servicenow-password-instance

    4/ rename it using the respective instanceName

    5/ check if on the instance isPolarisEnabled and set properly

4. set environment in file src/main/resources/application.properties
        with the actual instance name

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
     testFrameworkSettings.instanceName.json.


