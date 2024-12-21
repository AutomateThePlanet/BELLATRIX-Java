package factory.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Employee {
    public Employee() {
    }

    public Employee(String order, String firstName, String lastName, String businessEmail, String personalEmail, Object... additionalData) {
        this.order = order;
        this.firstName = firstName;
        this.lastName = lastName;
        this.businessEmail = businessEmail;
        this.personalEmail = personalEmail;
        this.additionalData = additionalData;
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String order;
    public String firstName;
    public String lastName;
    public String businessEmail;
    public String personalEmail;
    public Object[] additionalData;
}
