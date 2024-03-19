package O12_page_objects.models;

import lombok.Getter;
import lombok.Setter;

public class PurchaseInfo {
    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;
    @Getter @Setter private String company;
    @Getter @Setter private String country;
    @Getter @Setter private String address1;
    @Getter @Setter private String address2;
    @Getter @Setter private String city;
    @Getter @Setter private String zip;
    @Getter @Setter private String phone;
    @Getter @Setter private String email;
    @Getter @Setter private Boolean shouldCreateAccount = false;
    @Getter @Setter private Boolean shouldCheckPayment = false;
}
