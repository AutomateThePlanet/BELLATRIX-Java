package O7_DataCreationHttp.data.incident;

import O7_DataCreationHttp.data.user.User;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solutions.bellatrix.data.annotations.Dependency;
import solutions.bellatrix.servicenow.infrastructure.core.entities.ServiceNowEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.models.annotations.TableTarget;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableTarget("incident")
public class Incident extends ServiceNowEntity <Incident>{
    @Dependency(entityType = User.class)
    @Getter
    private transient User caller;

    @SerializedName("short_description")
    String shortDescription;

    @SerializedName("number")
    String number;

    @SerializedName("caller_id")
    String idCaller;

    public void setCaller(User user) {
        this.caller = user;
        if (caller.getSysId() != null) {
            this.setIdCaller(caller.getSysId());
        }
    }
}