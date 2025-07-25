package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import lombok.SneakyThrows;
import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;
import solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter;
import solutions.bellatrix.servicenow.utilities.TableApiParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("UnnecessaryLocalVariable")
public abstract class TableApiRepository<TEntity extends ApiEntity, TEntities extends Entities<TEntity>> extends HttpRepository<TEntity, TEntities> {
    public <Table extends ServiceNowProjectTable> TableApiRepository(Class<TEntity> entityClass, Table table) {
        super(entityClass, table);
    }

    public <Table extends ServiceNowProjectTable> TableApiRepository(Class<TEntity> entityClass) {
        super(entityClass);
    }

    public ApiResponse<TEntity> getById(String sysId) {
        var params = new TableApiParams().excludeReferenceLink().build();

        var entity = getById(sysId, params);
        setEntitySysClassName(entity);

        return entity;
    }

    @SneakyThrows
    public ApiResponse<TEntity> getById(ApiEntity apiEntity) {
        var params = new TableApiParams().excludeReferenceLink().build();

        var entity = getById(apiEntity.getEntityId(), params);

        return entity;
    }

    public ApiResponse<TEntities> getAll() {
        var params = new TableApiParams().excludeReferenceLink().build();

        var entities = getAll(params);
        setEntitiesSysClassName(entities);

        return entities;
    }

    public ApiResponse<TEntities> getAllWithCustomParams(LinkedHashMap<TableApiParameter, Object> customParams) {
        var entities = super.getAll(customParams);
        setEntitiesSysClassName(entities);
        return entities;
    }

    public ApiResponse<TEntities> getAllAttachmentsWithCustomParams(LinkedHashMap<TableApiParameter, Object> customParams) {
        var entities = super.getAllAttachments(customParams);
        setEntitiesSysClassName(entities);
        return entities;
    }

    public ApiResponse<TEntity> create(TEntity entity) {
        var params = new TableApiParams().excludeReferenceLink().build();

        var responseEntity = create(entity, params);
        setEntitySysClassName(responseEntity);

        return responseEntity;
    }

    public ApiResponse<TEntity> update(TEntity entity) {
        var params = new TableApiParams().excludeReferenceLink().build();

        var responseEntity = update(entity, params);
        setEntitySysClassName(responseEntity);

        return responseEntity;
    }

    @SuppressWarnings("unused")
    public void cleanAllCreatedBy(String... usersId) {
        cleanAllCreatedBy(Arrays.stream(usersId).toList());
    }

    public void cleanAllCreatedBy(List<String> usersId) {
        var entities = new ArrayList<TEntity>();
        usersId.forEach(userId -> {
            var currentEntities = getAllCreatedBy(userId).getResult().getEntities();
            if (currentEntities != null && currentEntities.size() > 0) {
                entities.addAll(currentEntities);
            }
        });

        if (entities.size() > 0) {
            entities.forEach(this::delete);
        }
    }

    private ApiResponse<TEntities> getAllCreatedBy(String userId) {
        var params = new TableApiParams()
            .excludeReferenceLink()
            .addSysparmEqualQuery("sys_created_by", userId)
            .build();

        return getAllWithCustomParams(params);
    }

    protected void setEntitySysClassName(ApiResponse<TEntity> responseEntity) {
        if (responseEntity.getResult().getSysClassName() == null) {
            responseEntity.getResult().setSysClassName(serviceNowProjectTable.getTable());
        }
    }

    private void setEntitiesSysClassName(ApiResponse<TEntities> response) {
        response.getResult().getEntities().forEach(x -> x.setSysClassName(serviceNowProjectTable.getTable()));
    }
}