Record Form Tests prerequisites:

1. In
    O3_RecordForm/data/ProjectTables.java
    Add an enum with the correct target table to access the necessary for the test data.
2. In the tests
    Change ProjectTables.INCIDENT_TABLE with the actual table
3. In the tests with the specific record from the actual table
    Change the target sysId with the actual sysId of the record
4. In
   O3_RecordForm/data/recordPage/Map.java
   Change the value of xpathLocator for the mainForm() to correspond to the locator of the form element on the target page.
5. For RecordFormSubTabsTests
   Change ProjectTables.INCIDENT_TABLE with the actual table
   Change "expectedTabs" values with the actual for the actual table
   Change tabByLabel("Child Incidents") with the actual tab to be tested
   Change the "expectedTabColumns" with the actual data columns for the selected tab
