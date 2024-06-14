package controls.grid;

import common.configuration.TestPagesSettings;
import controls.grid.data.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.advanced.grid.Grid;
import solutions.bellatrix.web.components.advanced.grid.GridRow;
import solutions.bellatrix.web.components.advanced.table.TableCell;
import solutions.bellatrix.web.findstrategies.TagFindStrategy;
import solutions.bellatrix.web.findstrategies.XPathFindStrategy;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

import java.util.ArrayList;
import java.util.List;

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
public class GridControlTestsChrome extends WebTest {
    private Grid testGrid;
    private List<Employee> expectedItems;

    @BeforeEach
    public void testInit() {
        app().navigate().toLocalPage(ConfigurationService.get(TestPagesSettings.class).getGridLocalPage());
        testGrid = app().create().byId(Grid.class, "sampleGrid")
                .setColumn("Order", TextInput.class, new TagFindStrategy("input"))
                .setColumn("Firstname")
                .setColumn("Lastname")
                .setColumn("Email Personal")
                .setColumn("Email Business")
                .setColumn("Actions", Button.class, new XPathFindStrategy("./input[@type='button']"));

        expectedItems = new ArrayList<>();
        expectedItems.add(new Employee("0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com"));
        expectedItems.add(new Employee("1", "Mary", "Moe", "", "mary@hotmail.com"));
        expectedItems.add(new Employee("2", "July", "Dooley", "jily@mscorp.com", ""));

    }

    @Test
    public void returnProperCell_When_GetCell_WithIntForColumn() {
        var cell = testGrid.getCell(0, 1);

        Assertions.assertEquals(0, cell.getRow());
        Assertions.assertEquals(1, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetCell_WithStringForColumn() {
        var cell = testGrid.getCell("Firstname", 1);

        Assertions.assertEquals(1, cell.getRow());
        Assertions.assertEquals(1, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetCell_WithExpressionGorColumn() {
        var cell = testGrid.getCell(Employee.class, employee -> !employee.getPersonalEmail().isBlank(), 1);

        Assertions.assertEquals(1, cell.getRow());
        Assertions.assertEquals(3, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetCell_WithSplitColumn() {
        var cell = testGrid.getCell("Email Business", 0);

        Assertions.assertEquals(0, cell.getRow());
        Assertions.assertEquals(4, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetRow_Then_GetCell_WithStringColumn() {
        var cell = testGrid.getRow(0).getCell("Firstname");

        Assertions.assertEquals(0, cell.getRow());
        Assertions.assertEquals(1, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetRow_Then_GetCell_WithIntColumn() {
        var cell = testGrid.getRow(0).getCell(1);

        Assertions.assertEquals(0, cell.getRow());
        Assertions.assertEquals(1, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetRow_Then_GetCell_WithSplitColumn() {
        var cell = testGrid.getRow(0).getCell("Email Business");

        Assertions.assertEquals(0, cell.getRow());
        Assertions.assertEquals(4, cell.getColumn());
    }

    @Test
    public void returnProperCell_When_GetRow_Then_GetCell_WithExpressionForColumn() {
        var cell = testGrid.getRow(1).getCell(Employee.class, employee -> !employee.getPersonalEmail().isBlank());

        Assertions.assertEquals(1, cell.getRow());
        Assertions.assertEquals(3, cell.getColumn());
    }

    @Test
    public void returnProperObject_When_GetCell_As() {
        TextInput cell = testGrid.getCell(0, 0).as(TextInput.class);

        cell.setText("test");

        Assertions.assertEquals("test", cell.getValue());
    }

    @Test
    public void returnProperCells_When_GetCells_WithExpressionForColumn() {
        var matchingCells = testGrid.getCells(TableCell.class, tableCell -> tableCell.getText().startsWith("J"));

        Assertions.assertEquals(2, matchingCells.size());
    }

    @Test
    public void returnProperCell_When_GetFirstOrDefaultCell() {
        var cell = testGrid.getFirstOrDefaultCell(TableCell.class, tableCell -> tableCell.getText().startsWith("J"));

        Assertions.assertEquals("John", cell.getText());
    }

    @Test
    public void returnProperCell_When_GetLastOrDefaultCell() {
        var cell = testGrid.getLastOrDefaultCell(TableCell.class, tableCell -> tableCell.getText().startsWith("J"));

        Assertions.assertEquals("July", cell.getText());
    }

    @Test
    public void assertRow() {
        testGrid.getRow(0).assertRow(Employee.class, expectedItems.get(0));
    }

    @Test
    public void returnRow_When_GetRowByIndex() {
        var row = testGrid.getRow(0);

        Assertions.assertEquals(0, row.getIndex());
    }

    @Test
    public void returnRows_When_GetRows() {
        var rows = testGrid.getRows();

        Assertions.assertEquals(3, rows.size());
    }

    @Test
    public void verifyRowElement_When_ForEachRow() {
        testGrid.forEachRow(row -> row.createByXPath(Button.class, ".//input[@type='button']").validateIsVisible());
    }

    @Test
    public void returnRow_When_GetRowByExpression() {
        var row = testGrid.getFirstOrDefaultRow(GridRow.class, r -> r.getHtml().contains("July"));

        Assertions.assertEquals(2, row.getIndex());
    }

    @Test
    public void returnColumnIndex_When_GetByName() {
        var index = testGrid.getGridColumnIndexByName("Firstname");

        Assertions.assertEquals(1, index);
    }

    @Test
    public void returnColumnName_When_GetByIndex() {
        var name = testGrid.getGridColumnNameByIndex(1);

        Assertions.assertEquals("Firstname", name);
    }

    @Test
    public void returnProperNames_When_GetHeaderNames() {
        var expectedHeaderNames = new ArrayList<String>();
        expectedHeaderNames.add("Order");
        expectedHeaderNames.add("Firstname");
        expectedHeaderNames.add("Lastname");
        expectedHeaderNames.add("Email Personal");
        expectedHeaderNames.add("Email Business");
        expectedHeaderNames.add("Actions");

        var headerNames = testGrid.getHeaderNames();

        Assertions.assertArrayEquals(expectedHeaderNames.toArray(String[]::new), headerNames.toArray(String[]::new));
    }

    @Test
    public void returnColumnCells_When_GetColumnByIndex() {
        var column = testGrid.getColumn(0);

        Assertions.assertEquals(3, column.size());
        Assertions.assertTrue(column.stream().anyMatch(c -> c.getColumn() == 0));
    }

    @Test
    public void returnProperColumn_When_GetColumnByIndex() {
        var column = testGrid.getColumn(0);

        Assertions.assertTrue(column.stream().anyMatch(c -> c.getColumn() == 0));
    }
}
