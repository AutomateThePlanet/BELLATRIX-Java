## Login Tests

## 1. Base URL Setup

The URL is set in the class `BaseInstancesUrlGeneration`.

The method `serviceNowPage.loginSection().login()` navigates to the URL:

## 2. Execute the Test

`loadInitialServiceNowPageWithCredentialsInput()`

To run the test:

`serviceNowPage.loginSection().login("user", "pass");
"user" and "pass" need to be changed with the actual credentials.`


## 3.  For the test loadInitialServiceNowPageWithConfigCredentials()
the credentials are read from the config file
testFrameworkSettings.dev.json.
