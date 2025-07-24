package solutions.bellatrix.servicenow.snSetupData;

import solutions.bellatrix.servicenow.infrastructure.repositories.core.Entities;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TableApiRepository;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TypeAdapterFactory;
import solutions.bellatrix.servicenow.snSetupData.tables.Tables;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class FxCurrency2InstanceRepository extends TableApiRepository<FxCurrency2Instance, Entities<FxCurrency2Instance>> {
    public FxCurrency2InstanceRepository() {
        super(FxCurrency2Instance.class, Tables.FX_CURRENCY2_INSTANCE);
    }

    @Override
    protected LinkedHashMap<Type, Object> getTypeAdapters() {
        return TypeAdapterFactory.allForCurrencyRepo();
    }
}