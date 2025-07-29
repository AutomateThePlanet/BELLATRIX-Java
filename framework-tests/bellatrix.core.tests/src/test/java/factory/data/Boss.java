package factory.data;

public class Boss {
    public Boss(String firstName, String lastName, String businessEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.businessEmail = businessEmail;
    }

    public Boss(String firstName, String lastName, String businessEmail, String personalEmail, Object[] additionalData) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.businessEmail = businessEmail;
        this.personalEmail = personalEmail;
        this.additionalData = additionalData;
    }

    public String firstName;
    public String lastName;
    public String businessEmail;
    public String personalEmail;
    public Object[] additionalData;
}
