package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels.AggregateEntities;
import solutions.bellatrix.servicenow.snSetupData.FxCurrency2Instance;
import solutions.bellatrix.servicenow.snSetupData.converters.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class TypeAdapterFactory {
    public static <TEntity extends ApiEntity> LinkedHashMap<Type, Object> all(Class<TEntity> entityClass) {
        LinkedHashMap<Type, Object> typeAdapters = allExceptEntities();
        typeAdapters.put(Entities.class, new EntitiesDeserializer<>(entityClass));
        typeAdapters.put(AggregateEntities.class, new AggregateEntitiesDeserializer());

        return typeAdapters;
    }

    public static LinkedHashMap<Type, Object> allExceptEntities() {
        LinkedHashMap<Type, Object> typeAdapters = new LinkedHashMap<>();
        typeAdapters.put(LocalDate.class, new LocalDateConverter());
        typeAdapters.put(LocalDateTime.class, new LocalDateTimeConverter());
        typeAdapters.put(String.class, new EmptyToNullConverter());
        typeAdapters.put(BigDecimal.class, new BigDecimalCurrencyConverter());
        typeAdapters.put(FxCurrency2Instance.class, new FxCurrencyConverter());
        typeAdapters.put(Duration.class, new DurationConverter());

        return typeAdapters;
    }

    public static LinkedHashMap<Type, Object> allForCurrencyRepo() {
        LinkedHashMap<Type, Object> typeAdapters = new LinkedHashMap<>();
        typeAdapters.put(BigDecimal.class, new BigDecimalCurrencyConverter());
        typeAdapters.put(String.class, new EmptyToNullConverter());
        typeAdapters.put(LocalDateTime.class, new LocalDateTimeConverter());

        return typeAdapters;
    }
}