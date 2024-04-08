package O8_web_components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class WebComponentsTests extends WebTest {
    @Test
    public void purchaseRocket() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEndingWith(Select.class, "orderby");
        // Create methods accept a generic parameter the type of the web control. Then only the methods for this specific
        // control are accessible. Here we tell BELLATRIX to find your component by name attribute ending with ‘orderby’.
        // <select name="orderby" class="orderby">
        //   <option value="popularity" selected="selected">Sort by popularity</option>
        //   <option value="rating">Sort by average rating</option>
        //   <option value="date">Sort by newness</option>
        //   <option value="price">Sort by price: low to high</option>
        //   <option value="price-desc">Sort by price: high to low</option>
        //   </select>
        sortDropDown.selectByText("Sort by price: low to high");

        Anchor protonMReadMoreButton =
                app().create().byInnerTextContaining(Anchor.class, "Read more");
        // Here BELLATRIX finds the first anchor component which has inner text containing the ‘Read more’ text.
        protonMReadMoreButton.hover();
        // You can Hover and Focus on most web components. Also, can invoke Click on anchors.
        // <a href="/?add-to-cart=28" data-product_id="28">Add to cart</a>
        Anchor addToCartFalcon9 =
                app().create().byAttributeContaining(Anchor.class, "data-product_id", "28").toBeClickable();
        addToCartFalcon9.focus();
        addToCartFalcon9.click();

        Anchor viewCartButton =
                app().create().byClassContaining(Anchor.class, "added_to_cart wc-forward").toBeClickable();
        // Find the anchor by class ‘added_to_cart wc-forward’ and wait for the component again to be clickable.
        viewCartButton.click();

        TextInput couponCodeTextInput = app().create().byId(TextInput.class, "coupon_code");
        //Find a regular text input component by id = ‘coupon_code’.
        couponCodeTextInput.setText("happybirthday");

        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");
        applyCouponButton.click();
        // Create a button control by value attribute containing the text ‘Apply coupon’. Button can be any of the following
        // web components – input button, input submit or button.

        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");

        messageAlert.toBeVisible().waitToBe();

        messageAlert.validateTextIs("Coupon code applied successfully.");

        NumberInput quantityBox = app().create().byClassContaining(NumberInput.class, "input-text qty text");

        quantityBox.setNumber(0);
        quantityBox.setNumber(2);

        Button updateCart = app().create().byValueContaining(Button.class, "Update cart").toBeClickable();
        updateCart.click();

        Span totalSpan = app().create().byXpath(Span.class, "//*[@class='order-total']//span");

        totalSpan.validateTextIs("95.00€");

        Anchor proceedToCheckout =
                app().create().byClassContaining(Anchor.class, "checkout-button button alt wc-forward");
        proceedToCheckout.click();

        Heading billingDetailsHeading = app().create().byInnerTextContaining(Heading.class, "Billing details");

        billingDetailsHeading.toBeVisible().waitToBe();

        Anchor showLogin = app().create().byInnerTextContaining(Anchor.class, "Click here to login");
        // All web components have multiple properties for their most important attributes and ensure methods for their
        // verification.
        showLogin.validateHrefIs("http://demos.bellatrix.solutions/checkout/#");

        TextArea orderCommentsTextArea = app().create().byId(TextArea.class, "order_comments");

        orderCommentsTextArea.scrollToVisible();
        // Here we find the order comments text area and since it is below the visible area we scroll down so that it gets
        // visible on the video recordings. Then the text is set.
        orderCommentsTextArea.setText("Please send the rocket to my door step!");

        TextInput billingFirstName = app().create().byId(TextInput.class, "billing_first_name");
        billingFirstName.setText("In");

        TextInput billingLastName = app().create().byId(TextInput.class, "billing_last_name");
        billingLastName.setText("Deepthought");

        TextInput billingCompany = app().create().byId(TextInput.class, "billing_company");
        billingCompany.setText("Automate The Planet Ltd.");

        Select billingCountry = app().create().byId(Select.class, "billing_country");
        billingCountry.selectByText("Bulgaria");

        TextInput billingAddress1 = app().create().byId(TextInput.class, "billing_address_1");

        Assertions.assertEquals(billingAddress1.getPlaceholder(), "House number and street name");
        // Through the Placeholder, you can get the default text of the control.
        billingAddress1.setText("bul. Yerusalim 5");

        TextInput billingAddress2 = app().create().byId(TextInput.class, "billing_address_2");
        billingAddress2.setText("bul. Yerusalim 6");

        TextInput billingCity = app().create().byId(TextInput.class, "billing_city");
        billingCity.setText("Sofia");

        Select billingState =
                app().create().byId(Select.class, "billing_state").toBeVisible().toBeClickable();
        billingState.selectByText("Sofia-Grad");

        TextInput billingZip = app().create().byId(TextInput.class, "billing_postcode");
        billingZip.setText("1000");

        PhoneInput billingPhone = app().create().byId(PhoneInput.class, "billing_phone");

        billingPhone.setPhone("+00359894646464");
        // Create the special text field control Phone it contains some additional properties unique for this web component.

        EmailInput billingEmail = app().create().byId(EmailInput.class, "billing_email");

        billingEmail.setEmail("info@bellatrix.solutions");
        // Here we create the special text field control Email it contains some additional properties unique for this web
        // component.

        CheckBox createAccountCheckBox = app().create().byId(CheckBox.class, "createaccount");

        createAccountCheckBox.check();
        // You can check and uncheck checkboxes.

        RadioButton checkPaymentsRadioButton =
                app().create().byAttributeContaining(RadioButton.class, "for", "payment_method_cheque");

        checkPaymentsRadioButton.click();
    }

    // Note: All other components have access to the above methods and properties
    //
    // Component 	Available properties
    // Anchor	    click, getHtml, getText, getHref, getTarget, getRel
    // Button	    click, getValue, isDisabled, getText
    // CheckBox	    check, uncheck, isChecked, isDisabled, getValue
    // Div	        getHtml, getText
    // FileInput	    upload, isRequired, isMultiple, getAccept
    // Frame	        getName
    // Heading	    getText
    // Image	        getSrc, getAlt, getHeight, getWidth, getSrcSet, getSizes, getLongDesc
    // Label	        getText, getHtml, getFor
    // Option	    isSelected, isDisabled, getText, getValue
    // RadioButton	click, isChecked, isDisabled, getValue
    // Reset	        click, isDisabled, getText, getValue
    // Select	    getSelected, getAllOptions, selectByText, selectByIndex, isDisabled, isReadonly, isRequired
    // Span	        getText, getHtml
    // TextArea	    getText, setText, isDisabled, isAutoComplete, isSpellCheck, isReadonly, isRequired, getPlaceholder,
    //              getMaxLength, getMinLength, getRows, getCols, getWrap
    // TextInput	    getText, setText isDisabled, isAutoComplete, isReadonly, isRequired, getPlaceholder, getMaxLength, getMinLength

    // HTML5 Components:
    // Component	Available properties

    // ColorInput	getColor, setColor, isAutoComplete, isDisabled, isRequired, getList, getValue
    // DateInput	getDate, setDate, isAutoComplete, isDisabled, isReadonly, isRequired, getMin, getMax, getStep, getValue
    // DateTimeInput getTime, setTime, isAutoComplete, isDisabled, isReadonly, isRequired, getMin, getMax, getStep, getValue
    // EmailInput	getEmail, setEmail, isAutoComplete, isDisabled, isReadonly, isRequired, getMinLength, getMaxLength,
    //              getPlaceholder, getSizeAttribute, getValue
    // MonthInput	getMonth, setMonth, isDisabled, isAutoComplete, isReadonly, isRequired, getMax, getMin, getStep, getValue
    // NumberInput	getNumber, setNumber, isDisabled, isAutoComplete, isReadonly, isRequired, getPlaceholder, getMax,
    //              getMin, getStep, getValue
    // Output	    getText, getHtml, getFor
    // PasswordField	getPassword, setPassword, isDisabled, isAutoComplete, isReadonly, isRequired, getPlaceholder,
    //              getMaxLenght, getMinLenght, getSize, getValue
    // PhoneField	getPhone, setPhone, isDisabled, isAutoComplete, isReadonly, isRequired, getPlaceholder, getMaxLenght,
    //              getMinLenght, getSize, getValue
    // Progress	    getMax, getText, getValue
    // RangeInput	getRange, setRange, isDisabled, isAutoComplete, isRequired, getList, getMax, getMin, getStep, getValue
    // SearchField	getSearch, setSearch, IsDisabled, isAutoComplete, isReadonly, isRequired, getPlaceholder, getMaxLenght,
    //              getMinLenght, getSize, getValue
    // TimeInput	getTime, setTime, getHover, getFocus, isDisabled, isAutoComplete, isReadonly, getMax, getMin, getStep, getValue
    // UrlField	    getUrl, setUrl, isDisabled, isAutoComplete, isReadonly, isRequired, getPlaceholder, getMaxLenght,
    //              getMinLenght, getSize, getValue
    // WeekInput	getWeek, setWeek, isDisabled, isAutoComplete, isReadonly, getMax, getMin, getStep, getValue
}