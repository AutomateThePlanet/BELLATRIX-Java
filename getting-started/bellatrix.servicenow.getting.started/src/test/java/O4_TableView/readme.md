# Record Form Tests Prerequisites

## Prerequisites

### 1. Update Project Tables Enum

**File:** `O3_RecordForm/data/ProjectTables.java`

Add an enum with the correct target table to access the necessary test data.

### 2. Update Table References in Tests

In the tests, change `ProjectTables.INCIDENT_TABLE` with the actual table name.

### 3. Update Record System ID

In the tests with the specific record from the actual table:
- Change the target `sysId` with the actual `sysId` of the record

### 4. Update Form Locator

**File:** `O3_RecordForm/data/recordPage/Map.java`

Change the value of `xpathLocator` for the `mainForm()` method to correspond to the locator of the form element on the target page.

### 5. RecordFormSubTabsTests Configuration

For `RecordFormSubTabsTests`, update the following:

#### Table Reference
- Change `ProjectTables.INCIDENT_TABLE` with the actual table

#### Expected Tabs
- Change `"expectedTabs"` values with the actual tabs for the actual table

#### Tab Selection
- Change `tabByLabel("Child Incidents")` with the actual tab to be tested

#### Tab Columns
- Change the `"expectedTabColumns"` with the actual data columns for the selected tab

## Implementation Notes

> **Important:** Ensure all references to table names, system IDs, and form elements are updated to match your target ServiceNow instance configuration.

> **Testing:** Verify that the updated locators and table references work correctly with your specific ServiceNow setup before running the full test suite.
