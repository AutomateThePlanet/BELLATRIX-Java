package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TableApiRepository;
import solutions.bellatrix.servicenow.snSetupData.enums.ServiceNowUser;
import java.util.Arrays;
import java.util.Map;
import lombok.SneakyThrows;

@SuppressWarnings("unused")
public class MassiveDataDeleter {
    @SneakyThrows
    public static void cleanAllCreatedBy(Map<Class<? extends ApiEntity>, Class<? extends TableApiRepository<?, ?>>> repositoriesMap, ServiceNowUser... users) {
        var repositoriesClasses = repositoriesMap.values();
        var userIds = Arrays.stream(users).map(ServiceNowUser::getValue).toList();

        for (var repositoryClass : repositoriesClasses) {
            var repository = (TableApiRepository<?, ?>) repositoryClass.getDeclaredConstructors()[0].newInstance();
            repository.cleanAllCreatedBy(userIds);
        }
    }
}