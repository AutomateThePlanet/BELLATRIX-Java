package controls.table;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.web.components.advanced.TableHeader;

@Getter @Setter
public class DataTableExampleOne {
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