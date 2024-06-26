package O11_behaviour_driven_development;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class BDDLoggingTests extends WebTest {
    @Test
    public void purchaseRocketWithLogs() {
        app().navigate().to("http://demos.bellatrix.solutions/");

        Select sortDropDown = app().create().byNameEndingWith(Select.class, "orderby");
        Anchor protonMReadMoreButton = app().create().byInnerTextContaining(Anchor.class, "Read more");
        Anchor addToCartFalcon9 =
                app().create().byAttributeContaining(Anchor.class, "data-product_id", "28").toBeClickable();
        Anchor viewCartButton =
                app().create().byClassContaining(Anchor.class, "added_to_cart wc-forward").toBeClickable();

        sortDropDown.selectByText("Sort by price: low to high");
        protonMReadMoreButton.hover();
        addToCartFalcon9.focus();
        addToCartFalcon9.click();
        viewCartButton.click();

        TextInput couponCodeTextInput = app().create().byId(TextInput.class, "coupon_code");
        Button applyCouponButton = app().create().byValueContaining(Button.class, "Apply coupon");
        Div messageAlert = app().create().byClassContaining(Div.class, "woocommerce-message");
        NumberInput quantityBox = app().create().byClassContaining(NumberInput.class, "input-text qty text");
        Button updateCart = app().create().byValueContaining(Button.class, "Update cart").toBeClickable();
        Span totalSpan = app().create().byXpath(Span.class, "//*[@class='order-total']//span");
        Anchor proceedToCheckout =
                app().create().byClassContaining(Anchor.class, "checkout-button button alt wc-forward");

        couponCodeTextInput.setText("happybirthday");
        applyCouponButton.click();
        messageAlert.toBeVisible().waitToBe();
        messageAlert.validateTextIs("Coupon code applied successfully.");
        quantityBox.setNumber(0);
        quantityBox.setNumber(2);
        totalSpan.validateTextIs("54.00€");
        proceedToCheckout.click();

        Heading billingDetailsHeading = app().create().byInnerTextContaining(Heading.class, "Billing details");
        Anchor showLogin = app().create().byInnerTextContaining(Anchor.class, "Click here to login");
        TextArea orderCommentsTextArea = app().create().byId(TextArea.class, "order_comments");
        TextInput billingFirstName = app().create().byId(TextInput.class, "billing_first_name");
        TextInput billingLastName = app().create().byId(TextInput.class, "billing_last_name");
        TextInput billingCompany = app().create().byId(TextInput.class, "billing_company");
        Select billingCountry = app().create().byId(Select.class, "billing_country");
        TextInput billingAddress1 = app().create().byId(TextInput.class, "billing_address_1");
        TextInput billingAddress2 = app().create().byId(TextInput.class, "billing_address_2");
        TextInput billingCity = app().create().byId(TextInput.class, "billing_city");
        Select billingState = app().create().byId(Select.class, "billing_state").toBeVisible().toBeClickable();
        TextInput billingZip = app().create().byId(TextInput.class, "billing_postcode");
        PhoneInput billingPhone = app().create().byId(PhoneInput.class, "billing_phone");
        EmailInput billingEmail = app().create().byId(EmailInput.class, "billing_email");
        CheckBox createAccountCheckBox = app().create().byId(CheckBox.class, "createaccount");
        RadioButton checkPaymentsRadioButton =
                app().create().byAttributeContaining(RadioButton.class, "for", "payment_method_cheque");

        billingDetailsHeading.toBeVisible().waitToBe();
        showLogin.validateHrefIs("https://demos.bellatrix.solutions/checkout/#");
        orderCommentsTextArea.scrollToVisible();
        orderCommentsTextArea.setText("Please send the rocket to my door step!");
        billingFirstName.setText("In");
        billingLastName.setText("Deepthought");
        billingCompany.setText("Automate The Planet Ltd.");
        billingCountry.selectByText("Bulgaria");
        billingAddress1.validatePlaceholderIs("House number and street name");
        billingAddress1.setText("bul. Yerusalim 5");
        billingAddress2.setText("bul. Yerusalim 6");
        billingCity.setText("Sofia");
        billingState.selectByText("Sofia-Grad");
        billingZip.setText("1000");
        billingPhone.setPhone("+00359894646464");
        billingEmail.setEmail("info@bellatrix.solutions");
        createAccountCheckBox.check();
        checkPaymentsRadioButton.click();

        // After the test is executed the following log is created:
        // selecting 'Sort by price: low to high' from Select (name ending with orderby)
        // hovering Anchor (text containing Read more)
        // focusing Anchor (data-product_id containing 28)
        // clicking Anchor (data-product_id containing 28)
        // clicking Anchor (class containing added_to_cart wc-forward)
        // typing 'happybirthday' in TextInput (id = coupon_code)
        // clicking Button (value containing Apply coupon)
        // validating Div (class containing woocommerce-message)'s inner text is 'Coupon code applied successfully.'
        // typing '0.0' in NumberInput (class containing input-text qty text)
        // typing '2.0' in NumberInput (class containing input-text qty text)
        // validating Span (xpath = //*[@class='order-total']//span)'s inner text is '54.00€'
        // clicking Anchor (class containing checkout-button button alt wc-forward)
        // validating Anchor (text containing Click here to login)'s href is 'https://demos.bellatrix.solutions/checkout/#'
        // scrolling to TextArea (id = order_comments)
        // typing 'Please send the rocket to my door step!' in TextArea (id = order_comments)
        // typing 'In' in TextInput (id = billing_first_name)
        // typing 'Deepthought' in TextInput (id = billing_last_name)
        // typing 'Automate The Planet Ltd.' in TextInput (id = billing_company)
        // selecting 'Bulgaria' from Select (id = billing_country)
        // validating TextInput (id = billing_address_1)'s placeholder is 'House number and street name'
        // typing 'bul. Yerusalim 5' in TextInput (id = billing_address_1)
        // typing 'bul. Yerusalim 6' in TextInput (id = billing_address_2)
        // typing 'Sofia' in TextInput (id = billing_city)
        // selecting 'Sofia-Grad' from Select (id = billing_state)
        // typing '1000' in TextInput (id = billing_postcode)
        // typing '+00359894646464' in PhoneField (id = billing_phone)
        // typing 'info@bellatrix.solutions' in EmailInput (id = billing_email)
        // checking CheckBox (id = createaccount)
        // clicking RadioButton (for containing payment_method_cheque)

        // You can notice that since we use validate assertions not the regular one they also present in the log:
        // validating Span (xpath = //*[@class='order-total']//span) inner text is '95.00€'
    }
}
