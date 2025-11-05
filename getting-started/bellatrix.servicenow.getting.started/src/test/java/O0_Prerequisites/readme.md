# Test Automation Project Prerequisites

---
## Prerequisites

### 1. Client Instance of ServiceNow Available

**Instance Options:**
- **Option 1:** Company live ServiceNow instance
- **Option 2:** Testing instance provided by ServiceNow

#### Personal Developer Instance (Free)
1. Go to [https://developer.servicenow.com/](https://developer.servicenow.com/)
2. Scroll down to page
3. Click **"Register"** 
4. Create a ServiceNow Developer account (if needed)
5. Request a **Personal Developer Instance (PDI)**
6. You'll receive in your ServiceNow profile the instance URL and credentials
7. The instance remains active for 10 days of inactivity before hibernating

#### Create a User with Admin Rights
1. Log in with your Developer account
2. Open the **Navigation menu** (click **All** if not visible)
3. Filter for: `sys_user.list` and press Enter
4. Click **New** and fill out the **New Record** form and do NOT forget to fill in the User ID
5. Open the newly created user record
6. Scroll to the **Role** tab at the bottom
7. Click **Edit**
   * Type `admin` in the **Collection**
   * Add `admin` to the **Role List** and click **Save**

#### Optional: Create one more user for Impersonate tests

---

### 2. Update the Config File

**Path:** `src/main/resources/testFrameworkSettings.dev.json`

#### Modify the following in `serviceNowProjectSettings`:

- **Instance:**
    ```jsonlines
  {
    "instance": "instance"
  }
    ```

- **Platform Release:**
    ```jsonlines
    "platformRelease": "release"
    ```

- **Polaris Enabled Flag:**
    ```jsonlines
    "isPolarisEnabled": "true"
    ```

- **Credentials Options:**

  **Option 4.1: Hardcoded Credentials**
    ```jsonlines
    "userName": "user",
    "password": "pass"
    ```

  **Option 4.2: Use Environment Variables**
    ```jsonlines
    "userName": "{env_servicenow-username-instance}",
    "password": "{env_servicenow-password-instance}"
    ```

  Create the following environment variables:
    - `servicenow-username-instance`
    - `servicenow-password-instance`

---
### 3. Set Environment

In the file `src/main/resources/application.properties set:
```properties
environment=dev
```
---
## Note
The example is for the update of "dev" instance
and the changes are applied to the config file: testFrameworkSettings.dev.json.

In case "qa" instance will be used, the above-mentioned changes need to be applied
to the config file testFrameworkSettings.qa.json and the update environment=qa
needed in the file application.properties.


### 3. Set Environment

In the file `src/main/resources/application.properties` set:
```properties
environment=dev