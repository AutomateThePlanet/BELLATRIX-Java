package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.mapper.ObjectMapperType;

public class RestAssuredConfigFactory {
    public RestAssuredConfig createDefault() {
        var logConfig = LogConfig.logConfig()
                .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

        var objectMapperConfig = ObjectMapperConfig.objectMapperConfig()
                .gsonObjectMapperFactory(new CustomGsonObjectMapperFactory())
                .defaultObjectMapperType(ObjectMapperType.GSON);

        var config = RestAssuredConfig.config()
                .logConfig(logConfig)
                .objectMapperConfig(objectMapperConfig);
        return config;
    }
}