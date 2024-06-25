package controls.table;

import common.configuration.TestPagesSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.components.advanced.table.Table;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class TableControlTests extends WebTest {
    @BeforeEach
    public void testInit() {
        var url = ConfigurationService.get(TestPagesSettings.class).getTableLocalPage();
        app().navigate().toLocalPage(url);
    }

    @Test
    public void tableGetItems() {
        var table = app().create().byId(Table.class, "table1");
        table.setColumn("Last Name").setColumn("First Name").setColumn("Email").setColumn("Due").setColumn("Web Site").setColumn("Action");

        var dataTableExampleOnes = table.getItems(DataTableExampleOne.class);

        Assertions.assertEquals("Smith", dataTableExampleOnes.get(0).lastName);
        Assertions.assertEquals("John", dataTableExampleOnes.get(0).firstName);
        Assertions.assertEquals("http://www.timconway.com", dataTableExampleOnes.get(dataTableExampleOnes.size() - 1).website);
    }

    @Test
    public void basicTableHasHeader() {
        var table = app().create().byId(Table.class, "table1");

        var headerNames = table.getHeaderNames();
        var tableCell = table.getCell(3, 1);

        Assertions.assertTrue(headerNames.contains("Due"));
        Assertions.assertEquals("$51.00", tableCell.getText());
    }

    @Test
    public void tableWithHeaderReturnsValue() {
        var table = app().create().byId(Table.class, "table1");
        table.setColumn("Last Name").setColumn("First Name").setColumn("Email").setColumn("Due").setColumn("Web Site").setColumn("Action");

        var tableCell = table.getCell("Email", 1);

        Assertions.assertEquals("fbach@yahoo.com", tableCell.getText());
    }

    @Test
    public void simpleTableReturnsAllRowsAndCells() {
        var table = app().create().byId(Table.class, "simpleTable");
        var firstRowCellsCount = table.getRow(0).getCells().size();
        var secondRowCellsCount = table.getRow(1).getCells().size();

        Assertions.assertEquals(firstRowCellsCount, secondRowCellsCount);
        Assertions.assertEquals(4, firstRowCellsCount);
    }

    @Test
    public void nestedTablesReturnsTableCellsCountEqualToTableHeadersCount() {
        var table = app().create().byId(Table.class, "nestedTable");
        table.setColumn("Last Name").setColumn("First Name").setColumn("Email").setColumn("Due").setColumn("Web Site");

        var tableHeadersCount = table.getHeaderNames().size();
        var tableCellsCount = table.getRow(0).getCells().size();

        Assertions.assertEquals(tableHeadersCount, tableCellsCount);
    }

    @Test
    public void nestedTablesReturnsTableCellsCountEqualBetweenRows() {
        var table = app().create().byId(Table.class, "nestedTable");
        table.setColumn("Last Name").setColumn("First Name").setColumn("Email").setColumn("Due").setColumn("Web Site");

        var firstRowCellsCount = table.getRow(0).getCells().size();
        var secondRowCellsCount = table.getRow(1).getCells().size();

        Assertions.assertEquals(firstRowCellsCount, secondRowCellsCount);

    }
}
