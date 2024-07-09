package controls.grid.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.bellatrix.web.components.advanced.TableHeader;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User {
    @TableHeader(name = "Last Name")
    public String lastName;
    @TableHeader(name = "First Name")
    public String firstName;
    @TableHeader(name = "Due")
    public String due;
    @TableHeader(name = "Web Site")
    public String website;
}
