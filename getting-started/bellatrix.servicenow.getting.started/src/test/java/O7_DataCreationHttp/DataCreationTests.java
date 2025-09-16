package O7_DataCreationHttp;

import O4_TableView.data.ProjectTables;
import O7_DataCreationHttp.data.incident.Incident;
import O7_DataCreationHttp.data.incident.IncidentRepository;
import O7_DataCreationHttp.data.user.User;
import O7_DataCreationHttp.data.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.TimestampBuilder;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;

import java.util.List;

public class DataCreationTests extends ServiceNowBaseTest {
    User currentUser;

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        UserRepository userRepository = new UserRepository();
        currentUser = userRepository.getEntitiesByParameters(List.of(new QueryParameter("user_name", ConfigurationService.get(ServiceNowProjectSettings.class).getUserName()))).get(0);
    }

    @Test
    public void createEntity() {
        IncidentRepository incidentRepository = new IncidentRepository();

        Incident incident = incidentRepository.create(Incident.builder()
                .caller(currentUser.getSysId())
                .shortDescription(String.format("Incident Report %s", TimestampBuilder.buildUniqueTextByPrefix("au")))
                .build());

        var entityCreated = incidentRepository.getById(incident);

        Assertions.assertEquals(incident.getCaller(), entityCreated.getCaller());
    }

    @Test
    public void updateEntity() {
        IncidentRepository incidentRepository = new IncidentRepository();

        Incident incident = incidentRepository.create(Incident.builder()
                .caller(currentUser.getSysId())
                .shortDescription(String.format("Incident Report %s", TimestampBuilder.buildUniqueTextByPrefix("au")))
                .build());


        var updatedIncident = Incident.builder().sysId(incident.getSysId()).shortDescription("Description updated").build();
        incidentRepository.update(updatedIncident);

        var entityFromAPI = incidentRepository.getById(incident);

        Assertions.assertEquals(updatedIncident.getShortDescription(), entityFromAPI.getShortDescription());
    }

    @Test
    public void deleteEntity() {
        IncidentRepository incidentRepository = new IncidentRepository();

        Incident incident = incidentRepository.create(Incident.builder()
                .caller(currentUser.getSysId())
                .shortDescription(String.format("Incident Report %s", TimestampBuilder.buildUniqueTextByPrefix("au")))
                .build());

        incidentRepository.delete(incident);

        incidentRepository.validateEntityDoesNotExist(incident);
    }

    @Test
    public void getEntity() {
        IncidentRepository incidentRepository = new IncidentRepository();

        Incident incident = incidentRepository.create(Incident.builder()
                .caller(currentUser.getSysId())
                .shortDescription(String.format("Incident Report %s", TimestampBuilder.buildUniqueTextByPrefix("au")))
                .build());

        var incidentRegistered = incidentRepository.getById(incident);

        Assertions.assertEquals(incident.getShortDescription(), incidentRegistered.getShortDescription());
    }

    @Test
    public void getAllEntities() {
        IncidentRepository incidentRepository = new IncidentRepository();

        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        List<Incident> incidents = incidentRepository.getAll();

        Assertions.assertEquals(serviceNowTableViewPage.map().totalRowsInfo().getText(), String.valueOf(incidents.size()));
    }
}