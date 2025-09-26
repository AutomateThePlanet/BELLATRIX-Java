# UIB Features Tests

## Prerequisites

This guide outlines the necessary steps to run ServiceNow UIB Tests.

### 1. Revise the Models and update the fields of the tst models

**Folder** `O8_UIB/data/models`

### 2. Revise the pages and update the locators if the sample Map deviates from the current servicenow implementation

**Folder** `O8_UIB/data/pages`

### 3. Update the test workspace

1. Add to ServiceNowWorkspaces an actual workspace to use for the test and replace in all tests
2. ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME with the actual value

**Folder** `src/main/java/solutions/bellatrix/servicenow/infrastructure/enums`

### 4. Update the expected data for UibDashboardNavigationTests 

#### 4.1 Update the expected sidebar buttons and tabs buttons in WorkspaceGeneralPage.

**Folder** `O8_UIB/data/pages/workspaceGeneralPage/WorkspaceGeneralPage.java`

Options not visible in LeftSidebarSection.MenuItems, need to be added to MenuItems first.

**Folder** 'src/main/java/solutions/bellatrix/servicenow/pages/uib/sections/leftSidebarSection/LeftSidebarSection.java'

#### 4.2 Update the expected custom parameters in the tests:

    expected_dashboard_heading
    card_button_name
    expected_main_heading

### 5. Update the expected data for UibRecordViewTests

#### 5.1 Update the expected custom parameters in the tests:
    
    card_button_name
    column_name
    column_value
    expected_main_heading

    all fields for expectedFormData
    all fields for newFormData

### 6. Update the expected data for UibTableViewTests

#### 6.1 Update the expected custom parameters in the tests:

    card_button_name
    column_name
    column_locator
    expected_main_heading
    column_name
    column_locator
    