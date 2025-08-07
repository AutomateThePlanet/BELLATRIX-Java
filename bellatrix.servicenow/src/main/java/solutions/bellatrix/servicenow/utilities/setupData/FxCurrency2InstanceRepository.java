package solutions.bellatrix.servicenow.utilities.setupData;

import solutions.bellatrix.servicenow.infrastructure.core.repositories.TableApiRepository;
import solutions.bellatrix.servicenow.utilities.setupData.tables.Tables;

public class FxCurrency2InstanceRepository extends TableApiRepository<FxCurrency2Instance> {
    public FxCurrency2InstanceRepository() {
        super(FxCurrency2Instance.class, Tables.FX_CURRENCY2_INSTANCE);
    }
}