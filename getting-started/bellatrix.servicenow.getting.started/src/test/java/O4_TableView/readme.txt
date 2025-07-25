Table View Tests prerequisites:

1. In
    O4_TableView/data/ProjectTables.java
    Add an enum with the correct target table to access the necessary for the test data.
2. In the tests
    Change ProjectTables.INCIDENT_TABLE with the actual table
    Change the "expectedColumns" with the actual data columns for the actual table

    Change "expectedTabs" values with the actual for the actual table
    Change tabByLabel("Child Incidents") with the actual tab to be tested
    Change the "expectedTabColumns" with the actual data columns for the selected tab

3. For test confirmRecordExistByValueAndColumnName() and correctRecordsSelected_when_searchByValueInColumn()
    Change with the actual column name and the value of the respective record


