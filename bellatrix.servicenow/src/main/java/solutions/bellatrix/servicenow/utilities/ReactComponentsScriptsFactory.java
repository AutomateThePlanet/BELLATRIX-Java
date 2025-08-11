package solutions.bellatrix.servicenow.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReactComponentsScriptsFactory {
    public static String generateComponentScript(String command) {
        return  "window.findReactComponent = function(el) { " +
                "  for (const key in el) { " +
                "    if (key.startsWith('__reactFiber$')) { " +
                "      const fiberNode = el[key]; " +
                "      return fiberNode && fiberNode.return && fiberNode.return.stateNode; " +
                "    } " +
                "  } " +
                "  return null; " +
                "};" + command;
    }

    public static String generateGridAllVisibleHeadersScript() {
        return generateGridAllVisibleHeadersScript("_instance");
    }

    public static String generateGridAllVisibleHeadersSortableScript() {
        return generateGridAllVisibleHeadersScript("_instance");
    }

    public static String generateGridAllVisibleHeadersScript(String instance) {
        var command = String.format("return JSON.stringify(window.findReactComponent(arguments[0]).%s.getVisibleColumns().filter(c=>c.type!='selection' && c.type!='groupExpand' && c.caption!==undefined).map(l=> l.allowSorting));", instance);
        return generateComponentScript(command);
    }

    public static String generateGridAllVisibleHeadersWithSortingScript(String instance) {
        var command = String.format("return JSON.stringify(let hashMap = new Map(); "
            + "window.findReactComponent(arguments[0]).%s.getVisibleColumns().filter(c=>c.type!='selection' && c.type!='groupExpand' && c.caption!==undefined).map(l=> {hashMap.set(l.caption, l.allowSorting)}));", instance);
        return generateComponentScript(command);
    }

    public static String generateGridAllVisibleDataScript() {
        return generateGridAllVisibleDataScript("_instance");
    }

    public static String generateGridAllVisibleDataScript(String instance) {
        var command = String.format("let visibleRows = window.findReactComponent(arguments[0]).%s.getVisibleRows().filter(x => x.rowType == 'data');" +
                "let keys = visibleRows.map(x => x.cells.map(y => y.column.dataField))[0];" +
                "let values = visibleRows.map(x => x.cells.map(y => y.column.dataType=='date' && y.text!==''?y.text:y.value));" +
                "return JSON.stringify(result = values.map(v => {" +
                "    let obj = {};" +
                "    keys.forEach((element, index) => { obj[element] = v[index] });" +
                "    return obj;" +
                "})); ", instance);

        return generateComponentScript(command);
    }
}
