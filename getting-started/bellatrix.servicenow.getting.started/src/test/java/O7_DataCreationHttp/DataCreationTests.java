package O7_DataCreationHttp;

import O7_DataCreationHttp.data.BaseServiceNowDataTest;
import O7_DataCreationHttp.data.ProjectTables;
import O7_DataCreationHttp.data.incident.Incident;
import O7_DataCreationHttp.data.incident.IncidentRepository;
import O7_DataCreationHttp.data.incident.IncidentRepositoryFactory;
import O7_DataCreationHttp.data.user.UserRepositoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DataCreationTests extends BaseServiceNowDataTest {
    protected IncidentRepositoryFactory incidentFactory;
    protected UserRepositoryFactory userFactory;

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();

        incidentFactory = new IncidentRepositoryFactory();
        userFactory = new UserRepositoryFactory();

        registerRepositoriesAndFactories();
    }

    @Test
    public void createEntityWithDependencyDefaultTest() {
        // Build incident with dependencies using factory
        Incident incident = incidentFactory.buildDefault();
        incident.createWithDependencies();

        Assertions.assertNotNull(incident.getSysId());
        Assertions.assertNotNull(incident.getCaller().getSysId());

        incident.deleteDependenciesAndSelf();
    }

    @Test
    public void createEntityWithDependencyCustomizedTest() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        String expectedIncidentDescription = "Custom Description " + System.currentTimeMillis();
        String expectedUserName = "Custom User "+ System.currentTimeMillis();

        // Build incident with dependencies using factory
        Incident incident = incidentFactory.buildDefaultWithDependencies();

        incident.setShortDescription(expectedIncidentDescription);
        incident.getCaller().setUserName(expectedUserName);

        incident.createWithDependencies();

        Incident defaultIncident = incidentFactory.buildDefault();
        defaultIncident.setIdentifier(incident.getSysId());

        var entityFromAPI = defaultIncident.getWithDependencies();

        Assertions.assertEquals(entityFromAPI.getShortDescription(), expectedIncidentDescription);
        Assertions.assertEquals(entityFromAPI.getCaller().getUserName(), expectedUserName);

        incident.deleteDependenciesAndSelf();
    }

    @Test
    public void deleteEntity() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Build incident with dependencies using factory
        IncidentRepository incidentRepository = new IncidentRepository();

        Incident incident = incidentFactory.buildDefault();
        incident.createWithDependencies();

        List<Incident> incidents = incidentRepository.getAll().stream().filter(i -> i.getSysId().equals(incident.getSysId())).toList();
        Assertions.assertTrue(incidents.size()==1);

        incident.deleteDependenciesAndSelf();

        incidents = incidentRepository.getAll().stream().filter(i -> i.getSysId().equals(incident.getSysId())).toList();
        Assertions.assertFalse(incidents.size()==1);
    }

    @Test
    public void getAllEntities() {
        IncidentRepository incidentRepository = new IncidentRepository();

        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        List<Incident> incidents = incidentRepository.getAll();

        Assertions.assertEquals(serviceNowTableViewPage.map().totalRowsInfo().getText(), String.valueOf(incidents.size()));
    }

    @Test
    public void updateEntityWithDependencyCustomizedTest() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // Build incident with dependencies using factory
        Incident incident = incidentFactory.buildDefaultWithDependencies();
        incident.createWithDependencies();

        String expectedIncidentDescription = "Custom Description " + System.currentTimeMillis();
        String expectedUserName = "Custom User "+ System.currentTimeMillis();

        incident.setShortDescription(expectedIncidentDescription);
        incident.getCaller().setUserName(expectedUserName);

        incident.updateWithDependencies();

        Incident defaultIncident = incidentFactory.buildDefault();
        defaultIncident.setIdentifier(incident.getSysId());

        var entityFromAPI = defaultIncident.getWithDependencies();

        Assertions.assertEquals(entityFromAPI.getShortDescription(), expectedIncidentDescription);
        Assertions.assertEquals(entityFromAPI.getCaller().getUserName(), expectedUserName);

        incident.deleteDependenciesAndSelf();
    }
}