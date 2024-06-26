package shadowdom;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.playwright.components.advanced.TableHeader;

@Getter @Setter
public class TableData {
    @TableHeader(name = "Last Name")
    public String lastName;
    @TableHeader(name = "First Name")
    public String firstName;
    @TableHeader(name = "Email")
    public String email;
    @TableHeader(name = "Due")
    public String due;
    @TableHeader(name = "Web Site")
    public String website;
}
