package O12_page_objects.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PurchaseInfo {
    private String firstName;
    private String lastName;
    private String company;
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private Boolean shouldCreateAccount = false;
    private Boolean shouldCheckPayment = false;
}
