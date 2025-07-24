window.createEnum = function (xpath, enumName) {
    const iterator = document.evaluate(xpath, document, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);

    const items = [];
    let item = iterator.iterateNext();

    while (item) {
        items.push(item);
        item = iterator.iterateNext();
    }

    let enumBody = items.map(x => {
        let itemText = x.textContent;
        return `${itemText.toUpperCase().replaceAll(" ", "_")}("${itemText}")`
    }).join(",\n");
    let result =
        `public enum ${enumName} {
        ${enumBody};

        private final String value;

        ${enumName}(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }`;

    console.log(result);
};

//Example
createEnum("//span[@class='dx-tab-text']", "Test");