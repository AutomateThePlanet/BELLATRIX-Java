package controls.grid.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.bellatrix.playwright.components.advanced.TableHeader;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Employee {
    @TableHeader(name = "Order")
    public String order;
    @TableHeader(name = "Firstname")
    public String firstName;
    @TableHeader(name = "Lastname")
    public String lastName;
    @TableHeader(name = "Email Business")
    public String businessEmail;
    @TableHeader(name = "Email Personal")
    public String personalEmail;
}
