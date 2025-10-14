package O7_DataCreationHttp.data.incident;

import solutions.bellatrix.data.http.contracts.EntityFactory;

public class IncidentRepositoryFactory implements EntityFactory<Incident> {

    public IncidentRepositoryFactory() {
    }

    @Override
    public Incident buildDefault() {
        return Incident.builder()
                .shortDescription("Default Test Incident " + System.currentTimeMillis())
                .build();
    }
}