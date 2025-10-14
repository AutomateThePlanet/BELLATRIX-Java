package O8_UIB.data.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.infrastructure.core.entities.ServiceNowEntity;
import solutions.bellatrix.servicenow.models.annotations.TableTarget;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TableTarget("sc_cat_item")
public class CatalogItem extends ServiceNowEntity<O7_DataCreationHttp.data.incident.Incident> {
    @SerializedName("name")
    String name;
}