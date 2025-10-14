package O7_DataCreationHttp.data;

import O7_DataCreationHttp.data.incident.Incident;
import O7_DataCreationHttp.data.incident.IncidentRepository;
import O7_DataCreationHttp.data.incident.IncidentRepositoryFactory;
import O7_DataCreationHttp.data.user.User;
import O7_DataCreationHttp.data.user.UserRepository;
import O7_DataCreationHttp.data.user.UserRepositoryFactory;
import solutions.bellatrix.data.configuration.FactoryProvider;
import solutions.bellatrix.data.configuration.RepositoryProvider;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;

public class BaseServiceNowDataTest extends ServiceNowBaseTest {
    public static void registerRepositoriesAndFactories() {
        // Register the factories
        FactoryProvider.INSTANCE.register(Incident.class, IncidentRepositoryFactory.class);
        FactoryProvider.INSTANCE.register(User.class, UserRepositoryFactory.class);

        // Register the repositories
        RepositoryProvider.INSTANCE.register(Incident.class, IncidentRepository.class);
        RepositoryProvider.INSTANCE.register(User.class, UserRepository.class);
    }
}