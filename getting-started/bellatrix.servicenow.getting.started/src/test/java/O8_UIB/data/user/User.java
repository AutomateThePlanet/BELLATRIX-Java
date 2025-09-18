package O8_UIB.data.user;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.infrastructure.core.entities.ServiceNowEntity;
import solutions.bellatrix.servicenow.models.annotations.TableTarget;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TableTarget ("sys_user")
public class User extends ServiceNowEntity<User> {
    @SerializedName("user_name")
    String userName;
    @SerializedName("name")
    String fullName;
}