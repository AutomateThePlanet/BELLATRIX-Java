# Left Navigation Tests 

## Prerequisites

### 1. Update Project Left Navigation Item Enum

**File:** `O5_LeftNavigation/data/ProjectLeftNavigationItem.java`

Add an enum with the actual left menu items.

### 2. Update Table References in Tests

In the tests, change `ProjectTables.INCIDENT_TABLE` with the actual table name.

### 3. Add user not authorized for specified feature
For the test for non-visible option in Left Navigation add user with no role for this feature

- This user's name is a test parameter
- Replace `"User Impersonate"` in the test with the actual username for the user without role.