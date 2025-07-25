function formConverter(locator, className) {
    window.querySelectorByXpath = function (xpath) {
        var iterator = document.evaluate(xpath, document, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);

        return iterator.iterateNext();
    }

    window.querySelectorAllByXpath = function (xpath) {
        var iterator = document.evaluate(xpath, document, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);
        const items = [];
        let item = iterator.iterateNext();

        while (item) {
            items.push(item);
            item = iterator.iterateNext();
        }
        return items;
    }

    const mainForm = querySelectorByXpath(locator);
    const allFields = querySelectorAllByXpath(locator + "//div[contains(concat(' ',normalize-space(@class),' '),' form-group ') and not(contains(@style,'display: none')) and not(contains(@class,'custom-form-group'))]");

    function getLabel(formGroup) {
        return formGroup.querySelector("label span.label-text").textContent;
    }

    function isRequire(formGroup) {
        requiredSymbol = formGroup.querySelector("label span.required-marker");
        if (requiredSymbol != undefined) {
            return true;
        }
        return false;
    }

    function isInputReadOnly(formGroup) {
        let isReadOnly = formGroup.querySelector("[class*='form-control'],[class*='checkbox']").getAttribute("readonly");
        if (isReadOnly == 'readonly') {
            return true
        }

        let checkboxReadOnly = formGroup.querySelector(".checkbox.disabled");
        if (checkboxReadOnly != undefined) {
            return true
        }

        let tableReadOnly = formGroup.querySelector("p[id*='sys_readonly']");
        if (tableReadOnly != undefined) {
            return true
        }

        let glideListReadOnly = formGroup.querySelector("p[class='form-control-static']");
        if (glideListReadOnly != undefined) {
            return true
        }

        let isDateReadOnly = formGroup.querySelector("[class*='form-control disabled']");
        if (isDateReadOnly != undefined) {
            return true
        }

        return formGroup.querySelector("[class*='form-control'],[class*='checkbox']").classList.contains("disabled")
    }

    function getType(formGroup) {
        if (formGroup.querySelector("textarea[aria-label]") != undefined && formGroup.querySelector("div[type]") == 'string') {
            return formGroup.querySelector("textarea[aria-label]").type;
        }
        return formGroup.querySelector("div[type]").getAttribute('type');
    }

    function getOptions(formGroup) {
        let selectOptions = [];
        if (formGroup.querySelector("div[type]").getAttribute('type') == 'choice') {
            let allOptions = formGroup.querySelectorAll("select[class*='form-control'] > option");
            allOptions.forEach(o => selectOptions.push(o.text));
        }
        return selectOptions;
    }

    function getSelectedOption(formGroup) {
        if ((formGroup.querySelector("div[type]").getAttribute('type') == 'choice' || formGroup.querySelector("div[type]").getAttribute('type') == 'table_name') &&
            formGroup.querySelector("select[class*='form-control'] > option[selected]") != undefined) {
            return formGroup.querySelector("select[class*='form-control'] > option[selected]").text;
        }
    }

    function getId(formGroup) {
        return formGroup.id;
    }

    function inputButtons(formGroup) {
        let buttons = [];
        hasSearchButton = formGroup.querySelector("a[class*='icon-book']");
        if (hasSearchButton != undefined) {
            buttons.push("Knowledge");
        }

        hasInfoButton = formGroup.querySelector("button[class*='icon-info']");
        if (hasInfoButton != undefined) {
            buttons.push("Info");
        }

        hasLightBulbButton = formGroup.querySelector("span[class*='icon-lightbulb']");
        if (hasLightBulbButton != undefined) {
            buttons.push("LightBulb");
        }

        hasEditButton = formGroup.querySelector("span[class*='icon-edit']");
        if (hasEditButton != undefined) {
            buttons.push("Edit");
        }

        hasCrossButton = formGroup.querySelector("span[class*='icon-cross']");
        if (hasCrossButton != undefined) {
            buttons.push("Cross");
        }

        hasLockButton = formGroup.querySelector("span[class*='icon-locked']");
        if (hasLockButton != undefined) {
            buttons.push("Locked");
        }

        hasAddUserButton = formGroup.querySelector("span[class*='icon-user-add']");
        if (hasAddUserButton != undefined) {
            buttons.push("Add");
        }
        return buttons;
    }

    function convertToFields(allFields) {
        let objToString = "@Data\n@SuperBuilder\n@EqualsAndHashCode(callSuper = true)\npublic class ";
        objToString += className + " extends ServiceNowForm {";
        allFields.map(f => {
            let fieldComponent;
            let fieldId;
            let type = getType(f).split("_").reduce((previous, current) =>
                previous + current.charAt(0).toUpperCase() + current.slice(1), []
            );

            let fieldReplacedName = getLabel(f).replace(/(?:^.|[A-Z]|\b.)/g, function (letter, index) {
                return index == 0 ? letter.toLowerCase() : letter.toUpperCase();
            }).replace(/[^a-zA-Z0-9]/g, '');

            let camelCaseFieldName = fieldReplacedName.charAt(0).toLowerCase() + fieldReplacedName.slice(1);

            if (isInputReadOnly(f)) {
                objToString += "\n\t\t@Disabled";
            }
            if (isRequire(f)) {
                objToString += "\n\t\t@Required";
            }
            if (inputButtons(f).length > 0) {
                let formattedButtons = inputButtons(f).toString().replaceAll(",", '", "')
                objToString += `\n\t\t@ExternalButtons({"${formattedButtons}"})`;
            }
            if (getOptions(f).length > 0) {
                let formattedOptions = getOptions(f).toString().replaceAll(",", '", "')
                objToString += `\n\t\t@SelectOptions({"${formattedOptions}"})`;
            }
            if (getSelectedOption(f) != null && getSelectedOption(f) != undefined) {
                objToString += `\n\t\t@SelectedOption("${getSelectedOption(f)}")`;
            }

            fieldComponent = "Sn" + type;
            fieldId = getId(f);
            fieldName = camelCaseFieldName;

            objToString += `
        @FieldLabel("${getLabel(f)}")
        @Component(${fieldComponent}.class)
        @Id("${fieldId}")
        private String ${fieldName};
        `.replace(/^[ \r\n]+$/gi, "");
        })

        objToString += "}";
        return objToString;
    }
    console.log(convertToFields(allFields, className));
}

formConverter("//form[@method='POST']/span[contains(concat(' ',normalize-space(@class),' '),' tabs2_section ')][1]", "Document");