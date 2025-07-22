package data.entities;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class ServiceNowEntity<TEntity extends ServiceNowEntity> extends HttpEntity<String, TEntity> {
    @SerializedName("sys_id")
    private String sysId;
    @SerializedName("sys_domain")
    private String sysDomain;
    @SerializedName("sys_created_on")
    private LocalDateTime sysCreatedOn;
    @EqualsAndHashCode.Exclude
    @SerializedName("sys_created_by")
    private String sysCreatedBy;
    @EqualsAndHashCode.Exclude
    @SerializedName("sys_updated_on")
    private LocalDateTime sysUpdatedOn;
    @EqualsAndHashCode.Exclude
    @SerializedName("sys_updated_by")
    private String sysUpdatedBy;
    @EqualsAndHashCode.Exclude
    @SerializedName("sys_mod_count")
    private String sysModCount;
    @SerializedName("sys_domain_path")
    private String sysDomainPath;
    @SerializedName("sys_tags")
    private String sysTags;
    @SerializedName("sys_class_name")
    private String sysClassName;

    @Override
    public String getIdentifier() {
        return sysId;
    }
}