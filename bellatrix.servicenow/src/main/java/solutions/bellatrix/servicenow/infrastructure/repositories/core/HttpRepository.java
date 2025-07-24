package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.servicenow.contracts.ApiParameter;
import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;
import solutions.bellatrix.servicenow.data.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels.AggregateResponse;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels.GridDataField;
import solutions.bellatrix.servicenow.snSetupData.enums.ServiceNowApiPathSegment;
import solutions.bellatrix.servicenow.utilities.BaseInstancesUrlGeneration;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

@SuppressWarnings("UnnecessaryLocalVariable")
public abstract class HttpRepository<TEntity extends ApiEntity, TEntities extends Entities<TEntity>> {
    private Gson gson;
    private final RequestSpecification tableApiRequestSpecification;
    private final RequestSpecification aggregateApiRequestSpecification;
    private final RequestSpecification attachmentApiRequestSpecification;
    private final Class<TEntity> entityClass;
    protected ServiceNowProjectTable serviceNowProjectTable = null;
    private Class<?> entitiesClass;

    public HttpRepository(Class<TEntity> entityClass, Class<?> entitiesClass, RequestSpecification requestSpecification) {
        this.tableApiRequestSpecification = requestSpecification;
        this.aggregateApiRequestSpecification = null;
        this.attachmentApiRequestSpecification = null;

        String userName = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getUserName());
        String password = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getPassword());
        requestSpecification.auth().basic(userName, password);
        this.entityClass = entityClass;
        this.entitiesClass = entitiesClass;
        configureGson();
    }

    public <Table extends ServiceNowProjectTable> HttpRepository(Class<TEntity> entityClass, Table table) {
        this(entityClass, Entities.class, table);
    }

    public <Table extends ServiceNowProjectTable> HttpRepository(Class<TEntity> entityClass) {
        this(entityClass, Entities.class);
    }

    public <Table extends ServiceNowProjectTable> HttpRepository(Class<TEntity> entityClass, Class<?> entitiesClass, Table table) {
        var tableApiRequestSpecification = new RequestSpecBuilder()
                .setBaseUri(BaseInstancesUrlGeneration.getBaseUrl())
                .setBasePath(BaseInstancesUrlGeneration.getBasePath(table))
                .addPathParam("api", ServiceNowApiPathSegment.TABLE_API)
                .setConfig(new RestAssuredConfigFactory().createDefault())
                .addFilter(new CurlLoggingFilter())
                .build();

        var aggregateApiRequestSpecification = new RequestSpecBuilder()
                .setBaseUri(BaseInstancesUrlGeneration.getBaseUrl())
                .setBasePath(BaseInstancesUrlGeneration.getBasePath(table))
                .addPathParam("api", ServiceNowApiPathSegment.AGGREGATE_API)
                .setConfig(new RestAssuredConfigFactory().createDefault())
                .addFilter(new CurlLoggingFilter())
                .build();

        this.attachmentApiRequestSpecification = null;
        this.serviceNowProjectTable = table;

        String userName = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getUserName());
        String password = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getPassword());
        tableApiRequestSpecification.auth().basic(userName, password);
        aggregateApiRequestSpecification.auth().basic(userName, password);

        this.tableApiRequestSpecification = tableApiRequestSpecification;
        this.aggregateApiRequestSpecification = aggregateApiRequestSpecification;
        this.entityClass = entityClass;
        this.entitiesClass = entitiesClass;
        configureGson();
    }

    public <Table extends ServiceNowProjectTable> HttpRepository(Class<TEntity> entityClass, Class<?> entitiesClass) {
        this.tableApiRequestSpecification = null;
        this.aggregateApiRequestSpecification = null;
        var attachmentApiRequestSpecification = new RequestSpecBuilder()
                .setBaseUri(BaseInstancesUrlGeneration.getBaseUrl())
                .setBasePath(BaseInstancesUrlGeneration.getBasePathNoTable())
                .addPathParam("api", ServiceNowApiPathSegment.ATTACHMENT_API)
                .addFilter(new CurlLoggingFilter())
                .setConfig(new RestAssuredConfigFactory().createDefault())
                .build();

        String userName = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getUserName());
        String password = SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getPassword());
        attachmentApiRequestSpecification.auth().basic(userName, password);

        this.attachmentApiRequestSpecification = attachmentApiRequestSpecification;
        this.entityClass = entityClass;
        this.entitiesClass = entitiesClass;
        configureGson();
    }

    public String getEntityName() {
        return "result";
    }

    protected void configureGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = registerGsonTypeAdapter(gsonBuilder);
        gsonBuilder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.setLenient();
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    public Class<TEntity> getEntityClass() {
        return entityClass;
    }

    public Gson getGson() {
        return gson;
    }

    protected LinkedHashMap<Type, Object> getTypeAdapters() {
        return TypeAdapterFactory.all(entityClass);
    }

    protected GsonBuilder registerGsonTypeAdapter(GsonBuilder gsonBuilder) {
        for (var typeAdapter : getTypeAdapters().entrySet()) {
            gsonBuilder.registerTypeAdapter(typeAdapter.getKey(), typeAdapter.getValue());
        }

        return gsonBuilder;
    }

    public RequestSpecification getTableApiRequestSpecification() {
        return tableApiRequestSpecification;
    }

    public RequestSpecification getAttachmentApiRequestSpecification() {
        return attachmentApiRequestSpecification;
    }

    public RequestSpecification getAggregateApiRequestSpecification() {
        return aggregateApiRequestSpecification;
    }

    public ApiResponse<TEntity> getById(String id) {
        Response response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .filter(new CurlLoggingFilter())
                .contentType(ContentType.JSON)
                .get("/" + id);

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    public String getAttachmentFileById(String id) {
        Response response = RestAssured.given()
                .spec(getAttachmentApiRequestSpecification())
                .filter(new CurlLoggingFilter())
                .contentType(ContentType.JSON)
                .get("/" + id + "/file");

//        TEntity entity = getEntityFromResponse(response);

//        return new ApiResponse<>(response, entity);
        return response.getBody().asPrettyString();
    }

    public ApiResponse<TEntity> getById(String id, Map<String, Object> params) {
        Response response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .filter(new CurlLoggingFilter())
                .contentType(ContentType.JSON)
                .queryParams(params)
                .get("/" + id);

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    protected <Param extends ApiParameter> ApiResponse<TEntity> getById(String id, LinkedHashMap<Param, Object> params) {
        Response response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .filter(new CurlLoggingFilter())
                .contentType(ContentType.JSON)
                .queryParams(convertApiParamsMap(params))
                .get("/" + id);

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    public ApiResponse<TEntities> getAll(Map<String, String> params) {
        var response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(params)
                .get();

        TEntities entities = getEntities(response).getResult();

        return new ApiResponse<>(response, entities);

    }

    protected <Param extends ApiParameter> ApiResponse<TEntities> getAll(LinkedHashMap<Param, Object> params) {
        var response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(convertApiParamsMap(params))
               .get();

        TEntities entities = getEntities(response).getResult();

        return new ApiResponse<>(response, entities);
    }

    protected <Param extends ApiParameter> ApiResponse<TEntities> getAllAttachments(LinkedHashMap<Param, Object> params) {
        var response = RestAssured.given()
                .spec(getAttachmentApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(convertApiParamsMap(params))
                .get();

        TEntities entities = getEntities(response).getResult();

        return new ApiResponse<>(response, entities);
    }

    protected <Param extends ApiParameter> ApiResponse<TEntity> getAttachmentFileContent(LinkedHashMap<Param, Object> params) {
        var response = RestAssured.given()
                .spec(getAttachmentApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(convertApiParamsMap(params))
                .get();

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    public ApiResponse<TEntity> update(TEntity toBeUpdated) {
        var response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .body(getGson().toJson(toBeUpdated))
                .when()
                .put("/" + toBeUpdated.getEntityId());

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    public ApiResponse<TEntity> update(TEntity toBeUpdated, Map<String, Object> params) {
        var response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .body(getGson().toJson(toBeUpdated))
                .queryParams(params)
                .when()
                .put("/" + toBeUpdated.getEntityId());

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    protected <Param extends ApiParameter> ApiResponse<TEntity> update(TEntity toBeUpdated, LinkedHashMap<Param, Object> params) {
        var response = RestAssured.given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .body(getGson().toJson(toBeUpdated))
                .queryParams(convertApiParamsMap(params))
                .when()
                .put("/" + toBeUpdated.getEntityId());

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    public Response delete(int id) {
        return RestAssured.given().spec(getTableApiRequestSpecification()).log().all().when().delete("/" + id);
    }

    public Response delete(String sysId) {
        return RestAssured.given().spec(getTableApiRequestSpecification()).log().all().when().delete("/" + sysId);
    }

    public Response delete(TEntity toBeDeleted) {
        return RestAssured.given().spec(getTableApiRequestSpecification()).log().all().when().delete("/" + toBeDeleted.getEntityId());
    }

    public ApiResponse<TEntity> create(TEntity toBeCreated) {
        Response response = RestAssured
                .given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .body(getGson().toJson(toBeCreated))
                .when()
                .post();

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    public ApiResponse<TEntity> create(TEntity toBeCreated, Map<String, Object> params) {
        Response response = RestAssured
                .given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(params)
                .body(getGson().toJson(toBeCreated))
                .when()
                .post();

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    protected <Param extends ApiParameter> ApiResponse<TEntity> create(TEntity toBeCreated, LinkedHashMap<Param, Object> params) {
        Response response = RestAssured
                .given()
                .spec(getTableApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(convertApiParamsMap(params))
                .body(getGson().toJson(toBeCreated))
                .when()
                .post();

        TEntity entity = getEntityFromResponse(response);

        return new ApiResponse<>(response, entity);
    }

    @SneakyThrows
    private ApiResponse<TEntities> getEntities(Response response) {
        TEntities entity;

//        try {
            entity = (TEntities) getGson().fromJson(response.asString(), entitiesClass);
//        } catch (Exception e) {
//            entity = null;
//        }

        return new ApiResponse<>(response, entity);
    }

    protected TEntity getEntityFromResponse(Response response) {
        var entityJson = response.jsonPath().getJsonObject(getEntityName());
        String userJsonStr = getGson().toJson(entityJson, Map.class);
        TEntity entity = getGson().fromJson(userJsonStr, getEntityClass());
        return entity;
    }

    protected List<String> getGridDataFields() {
        return Arrays.stream(getEntityClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(GridDataField.class)).map(f -> f.getDeclaredAnnotation(SerializedName.class).value()).toList();
    }

    public <Param extends ApiParameter> AggregateResponse getAggregated(Map<Param, Object> apiParams) {
        var response = RestAssured.given()
                .spec(getAggregateApiRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParams(convertApiParamsMap(apiParams))
                .pathParam("api", ServiceNowApiPathSegment.AGGREGATE_API)
                .get();

        AggregateResponse aggregateStats = getAggregateStatsFromResponse(response);

        return aggregateStats;
    }

    protected AggregateResponse getAggregateStatsFromResponse(Response response) {
        var entityJson = response.jsonPath().getJsonObject(getEntityName());
        String userJsonStr = getGson().toJson(entityJson, Map.class);
        AggregateResponse aggregateStats = getGson().fromJson(userJsonStr, AggregateResponse.class);
        return aggregateStats;
    }

    protected <Param extends ApiParameter> Map<String, Object> convertApiParamsMap(Map<Param, Object> apiParams) {
        var result = new LinkedHashMap<String, Object>();
        apiParams.forEach((key, value) -> result.put(key.getParameter(), value));

        return result;
    }
}