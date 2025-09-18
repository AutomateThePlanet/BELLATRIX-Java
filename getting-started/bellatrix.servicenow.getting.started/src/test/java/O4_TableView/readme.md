# Table View Tests Prerequisites

## Prerequisites

### 1. Update Project Tables Enum

**File:** `O4_TableView/data/ProjectTables.java`

Add an enum with the correct target table to access the necessary test data.

### 2. Update Test Configuration

In the tests, update the following parameters:

#### Table Reference
- Change `ProjectTables.INCIDENT_TABLE` with the actual table

#### Column Configuration
- Change the `"expectedColumns"` with the actual data columns for the actual table

#### Tab Configuration
- Change `"expectedTabs"` values with the actual tabs for the actual table
- Change `tabByLabel("Child Incidents")` with the actual tab to be tested
- Change the `"expectedTabColumns"` with the actual data columns for the selected tab

### 3. Record Search Tests Configuration

For the following specific tests:
- `confirmRecordExistByValueAndColumnName()`
- `correctRecordsSelected_when_searchByValueInColumn()`

#### Updates Required:
- Change with the actual column name for the target table
- Change with the actual value of the respective record in that column

## Implementation Notes

> **Important:** Ensure all tabl+e references, column names, and test data values match your target ServiceNow instance configuration.

> **Data Validation:** Verify that the expected columns and tabs exist in your ServiceNow table before running the tests.

> **Search Functionality:** Make sure the column names and values used in search tests correspond to actual data in your ServiceNow instance.
