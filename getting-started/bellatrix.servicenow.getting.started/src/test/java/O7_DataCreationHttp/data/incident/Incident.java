package O7_DataCreationHttp.data.incident;

import com.google.gson.annotations.SerializedName;
import solutions.bellatrix.servicenow.infrastructure.core.entities.ServiceNowEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.models.annotations.TableTarget;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TableTarget("incident")
public class Incident extends ServiceNowEntity<Incident> {
    @SerializedName("caller_id")
    String caller;
    @SerializedName("short_description")
    String shortDescription;
    @SerializedName("number")
    String number;
}