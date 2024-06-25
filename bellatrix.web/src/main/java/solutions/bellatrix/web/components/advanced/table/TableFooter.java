/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.components.advanced.table;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.services.FooterService;
import solutions.bellatrix.web.components.advanced.services.HeaderNamesService;
import solutions.bellatrix.web.components.contracts.ComponentHtml;

public class TableFooter extends WebComponent implements ComponentHtml {
    private Table parentTable;
    private HeaderNamesService headerNamesService;
    private FooterService footerService;

    @Getter @Setter private int index;

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    public void setParentTable(Table table) {
        parentTable = table;
        headerNamesService = new HeaderNamesService(parentTable.getTableService().getHeaderRows());
        footerService = new FooterService(parentTable.getTableService().getFooter());
    }

    public TableRow getRowByPosition(int position) {
        var footerRow = this.createByXPath(TableRow.class, "." + HtmlService.getAbsoluteXPath(footerService.getFooterRowByPosition(position)));
        footerRow.setParentTable(parentTable);
        footerRow.setIndex(position);

        return footerRow;
    }

    public TableRow getRowByName(String footerName) {
        var node = footerService.getFooterRowByName(footerName);
        int position = footerService.getRows().indexOf(node);
        var footerRow = this.createByXPath(TableRow.class, "." + HtmlService.getAbsoluteXPath(node));
        footerRow.setParentTable(parentTable);
        footerRow.setIndex(position);

        return footerRow;
    }

    public TableCell getCell(String footerName, String headerName) {
        var headerPosition = headerNamesService.getHeaderPosition(headerName, parentTable.getColumnHeaderNames());
        if (headerPosition == null) return null;

        var node = footerService.getFooterRowCellByName(footerName, headerPosition);
        var footerCell = this.createByXPath(TableCell.class, "." + HtmlService.getAbsoluteXPath(node));
        footerCell.setRow(footerService.getRows().indexOf(node));
        footerCell.setColumn(headerPosition);

        return footerCell;
    }

    public TableCell getCell(int position, String headerName) {
        var headerPosition = headerNamesService.getHeaderPosition(headerName, parentTable.getColumnHeaderNames());
        if (headerPosition == null) return null;

        var node = footerService.getFooterRowCellByPosition(position, headerPosition);
        var footerCell = this.createByXPath(TableCell.class, "." + HtmlService.getAbsoluteXPath(node));
        footerCell.setRow(position);
        footerCell.setColumn(headerPosition);

        return footerCell;
    }
}
